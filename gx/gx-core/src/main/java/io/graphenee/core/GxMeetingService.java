package io.graphenee.core;

import java.util.Collection;

import io.graphenee.core.model.GxMeeting;
import io.graphenee.core.model.GxMeetingUser;

/**
 * An interface for meeting services.
 */
public interface GxMeetingService {

	/**
	 * Creates a new meeting.
	 * @param host The host of the meeting.
	 * @param meetingId The ID of the meeting.
	 * @param invitees The invitees of the meeting.
	 * @return The new meeting.
	 */
	GxMeeting createMeeting(GxMeetingUser host, String meetingId, Collection<GxMeetingUser> invitees);

	/**
	 * Ends a meeting.
	 * @param host The host of the meeting.
	 * @param meetingId The ID of the meeting.
	 */
	void endMeeting(GxMeetingUser host, String meetingId);

	/**
	 * Joins a meeting.
	 * @param user The user joining the meeting.
	 * @param meetingId The ID of the meeting.
	 * @return The meeting.
	 */
	GxMeeting joinMeeting(GxMeetingUser user, String meetingId);

}
