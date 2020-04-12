package io.graphenee.core.model;

public interface GxNotificationEvent {

	boolean test(GxAuthenticatedUser user);

	String getTitle();

	String getDescription();

}
