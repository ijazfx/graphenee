package io.graphenee.core.model;

import java.util.Date;

import io.graphenee.common.GxAuthenticatedUser;

public interface GxNotificationEvent {

	boolean test(GxAuthenticatedUser user);

	String getTitle();

	String getDescription();

	Date notificationDate();

}
