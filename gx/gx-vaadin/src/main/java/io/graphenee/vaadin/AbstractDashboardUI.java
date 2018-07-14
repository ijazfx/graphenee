/*******************************************************************************
 * Copyright (c) 2016, 2018 Farrukh Ijaz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package io.graphenee.vaadin;

import java.util.Locale;

import org.vaadin.viritin.button.MButton;

import com.google.common.eventbus.Subscribe;
import com.vaadin.server.Page;
import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.core.exception.AuthenticationFailedException;
import io.graphenee.core.model.bean.GxSupportedLocaleBean;
import io.graphenee.i18n.api.LocalizerService;
import io.graphenee.vaadin.domain.DashboardUser;
import io.graphenee.vaadin.event.DashboardEvent.BrowserResizeEvent;
import io.graphenee.vaadin.event.DashboardEvent.CloseOpenWindowsEvent;
import io.graphenee.vaadin.event.DashboardEvent.UserLoggedOutEvent;
import io.graphenee.vaadin.event.DashboardEvent.UserLoginRequestedEvent;
import io.graphenee.vaadin.event.DashboardEventBus;
import io.graphenee.vaadin.util.VaadinUtils;

@SuppressWarnings("serial")
public abstract class AbstractDashboardUI extends UI {

	@Override
	protected void init(final VaadinRequest request) {
		if (dashboardSetup().shouldLocalize()) {
			LocalizerService localizer = VaadinSession.getCurrent().getAttribute(LocalizerService.class);
			if (localizer == null) {
				localizer = dashboardSetup().localizer();
				VaadinSession.getCurrent().setAttribute(LocalizerService.class, localizer);
			}
			Locale locale = VaadinSession.getCurrent().getAttribute(Locale.class);
			if (locale == null) {
				locale = Locale.ENGLISH;
				VaadinSession.getCurrent().setAttribute(Locale.class, locale);
			}
			setLocale(locale);
		}

		DashboardEventBus.sessionInstance().register(this);
		Responsive.makeResponsive(this);
		addStyleName(ValoTheme.UI_WITH_MENU);

		updateContent();

		// Some views need to be aware of browser resize events so a
		// BrowserResizeEvent gets fired to the event bus on every occasion.
		Page.getCurrent().addBrowserWindowResizeListener(new BrowserWindowResizeListener() {
			@Override
			public void browserWindowResized(final BrowserWindowResizeEvent event) {
				DashboardEventBus.sessionInstance().post(new BrowserResizeEvent(event.getWidth(), event.getHeight()));
			}
		});

		localizeRecursively(this);
	}

	/**
	 * Updates the correct content for this UI based on the current user status.
	 * If the user is logged in with appropriate privileges, main view is shown.
	 * Otherwise login view is shown.
	 */
	protected void updateContent() {
		DashboardUser user = (DashboardUser) VaadinSession.getCurrent().getSession().getAttribute(DashboardUser.class.getName());
		if (user != null) {
			// Authenticated user
			VaadinSession.getCurrent().setAttribute(DashboardUser.class, user);
			setContent(dashboardSetup().defaultComponent());
			removeStyleName("loginview");
			try {
				UI.getCurrent().getNavigator().navigateTo(getNavigator().getState());
			} catch (Exception ex) {
				UI.getCurrent().getNavigator().navigateTo(dashboardSetup().dashboardViewName());
				// getNavigator().navigateTo(dashboardSetup().dashboardViewName());
			}
		} else {
			CssLayout rootLayout = new CssLayout();
			rootLayout.setSizeFull();
			rootLayout.addComponent(dashboardSetup().loginComponent());

			Label poweredByLabel = new Label();
			poweredByLabel.setWidthUndefined();
			poweredByLabel.setStyleName("powered-by");
			poweredByLabel.setContentMode(ContentMode.HTML);
			poweredByLabel.setValue("Powered by <strong>Graphenee&trade;</strong>");
			poweredByLabel.setVisible(dashboardSetup().shouldShowPoweredByGraphenee());

			rootLayout.addComponent(poweredByLabel);

			if (dashboardSetup().shouldLocalize() && dashboardSetup().supportedLocales() != null) {
				CssLayout languageBar = new CssLayout();
				languageBar.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
				languageBar.addStyleName("language-bar");

				for (GxSupportedLocaleBean supportedLocaleBean : dashboardSetup().supportedLocales()) {
					MButton localeButton = new MButton(supportedLocaleBean.getLocaleName());
					localeButton.addClickListener(event -> {
						Locale locale = new Locale(supportedLocaleBean.getLocaleCode());
						VaadinSession.getCurrent().setAttribute(Locale.class, locale);
						UI.getCurrent().getPage().setLocation("/");
					});
					localeButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
					languageBar.addComponent(localeButton);
				}

				rootLayout.addComponent(languageBar);
			}

			setContent(rootLayout);
			// setContent(dashboardSetup().loginComponent());
			addStyleName("loginview");
		}
	}

	protected abstract AbstractDashboardSetup dashboardSetup();

	@Subscribe
	public void userLoginRequested(final UserLoginRequestedEvent event) {
		try {
			DashboardUser user = dashboardSetup().authenticate(event);
			VaadinSession.getCurrent().setAttribute(DashboardUser.class.getName(), user);
			VaadinSession.getCurrent().getSession().setAttribute(DashboardUser.class.getName(), user);
			updateContent();
		} catch (AuthenticationFailedException e) {
			Notification notification = new Notification("Access Denied", e.getMessage(), Type.ERROR_MESSAGE);
			notification.setDelayMsec(3000);
			notification.setPosition(Position.BOTTOM_CENTER);
			notification.show(getPage());
		}
	}

	@Subscribe
	public void userLoggedOut(final UserLoggedOutEvent event) {
		// When the user logs out, current VaadinSession gets closed and the
		// page gets reloaded on the login screen. Do notice the this doesn't
		// invalidate the current HttpSession.
		try {
			VaadinSession.getCurrent().getSession().invalidate();
			VaadinSession.getCurrent().close();
		} catch (Exception ex) {
			// To avoid NPE, most likely user UI unattended for longer and
			// session got terminated.
		}
		// Page.getCurrent().reload();
		Page.getCurrent().setLocation("/");
	}

	@Subscribe
	public void closeOpenWindows(final CloseOpenWindowsEvent event) {
		for (Window window : getWindows()) {
			window.close();
		}
	}

	protected String localizedSingularValue(String key) {
		return VaadinUtils.localizedSingularValue(key);
	}

	protected String localizedPluralValue(String key) {
		return VaadinUtils.localizedSingularValue(key);
	}

	protected void localizeRecursively(Component component) {
		VaadinUtils.localizeRecursively(component);
	}

	protected String localizedSingularValue(Locale locale, String key) {
		return VaadinUtils.localizedSingularValue(key);
	}

	protected String localizedPluralValue(Locale locale, String key) {
		return VaadinUtils.localizedSingularValue(key);
	}

	protected void localizeRecursively(Locale locale, Component component) {
		VaadinUtils.localizeRecursively(component);
	}

}
