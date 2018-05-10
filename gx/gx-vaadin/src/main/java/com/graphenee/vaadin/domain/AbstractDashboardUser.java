package com.graphenee.vaadin.domain;

public abstract class AbstractDashboardUser<T> implements DashboardUser {

	private T user;

	public AbstractDashboardUser(T user) {
		this.user = user;
	}

	public T getUser() {
		return user;
	}

}
