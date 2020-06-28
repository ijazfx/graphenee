package io.graphenee.core.model;

import java.util.Date;

public interface GxNotificationEvent {

	boolean test(GxAuthenticatedUser user);

	String getTitle();

	String getDescription();

	Date notificationDate();

}
