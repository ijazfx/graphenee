package io.graphenee.vaadin.flow.base;

import java.util.List;

import javax.annotation.PostConstruct;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.login.AbstractLogin.ForgotPasswordEvent;
import com.vaadin.flow.component.login.AbstractLogin.LoginEvent;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.VaadinSession;

import io.graphenee.core.exception.AuthenticationFailedException;
import io.graphenee.core.exception.PasswordChangeRequiredException;
import io.graphenee.core.model.GxAuthenticatedUser;
import io.graphenee.vaadin.flow.utils.DashboardUtils;

@CssImport("./styles/gx-common.css")
public abstract class GxAbstractLoginView extends VerticalLayout implements HasUrlParameter<String> {

	private static final long serialVersionUID = 1L;
	private String lastRoute;
	private Image appLogo;

	public GxAbstractLoginView() {
		setSizeFull();
		setClassName("gx-login-view");
		setJustifyContentMode(JustifyContentMode.CENTER);
		setAlignItems(Alignment.CENTER);
	}

	@PostConstruct
	private void postBuild() {
		FlexLayout headingLayout = new FlexLayout();
		headingLayout.setAlignItems(Alignment.BASELINE);
		headingLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
		headingLayout.setWidth("328px");
		Span heading = new Span(appTitle());
		heading.getStyle().set("color", "var(--app-layout-bar-font-color)");
		heading.getStyle().set("font-size", "var(--lumo-font-size-xxl)");
		Span version = new Span(appVersion());
		version.getStyle().set("color", "var(--app-layout-bar-font-color)");
		version.getStyle().set("font-size", "var(--lumo-font-size-s)");
		headingLayout.add(heading, version);

		add(headingLayout);

		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSpacing(false);
		rootLayout.setPadding(false);
		rootLayout.setWidth("-1px");
		rootLayout.getElement().getStyle().set("border-radius", "var(--lumo-border-radius-l)");
		rootLayout.getElement().getStyle().set("background", backgoundColor());

		appLogo = appLogo();
		if (appLogo != null) {
			appLogo.getElement().getStyle().set("padding-top", "var(--lumo-space-l)");
			appLogo.setWidth("100px");
			rootLayout.add(appLogo);
			rootLayout.setHorizontalComponentAlignment(Alignment.CENTER, appLogo);
		}

		add(rootLayout);

		LoginForm loginForm = new LoginForm();
		loginForm.getElement().getStyle().set("border-radius", "var(--lumo-border-radius-l)");
		loginForm.getElement().getStyle().set("background", backgoundColor());
		loginForm.setForgotPasswordButtonVisible(true);
		LoginI18n loginI18n = LoginI18n.createDefault();
		loginI18n.getForm().setSubmit("Sign In");
		loginI18n.getForm().setTitle("Enter Credentials");
		loginI18n.getForm().setForgotPassword("Forgot Password? Click here to Reset!");
		loginI18n.getForm().setUsername("Username");
		loginI18n.getForm().setPassword("Password");
		loginForm.setI18n(loginI18n);

		rootLayout.add(loginForm);

		loginForm.addLoginListener(e -> {
			try {
				GxAuthenticatedUser user = onLogin(e);
				VaadinSession.getCurrent().setAttribute(GxAuthenticatedUser.class, user);
				DashboardUtils.setCurrentUI(user, getUI().orElse(UI.getCurrent()));
				RouteConfiguration rc = RouteConfiguration.forSessionScope();
				registerRoutesOnSuccessfulAuthentication(rc, user);
				flowSetup().menuItems().forEach(mi -> {
					registerRoute(user, rc, mi);
				});
				getUI().ifPresent(ui -> {
					if (lastRoute != null && user.canDoAction(lastRoute, "view")) {
						ui.navigate(lastRoute);
					} else {
						ui.navigate(flowSetup().defaultRoute());
					}
				});
			} catch (AuthenticationFailedException afe) {
				Notification.show(afe.getMessage(), 5000, Position.BOTTOM_CENTER);
			} catch (PasswordChangeRequiredException pcre) {
				getUI().ifPresent(ui -> {
					ui.navigate("reset-password");
				});
			} finally {
				loginForm.setEnabled(true);
			}
		});
		loginForm.addForgotPasswordListener(e -> {
			onForgotPassword(e);
		});

	}

	protected String backgoundColor() {
		return "white";
	}

	protected Image appLogo() {
		return flowSetup().appLogo();
	}

	protected String appVersion() {
		return flowSetup().appVersion();
	}

	protected String appTitle() {
		return flowSetup().appTitle();
	}

	private void registerRoute(GxAuthenticatedUser user, RouteConfiguration rc, GxMenuItem mi) {
		Class<? extends Component> navigationTarget = mi.getComponentClass();
		if (navigationTarget != null) {
			String route = mi.getRoute();
			rc.removeRoute(navigationTarget);
			rc.removeRoute(route);
			rc.setRoute(route, navigationTarget, List.of(flowSetup().routerLayout()));
		}
		if (mi.hasChildren()) {
			mi.getChildren().forEach(cmi -> {
				registerRoute(user, rc, cmi);
			});
		}
	}

	protected abstract GxAbstractFlowSetup flowSetup();

	protected abstract GxAuthenticatedUser onLogin(LoginEvent event) throws AuthenticationFailedException, PasswordChangeRequiredException;

	protected void onForgotPassword(ForgotPasswordEvent event) {
		getUI().ifPresent(ui -> {
			ui.navigate("reset-password");
		});
	}

	protected void registerRoutesOnSuccessfulAuthentication(RouteConfiguration rc, GxAuthenticatedUser user) {
	}

	@Override
	public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
		if (parameter != null && !parameter.startsWith("login"))
			this.lastRoute = parameter;
	}

}
