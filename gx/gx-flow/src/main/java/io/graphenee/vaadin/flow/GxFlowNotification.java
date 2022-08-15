package io.graphenee.vaadin.flow;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;

public class GxFlowNotification {

	public static Notification info(String description) {
		Notification notification = new Notification(description, 5000);
		notification.setPosition(Position.BOTTOM_END);
		return notification;
	}

	public static Notification alert(String description) {
		Notification notification = new Notification(description, 5000);
		notification.setPosition(Position.BOTTOM_END);
		notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
		return notification;
	}

}
