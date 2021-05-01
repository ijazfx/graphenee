package io.graphenee.vaadin.flow.base;

import javax.annotation.PostConstruct;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.login.AbstractLogin.ForgotPasswordEvent;
import com.vaadin.flow.component.login.AbstractLogin.LoginEvent;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.VaadinSession;

import io.graphenee.core.exception.AuthenticationFailedException;
import io.graphenee.core.exception.PasswordChangeRequiredException;
import io.graphenee.core.model.GxAuthenticatedUser;

@CssImport("./styles/gx-common.css")
public abstract class GxAbstractLoginView extends VerticalLayout implements HasUrlParameter<String> {

	private static final long serialVersionUID = 1L;
	private String lastRoute;

	public GxAbstractLoginView() {
		setSizeFull();
		setClassName("gx-abstract-login-view");
		getStyle().set("display", "flex");
		getStyle().set("flex-flow", "initial");
		getStyle().set("background", "var(--app-layout-bar-background-color)");
		setJustifyContentMode(JustifyContentMode.CENTER);
	}

	@PostConstruct
	private void postBuild() {
		LoginForm loginForm = new LoginForm();
		loginForm.getElement().getStyle().set("border-radius", "var(--lumo-border-radius-l)");
		loginForm.getElement().getStyle().set("padding", "var(--lumo-border-radius)");
		loginForm.getElement().getStyle().set("background", "white");
		loginForm.setForgotPasswordButtonVisible(true);
		LoginI18n loginI18n = LoginI18n.createDefault();
		loginI18n.getForm().setTitle(flowSetup().appTitle());
		loginI18n.getForm().setForgotPassword("Forgot Password? Click here to Reset!");
		loginI18n.getForm().setUsername("Login ID");
		loginI18n.getForm().setPassword("Password");
		loginForm.setI18n(loginI18n);

		add(loginForm);
		setAlignSelf(Alignment.CENTER, loginForm);

		loginForm.addLoginListener(e -> {
			try {
				GxAuthenticatedUser user = onLogin(e);
				VaadinSession.getCurrent().setAttribute(GxAuthenticatedUser.class, user);
				RouteConfiguration rc = RouteConfiguration.forSessionScope();
				registerRoutesOnSuccessfulAuthentication(rc, user);
				flowSetup().menuItems().forEach(mi -> {
					registerRoute(user, rc, mi);
					if (mi.hasChildren()) {
						mi.getChildren().forEach(cmi -> {
							registerRoute(user, rc, cmi);
						});
					}
				});
				getUI().ifPresent(ui -> {
					if (lastRoute != null) {
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

	private void registerRoute(GxAuthenticatedUser user, RouteConfiguration rc, GxMenuItem mi) {
		Class<? extends Component> navigationTarget = mi.getComponentClass();
		if (navigationTarget != null) {
			String route = mi.getRoute();
			//if (route != null && user.canDoAction(route, "view")) {
			rc.removeRoute(navigationTarget);
			rc.removeRoute(route);
			rc.setRoute(route, navigationTarget, flowSetup().routerLayout());
			//}
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
	public void setParameter(BeforeEvent event, String parameter) {
		if (parameter != null && !parameter.startsWith("login"))
			this.lastRoute = parameter;
	}

}
