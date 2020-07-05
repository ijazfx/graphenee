package io.graphenee.core.model;

public interface GxMeetingUser {

	boolean isOnline();

	String getUserId();

	String getFullName();

	default String getAddtionalInfo() {
		return null;
	}

}
