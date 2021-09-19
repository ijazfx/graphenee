package io.graphenee.vaadin.flow.component;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;

public class GxNotification {

	private static final int DEFAULT_DURATION = 5000;
	private static final Position DEFAULT_POSITION = Position.BOTTOM_CENTER;

	private GxNotification() {
	}

	public static void error(String text, int duration, Position position) {
		show(text, duration, position, NotificationVariant.LUMO_ERROR);
	}

	public static void error(String text, int duration) {
		error(text, duration, DEFAULT_POSITION);
	}

	public static void error(String text) {
		error(text, DEFAULT_DURATION, DEFAULT_POSITION);
	}

	public static void primary(String text, int duration, Position position) {
		show(text, duration, position, NotificationVariant.LUMO_PRIMARY);
	}

	public static void primary(String text, int duration) {
		primary(text, duration, DEFAULT_POSITION);
	}

	public static void primary(String text) {
		primary(text, DEFAULT_DURATION, DEFAULT_POSITION);
	}

	public static void success(String text, int duration, Position position) {
		show(text, duration, position, NotificationVariant.LUMO_SUCCESS);
	}

	public static void success(String text, int duration) {
		success(text, duration, DEFAULT_POSITION);
	}

	public static void success(String text) {
		success(text, DEFAULT_DURATION, DEFAULT_POSITION);
	}

	private static void show(String text, int duration, Position position, NotificationVariant notificationVariant) {
		Notification notification = Notification.show(text, duration, position);
		notification.addThemeVariants(notificationVariant);
	}
}
