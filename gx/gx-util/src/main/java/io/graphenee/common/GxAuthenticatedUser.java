package io.graphenee.common;

import java.util.Map;

import io.graphenee.util.enums.GenderEnum;

/**
 * An interface that represents an authenticated user.
 */
public interface GxAuthenticatedUser {

	/**
	 * Gets the profile photo.
	 * @return The profile photo.
	 */
	byte[] getProfilePhoto();

	/**
	 * Gets the first name.
	 * @return The first name.
	 */
	String getFirstName();

	/**
	 * Sets the first name.
	 * @param firstName The first name.
	 */
	void setFirstName(String firstName);

	/**
	 * Gets the last name.
	 * @return The last name.
	 */
	String getLastName();

	/**
	 * Sets the last name.
	 * @param lastName The last name.
	 */
	void setLastName(String lastName);

	/**
	 * Gets the username.
	 * @return The username.
	 */
	String getUsername();

	/**
	 * Sets the username.
	 * @param username The username.
	 */
	void setUsername(String username);

	/**
	 * Gets the password.
	 * @return The password.
	 */
	@Deprecated(forRemoval = true)
	String getPassword();

	/**
	 * Sets the password.
	 * @param password The password.
	 */
	@Deprecated(forRemoval = true)
	void setPassword(String password);

	/**
	 * Gets the gender.
	 * @return The gender.
	 */
	GenderEnum getGender();

	/**
	 * Sets the gender.
	 * @param gender The gender.
	 */
	void setGender(GenderEnum gender);

	/**
	 * Gets the email.
	 * @return The email.
	 */
	String getEmail();

	/**
	 * Sets the email.
	 * @param email The email.
	 */
	void setEmail(String email);

	/**
	 * Gets the mobile number.
	 * @return The mobile number.
	 */
	String getMobileNumber();

	/**
	 * Sets the mobile number.
	 * @param mobileNumber The mobile number.
	 */
	void setMobileNumber(String mobileNumber);

	/**
	 * Checks if the password needs to be changed.
	 * @return True if the password needs to be changed, false otherwise.
	 */
	default boolean isPasswordChangeRequired() {
		return false;
	}

	/**
	 * Gets the first name and last name.
	 * @return The first name and last name.
	 */
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

	/**
	 * Gets the last name and first name.
	 * @return The last name and first name.
	 */
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

	/**
	 * Checks if the user can do an action on a resource.
	 * @param resource The resource.
	 * @param action The action.
	 * @return True if the user can do the action, false otherwise.
	 */
	default boolean canDoAction(String resource, String action) {
		return canDoAction(resource, action, null, false);
	}

	/**
	 * Checks if the user can do an action on a resource.
	 * @param resource The resource.
	 * @param action The action.
	 * @param forceRefresh Whether to force a refresh.
	 * @return True if the user can do the action, false otherwise.
	 */
	default boolean canDoAction(String resource, String action, boolean forceRefresh) {
		return canDoAction(resource, action, null, forceRefresh);
	}

	/**
	 * Checks if the user can do an action on a resource.
	 * @param resource The resource.
	 * @param action The action.
	 * @return True if the user can do the action, false otherwise.
	 */
	boolean canDoAction(String resource, String action, Map<String, Object> keyValueMap);

	/**
	 * Checks if the user can do an action on a resource.
	 * @param resource The resource.
	 * @param action The action.
	 * @param forceRefresh Whether to force a refresh.
	 * @return True if the user can do the action, false otherwise.
	 */
	boolean canDoAction(String resource, String action, Map<String, Object> keyValueMap, boolean forceRefresh);

}
