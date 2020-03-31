package io.graphenee.core.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class GxMeeting {

	private String meetingId;
	private GxMeetingUser host;
	private Set<GxMeetingUser> attendees = new HashSet<>();
	private boolean started = false;

	public GxMeeting(GxMeetingUser host, String meetingId) {
		this.meetingId = meetingId;
		this.host = host;
	}

	public String getMeetingId() {
		return meetingId;
	}

	public GxMeetingUser getHost() {
		return host;
	}

	public abstract Collection<GxMeetingUser> getInvitees();

	public boolean start(GxMeetingUser host) {
		if (getHost().getUserId().equals(host.getUserId()) && !started) {
			started = true;
			return true;
		}
		return false;
	}

	public boolean end(GxMeetingUser host) {
		if (getHost().getUserId().equals(host.getUserId()) && started) {
			started = false;
			return true;
		}
		return false;
	}

	public boolean join(GxMeetingUser attendee) {
		for (GxMeetingUser a : getInvitees()) {
			if (a.getUserId().equals(attendee.getUserId())) {
				return false;
			}
		}
		attendees.add(attendee);
		return true;
	}

	public boolean leave(GxMeetingUser attendee) {
		return attendees.remove(attendee);
	}

	public boolean isOnline(GxMeetingUser user) {
		return attendees.contains(user);
	}

	public boolean isStarted() {
		return started;
	}

}
