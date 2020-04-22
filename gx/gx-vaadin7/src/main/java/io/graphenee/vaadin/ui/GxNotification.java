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
package io.graphenee.vaadin.ui;

import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;

public class GxNotification extends Notification {

	private static final long serialVersionUID = 1L;

	public GxNotification(String caption, String description, Type type, boolean htmlContentAllowed) {
		super(caption, description, type, htmlContentAllowed);
	}

	public GxNotification(String caption, String description, Type type) {
		super(caption, description, type);
	}

	public GxNotification(String caption, String description) {
		super(caption, description);
	}

	public GxNotification(String caption, Type type) {
		super(caption, type);
	}

	public GxNotification(String caption) {
		super(caption);
	}

	public static GxNotification closable(String description, Type type) {
		GxNotification notification = new GxNotification(null, description);
		notification.setStyleName(ValoTheme.NOTIFICATION_CLOSABLE + " " + type.getStyle());
		notification.setPosition(Position.MIDDLE_CENTER);
		notification.setDelayMsec(DELAY_FOREVER);
		return notification;
	}

	public static GxNotification closable(String caption, String description, Type type) {
		GxNotification notification = new GxNotification(caption, description);
		notification.setStyleName(ValoTheme.NOTIFICATION_CLOSABLE + " " + type.getStyle());
		notification.setPosition(Position.MIDDLE_CENTER);
		notification.setDelayMsec(DELAY_FOREVER);
		return notification;
	}

	public static GxNotification closable(String caption, String description, Type type, Position position) {
		GxNotification notification = new GxNotification(caption, description);
		notification.setStyleName(ValoTheme.NOTIFICATION_CLOSABLE + " " + type.getStyle());
		notification.setPosition(position);
		notification.setDelayMsec(DELAY_FOREVER);
		return notification;
	}

	public static GxNotification tray(String description) {
		GxNotification notification = new GxNotification(null, description, Type.TRAY_NOTIFICATION);
		return notification;
	}

	public static GxNotification tray(String caption, String description) {
		GxNotification notification = new GxNotification(caption, description, Type.TRAY_NOTIFICATION);
		return notification;
	}

}
