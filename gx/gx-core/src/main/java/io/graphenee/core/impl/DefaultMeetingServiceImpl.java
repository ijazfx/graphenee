package io.graphenee.core.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import io.graphenee.core.api.GxMeetingService;
import io.graphenee.core.model.GxMeeting;
import io.graphenee.core.model.GxMeetingUser;

public class DefaultMeetingServiceImpl implements GxMeetingService {

	private Map<String, GxMeeting> meetingMap = new HashMap<>();

	@Override
	public GxMeeting createMeeting(GxMeetingUser host, String meetingId, Collection<GxMeetingUser> invitees) {
		GxMeeting meeting = meetingMap.get(meetingId);
		if (meeting == null) {
			meeting = new GxMeeting(host, meetingId) {

				@Override
				public Collection<GxMeetingUser> getInvitees() {
					return invitees;
				}

			};
			meetingMap.put(meeting.getMeetingId(), meeting);
		}
		return meeting;
	}

	@Override
	public void endMeeting(GxMeetingUser host, String meetingId) {
		GxMeeting meeting = meetingMap.get(meetingId);
		if (meeting != null && meeting.getHost().equals(host)) {
			meetingMap.remove(meetingId);
		}
	}

	@Override
	public GxMeeting joinMeeting(GxMeetingUser user, String meetingId) {
		GxMeeting meeting = meetingMap.get(meetingId);
		if (meeting != null) {
			meeting.join(user);
		}
		return meeting;
	}

}
