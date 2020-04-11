package io.graphenee.core.model;

public interface GxNotificationEvent {

	boolean test(GxAuthenticatedUser authenticatedUser);

	String getTitle();

	String getDescription();

}
