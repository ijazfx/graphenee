package io.graphenee.core.api;

import java.util.Collection;

import io.graphenee.core.model.GxMeeting;
import io.graphenee.core.model.GxMeetingUser;

public interface GxMeetingService {

	GxMeeting createMeeting(GxMeetingUser host, String meetingId, Collection<GxMeetingUser> invitees);

	void endMeeting(GxMeetingUser host, String meetingId);

	GxMeeting joinMeeting(GxMeetingUser user, String meetingId);

}
