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
