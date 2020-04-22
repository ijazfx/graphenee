package io.graphenee.core.model.bean;

import io.graphenee.core.model.GxMeetingUser;

public class GxUserAccountMeetingUserWrapper implements GxMeetingUser {

	private GxUserAccountBean wrappedUser;
	private boolean online = false;

	public GxUserAccountMeetingUserWrapper(GxUserAccountBean user) {
		this.wrappedUser = user;
	}

	@Override
	public String getUserId() {
		return wrappedUser.getUsername();
	}

	@Override
	public String getFullName() {
		return wrappedUser.getFullName();
	}

	@Override
	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

}
