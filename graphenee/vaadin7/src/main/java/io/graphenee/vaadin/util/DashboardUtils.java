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
package io.graphenee.vaadin.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

import io.graphenee.core.model.GxAuthenticatedUser;

public class DashboardUtils {

	private static final Map<GxAuthenticatedUser, UI> UI_MAP = new ConcurrentHashMap<>(new HashMap<>());

	public static void setCurrentUI(GxAuthenticatedUser user, UI ui) {
		UI_MAP.put(user, ui);
	}

	public static UI getCurrentUI(GxAuthenticatedUser user) {
		UI ui = UI_MAP.get(user);
		if (ui != null && ui.isAttached())
			return ui;
		UI_MAP.remove(user);
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T extends GxAuthenticatedUser> T getLoggedInUser() {
		return (T) VaadinSession.getCurrent().getAttribute(GxAuthenticatedUser.class.getName());
	}

	@SuppressWarnings("unchecked")
	public static <T extends GxAuthenticatedUser> T getLoggedInUser(VaadinSession vaadinSession) {
		return (T) vaadinSession.getAttribute(GxAuthenticatedUser.class.getName());
	}

	public static String getLoggedInUsername() {
		GxAuthenticatedUser loggedInUser = DashboardUtils.getLoggedInUser();
		final String targetUser;
		if (loggedInUser != null) {
			targetUser = loggedInUser.getUsername();
		} else {
			targetUser = "system";
		}
		return targetUser;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String[]> getQueryMap() {
		Map<String, String[]> map = (Map<String, String[]>) VaadinSession.getCurrent().getAttribute("gx-QueryMap");
		if (map == null) {
			map = new HashMap<>();
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String[]> getQueryMap(VaadinSession vaadinSession) {
		Map<String, String[]> map = (Map<String, String[]>) vaadinSession.getAttribute("gx-QueryMap");
		if (map == null) {
			map = new HashMap<>();
		}
		return map;
	}

}
