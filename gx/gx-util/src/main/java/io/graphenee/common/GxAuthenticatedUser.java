package io.graphenee.common;

import java.security.Principal;
import java.util.Map;

/**
 * An interface that represents an authenticated user.
 */
public interface GxAuthenticatedUser extends Principal {

	/**
	 * Gets the user id.
	 * 
	 * @return The user id.
	 */
	Integer getOid();

	/**
	 * Sets the profile photo.
	 * 
	 * @return The profile photo.
	 */
	byte[] getProfilePhoto();

	/**
	 * Gets the first name.
	 * 
	 * @return The first name.
	 */
	String getFirstName();

	/**
	 * Gets the last name.
	 * 
	 * @return The last name.
	 */
	String getLastName();

	/**
	 * Gets the username.
	 * 
	 * @return The username.
	 */
	String getUsername();

	/**
	 * Gets the email.
	 * 
	 * @return The email.
	 */
	String getEmail();

	/**
	 * Gets the mobile number.
	 * 
	 * @return The mobile number.
	 */
	String getMobileNumber();

	/**
	 * Gets the first name and last name.
	 * 
	 * @return The first name and last name.
	 */
	default String getFirstNameLastName() {
		if (getFirstName() != null || getLastName() != null) {
			return String.join(" ", getFirstName(), getLastName());
		}
		if (getUsername() != null) {
			return getUsername();
		}
		return "Anonymous";
	}

	/**
	 * Gets the last name and first name.
	 * 
	 * @return The last name and first name.
	 */
	default String getLastNameFirstName() {
		if (getLastName() != null || getFirstName() != null) {
			return String.join(", ", getLastName(), getFirstName());
		}
		if (getUsername() != null) {
			return getUsername();
		}
		return "Anonymous";
	}

	/**
	 * Checks if the user can do an action on a resource.
	 * 
	 * @param resource The resource.
	 * @param action   The action.
	 * @return True if the user can do the action, false otherwise.
	 */
	default boolean canDoAction(String resource, String action) {
		return canDoAction(resource, action, null, false);
	}

	/**
	 * Checks if the user can do an action on a resource.
	 * 
	 * @param resource     The resource.
	 * @param action       The action.
	 * @param forceRefresh Whether to force a refresh.
	 * @return True if the user can do the action, false otherwise.
	 */
	default boolean canDoAction(String resource, String action, boolean forceRefresh) {
		return canDoAction(resource, action, null, forceRefresh);
	}

	/**
	 * Checks if the user can do an action on a resource.
	 * 
	 * @param resource The resource.
	 * @param action   The action.
	 * @return True if the user can do the action, false otherwise.
	 */
	boolean canDoAction(String resource, String action, Map<String, Object> keyValueMap);

	/**
	 * Checks if the user can do an action on a resource.
	 * 
	 * @param resource     The resource.
	 * @param action       The action.
	 * @param forceRefresh Whether to force a refresh.
	 * @return True if the user can do the action, false otherwise.
	 */
	boolean canDoAction(String resource, String action, Map<String, Object> keyValueMap, boolean forceRefresh);

	@Override
	default String getName() {
		return getUsername();
	}

}
