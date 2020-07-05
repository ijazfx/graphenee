package io.graphenee.core.api;

import io.graphenee.core.model.GxNotificationEvent;

public interface GxNotificationService {

	void sendNotification(GxNotificationEvent notification);

}
