package io.graphenee.core;

import io.graphenee.core.model.GxNotificationEvent;

public interface GxNotificationSubscriber {

	void onNotification(GxNotificationEvent event);

}
