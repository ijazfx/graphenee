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

import io.graphenee.core.api.GxNotificationSubscriber;
import io.graphenee.core.model.GxAuthenticatedUser;

public abstract class AbstractDashboardUser<T> implements GxAuthenticatedUser, GxNotificationSubscriber {

	private T user;
	private AtomicInteger notificationCount = new AtomicInteger();

	public AbstractDashboardUser(T user) {
		this.user = user;
	}

	public T getUser() {
		return user;
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
