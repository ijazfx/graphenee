package io.graphenee.vaadin.flow;

import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Direction;
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
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
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
public abstract class GxAbstractLoginView extends FlexLayout implements HasUrlParameter<String>, LocaleChangeObserver {

	private static final long serialVersionUID = 1L;
	private String lastRoute;

	private LoginForm loginForm;
	private Span appTitle = new Span();
	private Span appVersion = new Span();

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
		appTitle.addClassName("gx-login-app-title");
		appVersion.addClassName("gx-login-app-version");
		Div appTitleVersion = new Div(appTitle, appVersion);
		appTitleVersion.addClassName("gx-login-app-title-version");
		heading.add(appTitleVersion);
		rootLayout.add(heading);

		loginForm = new LoginForm();
		loginForm.addClassName("gx-login-form");
		loginForm.setForgotPasswordButtonVisible(true);

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
		rootLayout.add(new GxLanguageBar());
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

	protected abstract GxAuthenticatedUser onLogin(LoginEvent event)
			throws AuthenticationFailedException, PasswordChangeRequiredException;

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

	@Override
	public void localeChange(LocaleChangeEvent event) {
		String lang = event.getLocale().getLanguage().split("_")[0];
		boolean isRtl = "ar, he, fa, ur, ps, sd, ckb, ug, yi".contains(lang);
		event.getUI().setDirection(isRtl ? Direction.RIGHT_TO_LEFT : Direction.LEFT_TO_RIGHT);
		event.getUI().access(() -> {
			localizeUI(event.getUI());
		});
	}

	protected void localizeUI(UI ui) {
		appTitle.setText(flowSetup().appTitle());
		appVersion.setText(flowSetup().appVersion());
		LoginI18n loginI18n = LoginI18n.createDefault();
		loginI18n.getForm().setSubmit(getTranslation("Login"));
		loginI18n.getForm().setTitle(getTranslation("Enter Credentials")); // "");
		loginI18n.getForm().setForgotPassword(getTranslation("Forgot Password? Click here!"));
		loginI18n.getForm().setUsername(getTranslation("Username or Email"));
		loginI18n.getForm().setPassword(getTranslation("Password"));
		loginForm.setI18n(loginI18n);
	}

}
