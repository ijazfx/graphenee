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

import com.vaadin.server.Page;
import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.core.model.GxAuthenticatedUser;
import io.graphenee.i18n.api.LocalizerService;
import io.graphenee.vaadin.event.DashboardEvent.BrowserResizeEvent;
import io.graphenee.vaadin.event.DashboardEventBus;
import io.graphenee.vaadin.util.VaadinUtils;
import io.graphenee.vaadin.view.MainComponent;

@SuppressWarnings("serial")
public abstract class AbstractDashboardUI extends UI {

	@Override
	protected void init(final VaadinRequest request) {

		if (this.getClass().isAnnotationPresent(GxSecuredUI.class)) {
			GxAuthenticatedUser user = VaadinSession.getCurrent().getAttribute(GxAuthenticatedUser.class);
			if (user == null) {
				Page.getCurrent().setLocation("/login");
				return;
			}
		}

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

		DashboardEventBus.sessionInstance().register(AbstractDashboardUI.this);

		Responsive.makeResponsive(this);
		addStyleName(ValoTheme.UI_WITH_MENU);

		Component contentComponent = createComponent();
		setContent(contentComponent);

		// Some views need to be aware of browser resize events so a
		// BrowserResizeEvent gets fired to the event bus on every occasion.
		Page.getCurrent().addBrowserWindowResizeListener(new BrowserWindowResizeListener() {
			@Override
			public void browserWindowResized(final BrowserWindowResizeEvent event) {
				DashboardEventBus.sessionInstance().post(new BrowserResizeEvent(event.getWidth(), event.getHeight()));
			}
		});

		addDetachListener(event -> {
			DashboardEventBus.sessionInstance().unregister(AbstractDashboardUI.this);
		});

		localizeRecursively(this);

		if (UI.getCurrent().getNavigator() != null)
			UI.getCurrent().getNavigator().navigateTo(dashboardSetup().dashboardViewName());

	}

	protected Component createComponent() {
		return new MainComponent(dashboardSetup()).build();
	}

	protected abstract AbstractDashboardSetup dashboardSetup();

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
