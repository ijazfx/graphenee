package io.graphenee.vaadin.flow;

import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.login.AbstractLogin.ForgotPasswordEvent;
import com.vaadin.flow.component.login.AbstractLogin.LoginEvent;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinSession;

import io.graphenee.common.GxAuthenticatedUser;
import io.graphenee.common.exception.AuthenticationFailedException;
import io.graphenee.common.exception.PasswordChangeRequiredException;
import io.graphenee.util.ServletUtil;
import io.graphenee.vaadin.flow.utils.DashboardUtils;
import jakarta.annotation.PostConstruct;

@CssImport(value = "./styles/graphenee.css")
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
		Div rootLayout = new Div();
		rootLayout.addClassName("gx-login-root-layout");
		add(rootLayout);

		Image appLogo = flowSetup().appLogo();
		if (appLogo != null) {
			Div appLogoDiv = new Div(appLogo);
			appLogoDiv.addClassName("gx-login-app-logo");
			rootLayout.add(appLogoDiv);
		}

		Div heading = new Div();
		heading.addClassName("gx-login-header");
		Span appTitle = new Span(flowSetup().appTitle());
		appTitle.addClassName("gx-login-app-title");
		Span appVersion = new Span(flowSetup().appVersion());
		appVersion.addClassName("gx-login-app-version");
		Div appTitleVersion = new Div(appTitle, appVersion);
		appTitleVersion.addClassName("gx-login-app-title-version");
		heading.add(appTitleVersion);
		rootLayout.add(heading);

		LoginForm loginForm = new LoginForm();
		loginForm.addClassName("gx-login-form");
		loginForm.setForgotPasswordButtonVisible(true);
		LoginI18n loginI18n = LoginI18n.createDefault();
		loginI18n.getForm().setSubmit("Login");
		loginI18n.getForm().setTitle("");
		loginI18n.getForm().setForgotPassword("Forgot Password? Click here!");
		loginI18n.getForm().setUsername("Username or Email");
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
					if (lastRoute != null && user.canDoAction(lastRoute, "view", null)) {
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

		rootLayout.add(loginForm);
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

	/**
	 * The method returns the host header for the incoming request.
	 * @return value of the Host header
	 */
	protected String host() {
		return ServletUtil.host(VaadinServletRequest.getCurrent());
	}

}
