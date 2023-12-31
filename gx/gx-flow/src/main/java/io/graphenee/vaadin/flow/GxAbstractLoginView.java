package io.graphenee.vaadin.flow;

import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.AbstractLogin.ForgotPasswordEvent;
import com.vaadin.flow.component.login.AbstractLogin.LoginEvent;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.VaadinSession;

import io.graphenee.core.exception.AuthenticationFailedException;
import io.graphenee.core.exception.PasswordChangeRequiredException;
import io.graphenee.core.model.GxAuthenticatedUser;
import io.graphenee.vaadin.flow.utils.DashboardUtils;
import jakarta.annotation.PostConstruct;

@CssImport("./styles/graphenee.css")
public abstract class GxAbstractLoginView extends FlexLayout implements HasUrlParameter<String> {

	private static final long serialVersionUID = 1L;
	private String lastRoute;

	public GxAbstractLoginView() {
		addClassName("gx-login-view");
		setSizeFull();
		setFlexDirection(FlexDirection.COLUMN);
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);
	}

	@PostConstruct
	private void postBuild() {
		FlexLayout loginFormLayout = new FlexLayout();
		loginFormLayout.addClassName("gx-login-form-layout");
		loginFormLayout.setFlexDirection(FlexDirection.COLUMN);
		loginFormLayout.setAlignItems(Alignment.CENTER);
		loginFormLayout.setJustifyContentMode(JustifyContentMode.CENTER);

		HorizontalLayout titleVersionLayout = new HorizontalLayout();
		titleVersionLayout.setWidthFull();
		titleVersionLayout.addClassName("gx-login-title-layout");
		H1 title = new H1(appTitle());
		title.addClassName("gx-login-title");
		H5 version = new H5(appVersion());
		version.addClassName("gx-login-version");
		titleVersionLayout.add(title, version);
		titleVersionLayout.setDefaultVerticalComponentAlignment(Alignment.END);

		LoginForm loginForm = new LoginForm();
		loginForm.addClassName("gx-login-form");
		loginForm.setForgotPasswordButtonVisible(true);
		LoginI18n loginI18n = LoginI18n.createDefault();
		loginI18n.getForm().setSubmit("Sign In");
		loginI18n.getForm().setTitle("Enter Credentials");
		loginI18n.getForm().setForgotPassword("Forgot Password? Click here!");
		loginI18n.getForm().setUsername("Username");
		loginI18n.getForm().setPassword("Password");
		loginForm.setI18n(loginI18n);

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

		Image logo = appLogo();
		if (logo != null) {
			logo.setHeight("3.5rem");
			logo.getStyle().set("border-radius", "0.5rem");
			titleVersionLayout.add(logo);
			titleVersionLayout.expand(version);
		}

		loginFormLayout.add(titleVersionLayout, loginForm);
		//loginFormLayout.setWidth("400px");
		add(loginFormLayout);
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
