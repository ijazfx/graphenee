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

import io.graphenee.core.enums.GenderEnum;
import io.graphenee.core.model.GxNotificationEvent;

public final class MockUser extends AbstractDashboardUser<String> {

	String firstName;
	String lastName;
	String username;
	String password;
	GenderEnum gender;
	private AtomicInteger notificationCount = new AtomicInteger();

	public MockUser() {
		super("Mock User");
	}

	@Override
	public String getFirstName() {
		return firstName;
	}

	@Override
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Override
	public String getLastName() {
		return lastName;
	}

	@Override
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public GenderEnum getGender() {
		return this.gender;
	}

	@Override
	public void setGender(GenderEnum gender) {
		this.gender = gender;
	}

	@Override
	public byte[] getProfilePhoto() {
		return null;
	}

	@Override
	public String getEmail() {
		return null;
	}

	@Override
	public void setEmail(String email) {
	}

	@Override
	public String getMobileNumber() {
		return null;
	}

	@Override
	public void setMobileNumber(String mobileNumber) {
	}

	@Override
	public int getUnreadNotificationCount() {
		return notificationCount.get();
	}

	@Override
	public void setUnreadNotificationCount(int count) {
		notificationCount.set(count);
	}

	@Override
	public void onNotification(GxNotificationEvent event) {
		// TODO Auto-generated method stub

	}
}
