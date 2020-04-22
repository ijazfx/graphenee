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

import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.eventbus.Subscribe;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.UI;

import io.graphenee.core.enums.GenderEnum;
import io.graphenee.core.model.GxNotificationEvent;
import io.graphenee.core.model.bean.GxUserAccountBean;
import io.graphenee.vaadin.BadgeWrapper;
import io.graphenee.vaadin.event.DashboardEvent;
import io.graphenee.vaadin.event.DashboardEventBus;
import io.graphenee.vaadin.ui.GxNotification;
import io.graphenee.vaadin.util.DashboardUtils;

public class GxDashboardUser extends AbstractDashboardUser<GxUserAccountBean> {

	private AtomicInteger notificationCount = new AtomicInteger();

	public GxDashboardUser(GxUserAccountBean user) {
		super(user);
	}

	@Override
	public String getFirstName() {
		return getUser().getFirstName();
	}

	@Override
	public void setFirstName(String firstName) {
		getUser().setFirstName(firstName);
	}

	@Override
	public String getLastName() {
		return getUser().getLastName();
	}

	@Override
	public void setLastName(String lastName) {
		getUser().setLastName(lastName);
	}

	@Override
	public String getUsername() {
		return getUser().getUsername();
	}

	@Override
	public void setUsername(String username) {
		getUser().setUsername(username);
	}

	@Override
	public String getPassword() {
		return getUser().getPassword();
	}

	@Override
	public void setPassword(String password) {
		getUser().setPassword(password);
	}

	@Override
	public boolean isPasswordChangeRequired() {
		return getUser().getIsPasswordChangeRequired();
	}

	@Override
	public GenderEnum getGender() {
		return getUser().getGender();
	}

	@Override
	public void setGender(GenderEnum gender) {
		getUser().setGender(gender);
	}

	@Override
	public boolean canDoAction(String resource, String action) {
		return getUser().canDoAction(resource, action);
	}

	@Override
	public boolean canDoAction(String resource, String action, boolean forceRefresh) {
		return getUser().canDoAction(resource, action, forceRefresh);
	}

	@Override
	public String getEmail() {
		return getUser().getEmail();
	}

	@Override
	public void setEmail(String email) {
		getUser().setEmail(email);
	}

	@Override
	public String getMobileNumber() {
		return null;
	}

	@Override
	public void setMobileNumber(String mobileNumber) {
	}

	public byte[] getProfilePhoto() {
		return getUser().getProfileImage();
	}

	@Subscribe
	@Override
	public void onNotification(GxNotificationEvent event) {
		UI ui = DashboardUtils.getCurrentUI(this);
		if (ui != null && ui.isAttached()) {
			if (event != null && event.test(this)) {
				notificationCount.incrementAndGet();
				ui.access(() -> {
					GxNotification notification = GxNotification.tray(event.getTitle(), event.getDescription());
					notification.setDelayMsec(10000);
					notification.setIcon(FontAwesome.BELL);
					notification.show(ui.getPage());
					ui.push();
					DashboardEventBus.sessionInstance(ui.getSession())
							.post(new DashboardEvent.BadgeUpdateEvent(this, BadgeWrapper.NOTIFICATIONS_BADGE_ID, getUnreadNotificationCount() + ""));
				});
			}
		}
	}

	@Override
	public int getUnreadNotificationCount() {
		return notificationCount.get();
	}

	@Override
	public void setUnreadNotificationCount(int count) {
		notificationCount.set(count);
	}

}
