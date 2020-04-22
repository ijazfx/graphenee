package io.graphenee.core.model;

import io.graphenee.core.enums.GenderEnum;

public interface GxAuthenticatedUser {

	int getUnreadNotificationCount();

	void setUnreadNotificationCount(int count);

	byte[] getProfilePhoto();

	String getFirstName();

	void setFirstName(String firstName);

	String getLastName();

	void setLastName(String lastName);

	String getUsername();

	void setUsername(String username);

	String getPassword();

	void setPassword(String password);

	GenderEnum getGender();

	void setGender(GenderEnum gender);

	String getEmail();

	void setEmail(String email);

	String getMobileNumber();

	void setMobileNumber(String mobileNumber);

	default boolean isPasswordChangeRequired() {
		return false;
	}

	default String getFirstNameLastName() {
		if (getFirstName() != null) {
			if (getLastName() != null) {
				return getFirstName() + " " + getLastName();
			}
			return getFirstName();
		}
		if (getLastName() != null) {
			return getLastName();
		}
		if (getUsername() != null) {
			return getUsername();
		}
		return "Anonymous";
	}

	default String getLastNameFirstName() {
		if (getLastName() != null) {
			if (getFirstName() != null) {
				return getLastName() + ", " + getFirstName();
			}
			return getLastName();
		}
		if (getFirstName() != null) {
			return getFirstName();
		}
		if (getUsername() != null) {
			return getUsername();
		}
		return "Anonymous";
	}

	default boolean canDoAction(String resource, String action) {
		return true;
	}

	default boolean canDoAction(String resource, String action, boolean forceRefresh) {
		return true;
	}

}
