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
import com.vaadin.flow.server.VaadinSession;

import io.graphenee.common.GxAuthenticatedUser;
import io.graphenee.common.exception.AuthenticationFailedException;
import io.graphenee.common.exception.PasswordChangeRequiredException;
import io.graphenee.vaadin.flow.utils.DashboardUtils;
import jakarta.annotation.PostConstruct;

@CssImport(value = "./styles/graphenee.css")
public abstract class GxAbstractEnhancedLoginView extends FlexLayout implements HasUrlParameter<String> {

	private static final long serialVersionUID = 1L;
	private String lastRoute;

	public GxAbstractEnhancedLoginView() {
		addClassName("gx-login-view");
		setSizeFull();
		setFlexDirection(FlexDirection.ROW);
		setAlignItems(Alignment.STRETCH);
	}

	@PostConstruct
	private void postBuild() {
		FlexLayout leftPanel = new FlexLayout();
		leftPanel.addClassName("gx-login-left-panel");
		leftPanel.setFlexDirection(FlexDirection.COLUMN);
		leftPanel.setAlignItems(Alignment.CENTER);
		leftPanel.setJustifyContentMode(JustifyContentMode.CENTER);

		Image backgroundImage = flowSetup().backgroundImage();
		if (backgroundImage != null) {
			leftPanel.addClassName("has-background-image");
			backgroundImage.addClassName("gx-login-background-image");
			leftPanel.add(backgroundImage);
		}

		Div infoLayout = new Div();
		infoLayout.addClassName("gx-login-info-layout");

		FlexLayout titleLayout = new FlexLayout();
		titleLayout.addClassName("gx-login-title-layout");
		titleLayout.setAlignItems(Alignment.CENTER);

		Image appLogo = flowSetup().appLogo();
		if (appLogo != null) {
			appLogo.addClassName("gx-login-app-logo");
			titleLayout.add(appLogo);
		}

		Span appTitle = new Span(flowSetup().appTitle());
		appTitle.addClassName("gx-login-app-title");
		appTitle.getStyle().set("font-weight", "bold");
		titleLayout.add(appTitle);

		infoLayout.add(titleLayout);

		if (flowSetup().appDescription() != null) {
			Span appDescription = new Span(flowSetup().appDescription());
			appDescription.addClassName("gx-login-app-description");
			infoLayout.add(appDescription);
		}

		leftPanel.add(infoLayout);

		FlexLayout rightPanel = new FlexLayout();
		rightPanel.addClassName("gx-login-right-panel");
		rightPanel.setFlexDirection(FlexDirection.COLUMN);
		rightPanel.setAlignItems(Alignment.CENTER);
		rightPanel.setJustifyContentMode(JustifyContentMode.CENTER);
		rightPanel.getStyle().set("background-color", "var(--background-color)");

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

		rightPanel.add(loginForm);

		add(leftPanel, rightPanel);
		setFlexGrow(1, leftPanel);
		setFlexGrow(1, rightPanel);
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
