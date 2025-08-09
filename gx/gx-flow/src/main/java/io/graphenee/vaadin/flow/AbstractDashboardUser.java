package io.graphenee.vaadin.flow;

import io.graphenee.common.GxAuthenticatedUser;

/**
 * An abstract implementation of a dashboard user.
 *
 * @param <T> The user type.
 */
public abstract class AbstractDashboardUser<T> implements GxAuthenticatedUser {

	private T user;

	/**
	 * Creates a new instance of this user.
	 * @param user The user.
	 */
	public AbstractDashboardUser(T user) {
		this.user = user;
	}

	/**
	 * Gets the user.
	 * @return The user.
	 */
	public T getUser() {
		return user;
	}

}
