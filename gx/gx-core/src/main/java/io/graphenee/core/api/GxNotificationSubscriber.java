package io.graphenee.core.api;

import io.graphenee.core.model.GxNotificationEvent;

public interface GxNotificationSubscriber {

	void onNotification(GxNotificationEvent event);

}
