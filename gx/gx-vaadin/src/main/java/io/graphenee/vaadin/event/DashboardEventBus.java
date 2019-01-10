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

import java.util.HashSet;
import java.util.Set;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import com.vaadin.server.VaadinSession;

/**
 * A simple wrapper for Guava event bus. Defines static convenience methods for
 * relevant actions.
 */
public class DashboardEventBus implements SubscriberExceptionHandler {

	private Set<Object> registeredObjects = new HashSet<>();

	private final EventBus eventBus = new EventBus(this);

	public void post(final Object event) {
		eventBus.post(event);
	}

	/**
	 * The object must implement hashCode() and equals() to work properly.
	 *
	 * @param object - any object interested to receive event bus notifications
	 */
	public void register(final Object object) {
		if (!registeredObjects.contains(object)) {
			registeredObjects.add(object);
			eventBus.register(object);
		}
	}

	/**
	 * The object must implement hashCode() and equals() to work properly.
	 *
	 * @param object - an object which was previously registered to receive
	 * notifications
	 */
	public void unregister(final Object object) {
		if (registeredObjects.contains(object)) {
			eventBus.unregister(object);
			registeredObjects.remove(object);
		}
	}

	@Override
	public final void handleException(final Throwable exception, final SubscriberExceptionContext context) {
		exception.printStackTrace();
	}

	public static DashboardEventBus sessionInstance() {
		DashboardEventBus dashboardEventBus = VaadinSession.getCurrent().getAttribute(DashboardEventBus.class);
		if (dashboardEventBus == null) {
			synchronized (DashboardEventBus.class) {
				if (dashboardEventBus == null) {
					dashboardEventBus = new DashboardEventBus();
					VaadinSession.getCurrent().setAttribute(DashboardEventBus.class, dashboardEventBus);
				}
			}
		}
		return dashboardEventBus;
	}

	public static DashboardEventBus sessionInstance(VaadinSession session) {
		DashboardEventBus dashboardEventBus = session.getAttribute(DashboardEventBus.class);
		if (dashboardEventBus == null) {
			synchronized (DashboardEventBus.class) {
				if (dashboardEventBus == null) {
					dashboardEventBus = new DashboardEventBus();
					session.setAttribute(DashboardEventBus.class, dashboardEventBus);
				}
			}
		}
		return dashboardEventBus;
	}

}
