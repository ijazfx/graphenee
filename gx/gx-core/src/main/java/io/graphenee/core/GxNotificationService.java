package io.graphenee.core;

import io.graphenee.core.model.GxNotificationEvent;

public interface GxNotificationService {

	void sendNotification(GxNotificationEvent notification);

}
