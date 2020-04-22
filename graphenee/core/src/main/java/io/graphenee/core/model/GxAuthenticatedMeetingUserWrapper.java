package io.graphenee.core.model;

public class GxAuthenticatedMeetingUserWrapper implements GxMeetingUser {

	private GxAuthenticatedUser wrappedUser;
	private boolean online = false;

	public GxAuthenticatedMeetingUserWrapper(GxAuthenticatedUser user) {
		this.wrappedUser = user;
	}

	@Override
	public String getUserId() {
		return wrappedUser.getUsername();
	}

	@Override
	public String getFullName() {
		return wrappedUser.getFirstNameLastName();
	}

	@Override
	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

}
