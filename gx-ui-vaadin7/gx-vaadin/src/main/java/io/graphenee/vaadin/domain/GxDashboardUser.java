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
package io.graphenee.vaadin.domain;

import com.google.common.eventbus.Subscribe;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.UI;

import io.graphenee.core.api.GxNotificationSubscriber;
import io.graphenee.core.enums.GenderEnum;
import io.graphenee.core.model.GxAuthenticatedUser;
import io.graphenee.core.model.GxNotificationEvent;
import io.graphenee.core.model.bean.GxUserAccountBean;
import io.graphenee.vaadin.BadgeWrapper;
import io.graphenee.vaadin.event.DashboardEvent;
import io.graphenee.vaadin.event.DashboardEventBus;
import io.graphenee.vaadin.ui.GxNotification;
import io.graphenee.vaadin.util.DashboardUtils;

public class GxDashboardUser implements GxAuthenticatedUser, GxNotificationSubscriber {

	private GxUserAccountBean user;

	public GxDashboardUser(GxUserAccountBean user) {
		this.user = user;
	}

	public GxUserAccountBean getUser() {
		return user;
	}

	@Override
	public String getFirstName() {
		return user.getFirstName();
	}

	@Override
	public void setFirstName(String firstName) {
		user.setFirstName(firstName);
	}

	@Override
	public String getLastName() {
		return user.getLastName();
	}

	@Override
	public void setLastName(String lastName) {
		user.setLastName(lastName);
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public void setUsername(String username) {
		user.setUsername(username);
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public void setPassword(String password) {
		user.setPassword(password);
	}

	@Override
	public boolean isPasswordChangeRequired() {
		return user.getIsPasswordChangeRequired();
	}

	@Override
	public GenderEnum getGender() {
		return user.getGender();
	}

	@Override
	public void setGender(GenderEnum gender) {
		user.setGender(gender);
	}

	@Override
	public boolean canDoAction(String resource, String action) {
		return user.canDoAction(resource, action);
	}

	@Override
	public boolean canDoAction(String resource, String action, boolean forceRefresh) {
		return user.canDoAction(resource, action, forceRefresh);
	}

	@Override
	public String getEmail() {
		return user.getEmail();
	}

	@Override
	public void setEmail(String email) {
		user.setEmail(email);
	}

	@Override
	public String getMobileNumber() {
		return null;
	}

	@Override
	public void setMobileNumber(String mobileNumber) {
	}

	public byte[] getProfilePhoto() {
		return user.getProfileImage();
	}

	@Subscribe
	@Override
	public void onNotification(GxNotificationEvent event) {
		if (event != null && event.test(this)) {
			UI ui = DashboardUtils.getCurrentUI(this);
			ui.access(() -> {
				GxNotification notification = GxNotification.tray(event.getTitle(), event.getDescription());
				notification.setDelayMsec(10000);
				notification.setIcon(FontAwesome.BELL);
				notification.show(ui.getPage());
				ui.push();
				DashboardEventBus.sessionInstance(ui.getSession()).post(new DashboardEvent.BadgeUpdateEvent(BadgeWrapper.NOTIFICATIONS_BADGE_ID, "+1"));
			});
		}
	}

}
