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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.core.model.GxAuthenticatedUser;
import io.graphenee.i18n.api.LocalizerService;
import io.graphenee.vaadin.util.DashboardUtils;
import io.graphenee.vaadin.util.VaadinUtils;
import io.graphenee.vaadin.view.MainComponent;

@SuppressWarnings("serial")
public abstract class AbstractDashboardUI extends UI {

	@Override
	protected void init(final VaadinRequest request) {

		Map<String, String[]> parameterMap = request.getParameterMap();
		Map<String, String[]> queryMap = new HashMap<>();
		for (String key : parameterMap.keySet()) {
			if (!key.startsWith("v-")) {
				queryMap.put(key, parameterMap.get(key));
			}
		}

		VaadinSession.getCurrent().setAttribute("gx-QueryMap", queryMap);

		if (this.getClass().isAnnotationPresent(GxSecuredUI.class)) {
			GxAuthenticatedUser user = VaadinSession.getCurrent().getAttribute(GxAuthenticatedUser.class);
			if (user == null) {
				Page.getCurrent().setLocation("/login");
				return;
			}
			DashboardUtils.setCurrentUI(user, AbstractDashboardUI.this);
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

		Responsive.makeResponsive(this);
		addStyleName(ValoTheme.UI_WITH_MENU);

		Component contentComponent = createComponent();
		setContent(contentComponent);

		localizeRecursively(this);

		if (UI.getCurrent().getNavigator() != null) {

			GxAuthenticatedUser user = VaadinSession.getCurrent().getAttribute(GxAuthenticatedUser.class);

			String currentState = UI.getCurrent().getNavigator().getState();
			String navigableState = findNavigableState(user, currentState);

			try {
				if (currentState.startsWith(navigableState))
					UI.getCurrent().getNavigator().navigateTo(currentState);
				else
					UI.getCurrent().getNavigator().navigateTo(navigableState);
			} catch (Exception ex) {
				String dashboardViewName = dashboardSetup().dashboardViewName();
				if (dashboardViewName == null)
					dashboardViewName = "";
				UI.getCurrent().getNavigator().navigateTo(dashboardViewName);
			}

		}

		Page.getCurrent().setTitle(dashboardSetup().applicationTitle());

	}

	private String findNavigableState(GxAuthenticatedUser user, String currentState) {
		if (user != null && user.canDoAction(currentState, "view"))
			return currentState;
		if (currentState.lastIndexOf("/") >= 0) {
			return findNavigableState(user, currentState.substring(0, currentState.lastIndexOf("/")));
		}
		return dashboardSetup().dashboardViewName();
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
