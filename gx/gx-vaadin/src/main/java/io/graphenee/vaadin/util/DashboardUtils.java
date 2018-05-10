/*******************************************************************************
 * Copyright (c) 2016, 2017, Graphenee
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

import com.vaadin.server.VaadinSession;

import io.graphenee.vaadin.domain.DashboardUser;

public class DashboardUtils {

	@SuppressWarnings("unchecked")
	public static <T extends DashboardUser> T getLoggedInUser() {
		return (T) VaadinSession.getCurrent().getAttribute(DashboardUser.class.getName());
	}

	@SuppressWarnings("unchecked")
	public static <T extends DashboardUser> T getLoggedInUser(VaadinSession vaadinSession) {
		return (T) vaadinSession.getAttribute(DashboardUser.class.getName());
	}

	public static String getLoggedInUsername() {
		DashboardUser loggedInUser = DashboardUtils.getLoggedInUser();
		final String targetUser;
		if (loggedInUser != null) {
			targetUser = loggedInUser.getUsername();
		} else {
			targetUser = "system";
		}
		return targetUser;
	}

}
