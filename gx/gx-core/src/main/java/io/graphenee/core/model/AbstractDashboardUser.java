package io.graphenee.core.model;

public abstract class AbstractDashboardUser<T> implements GxAuthenticatedUser {

	private T user;

	public AbstractDashboardUser(T user) {
		this.user = user;
	}

	public T getUser() {
		return user;
	}

}
