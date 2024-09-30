package io.graphenee.vaadin.flow.base;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

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

import io.graphenee.core.api.GxUserSessionDetailDataService;
import io.graphenee.core.exception.AuthenticationFailedException;
import io.graphenee.core.exception.PasswordChangeRequiredException;
import io.graphenee.core.model.GxAuthenticatedUser;
import io.graphenee.vaadin.flow.component.GxNotification;
import io.graphenee.vaadin.flow.utils.DashboardUtils;

@CssImport("./styles/gx-common.css")
public abstract class GxAbstractLoginView extends VerticalLayout implements HasUrlParameter<String> {

	@Autowired
	GxUserSessionDetailDataService userSessionDetailDataService;

	@Autowired
	ApplicationEventPublisher eventPublisher;

	@Autowired
	HttpServletRequest httpServletRequest;

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
				System.out.println(httpServletRequest.getHeader("User-Agent"));
				GxAuthenticatedUser user = onLogin(e);
				if (user.getNamespaceFault() == null) { // user is admin

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

				} else if (user.getNamespaceFault() != null && user.getOid() != null) {
					Integer oidNamespace = user.getNamespaceFault().getBean().getOid();
					try {
						Boolean isUserLimitReached = userSessionDetailDataService.isUserLimitReached(oidNamespace,
								user.getOid());
						if (isUserLimitReached) {
							GxNotification.error("User limited reached.");
						} else {
							VaadinSession session = VaadinSession.getCurrent();
							// logoutExistingSessions(user);
							GxNotification.error("Success.");
							userSessionDetailDataService.saveNewSessionForUser(oidNamespace, user.getOid(),
									DashboardUtils.getMacAddress() + getBrowserName(
											httpServletRequest.getHeader("User-Agent")));

							session.setAttribute(GxAuthenticatedUser.class, user);
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
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				} else {
					GxNotification.error("Error occured while login.");
				}

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
	
	private static String getBrowserName(String userAgent) {
		if (userAgent.contains("Chrome")) {
			return "Chrome";
		} else if (userAgent.contains("Firefox")) {
			return "Mozilla Firefox";
		} else if (userAgent.contains("Safari") && !userAgent.contains("Chrome")) {
			return "Safari";
		} else if (userAgent.contains("Edge")) {
			return "Microsoft Edge";
		} else if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
			return "Internet Explorer";
		} else {
			return "Unknown Browser";
		}
	}

	public void logoutExistingSessions(GxAuthenticatedUser user) {
		// IMap<String, UserSessionBean> map = hazelcastInstance.getMap("userSessionMap");
		// UserSessionBean userSessionBean = map.get(user.getUsername());
		// VaadinSession s = VaadinSession.getCurrent();
		// if (userSessionBean != null && s.getSession().getId().equals(userSessionBean.getSessionId())) {
		// 	s.close();
		// }

		// 	// HttpServletRequest httpServletRequest =
		// 	// VaadinServletRequest.getCurrent().getHttpServletRequest();
		// 	// HttpSession httpSession = httpServletRequest.getSession(true);
		// 	// if (httpSession != null) {
		// 	// Collection<VaadinSession> allSessions =
		// 	// VaadinSession.getAllSessions(httpSession);
		// 	// Set<VaadinSession> allSessionsForUser = new HashSet<>();
		// 	// for (VaadinSession vaadinSession : allSessions) {
		// 	// GxAuthenticatedUser sessionUser =
		// 	// vaadinSession.getAttribute(GxAuthenticatedUser.class);
		// 	// if (user.equals(sessionUser)) {
		// 	// allSessionsForUser.add(vaadinSession);
		// 	// }
		// 	// }
		// 	// System.out.println("Total sessions for " + user.getUsername() + " are: " +
		// 	// allSessionsForUser.size());
		// 	// for (VaadinSession vaadinSession : allSessionsForUser) {
		// 	// vaadinSession.close();
		// 	// }
		// 	// }

		// 	List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
		// 	for (Object p : allPrincipals) {
		// 		if (p instanceof GxUserAccount) {
		// 			GxUserAccount user = (GxUserAccount) p;
		// 			if (user.getUsername().equals(username)) {
		// 				List<SessionInformation> sessions = sessionRegistry.getAllSessions(username,
		// 						false);
		// 				for (SessionInformation session : sessions) {
		// 					session.expireNow();
		// 				}
		// 			}
		// 		}
		// 	}
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

	// public synchronized void updateUserSession(String username, VaadinSession
	// newSession) {
	// System.err.println("Updaing Session: " + newSession);
	// VaadinSession existingUserSession = userSessions.get(username);
	// closeExistingSession(username, existingUserSession);
	// if (newSession != null)
	// userSessions.put(username, newSession);
	// }

	// private void closeExistingSession(String username, VaadinSession
	// existingSession) {
	// if (existingSession != null && existingSession.getSession() != null) {
	// existingSession.getSession().invalidate();
	// existingSession.close();
	// System.err.println("Closed Session: " + existingSession);
	// userSessions.remove(username);
	// }
	// }

}
