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
package io.graphenee.vaadin.event;

import java.util.Map;

import io.graphenee.core.model.GxAuthenticatedUser;
import io.graphenee.vaadin.AbstractDashboardView.Dashlet;

/*
 * Event bus events used in Dashboard are listed here as inner classes.
 */
public abstract class DashboardEvent {

	public static class DashletMaximized {

		private Dashlet dashlet;

		public DashletMaximized(Dashlet dashlet) {
			this.setDashlet(dashlet);

		}

		public Dashlet getDashlet() {
			return dashlet;
		}

		public void setDashlet(Dashlet dashlet) {
			this.dashlet = dashlet;
		}

	}

	public static class DashletMinimized {

		private Dashlet dashlet;

		public DashletMinimized(Dashlet dashlet) {
			this.setDashlet(dashlet);

		}

		public Dashlet getDashlet() {
			return dashlet;
		}

		public void setDashlet(Dashlet dashlet) {
			this.dashlet = dashlet;
		}

	}

	public static final class UserLoginRequestedEvent {
		private final String userName, password;
		private Map<String, Object> additionalData;

		public UserLoginRequestedEvent(final String userName, final String password) {
			this.userName = userName;
			this.password = password;
		}

		public String getUserName() {
			return userName;
		}

		public String getPassword() {
			return password;
		}

		public Map<String, Object> getAdditionalData() {
			return additionalData;
		}

		public void setAdditionalData(Map<String, Object> additionalData) {
			this.additionalData = additionalData;
		}

	}

	public static final class UserChangePasswordRequestedEvent {
		private final String userName, oldPassword, newPassword;
		private Map<String, Object> additionalData;

		public UserChangePasswordRequestedEvent(final String userName, final String oldPassword, final String newPassword) {
			this.userName = userName;
			this.oldPassword = oldPassword;
			this.newPassword = newPassword;
		}

		public String getUserName() {
			return userName;
		}

		public String getOldPassword() {
			return oldPassword;
		}

		public String getNewPassword() {
			return newPassword;
		}

		public Map<String, Object> getAdditionalData() {
			return additionalData;
		}

		public void setAdditionalData(Map<String, Object> additionalData) {
			this.additionalData = additionalData;
		}

	}

	public static class UserLoggedOutEvent {

	}

	public static class NotificationsCountUpdatedEvent {
		private final int count;

		public NotificationsCountUpdatedEvent(final int count) {
			this.count = count;
		}

		public int getCount() {
			return count;
		}
	}

	public static final class ReportsCountUpdatedEvent {
		private final int count;

		public ReportsCountUpdatedEvent(final int count) {
			this.count = count;
		}

		public int getCount() {
			return count;
		}

	}

	public static final class PostViewChangeEvent {
		private final String viewName;
		private final String parameters;

		public PostViewChangeEvent(final String viewName, final String parameters) {
			this.viewName = viewName;
			this.parameters = parameters;
		}

		public String getViewName() {
			return viewName;
		}

		public String getParameters() {
			return parameters;
		}

	}

	public static class CloseOpenWindowsEvent {
	}

	public static class ProfileUpdatedEvent {
	}

	public static class UserProfileRenderEvent {
	}

	public static class RequestRefreshNotificationsEvent {
	}

	public static final class BadgeUpdateEvent {

		private final String badgeId;
		private final String badgeValue;
		private GxAuthenticatedUser user;

		public BadgeUpdateEvent(final GxAuthenticatedUser user, final String badgeId, final String badgeValue) {
			this.user = user;
			this.badgeId = badgeId;
			this.badgeValue = badgeValue;
		}

		public String getBadgeId() {
			return badgeId;
		}

		public String getBadgeValue() {
			return badgeValue;
		}

		public GxAuthenticatedUser getUser() {
			return user;
		}

	}

}
