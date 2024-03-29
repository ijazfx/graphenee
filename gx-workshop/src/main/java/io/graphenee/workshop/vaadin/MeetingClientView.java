/*******************************************************************************
 * Copyright (c) 2016, 2018 Farrukh Ijaz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package io.graphenee.workshop.vaadin;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.navigator.MView;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;

import io.graphenee.core.api.GxMeetingService;
import io.graphenee.core.model.GxAuthenticatedMeetingUserWrapper;
import io.graphenee.core.model.GxMeeting;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.vaadin.AbstractDashboardPanel;
import io.graphenee.vaadin.meeting.GxMeetingClient;
import io.graphenee.vaadin.util.DashboardUtils;

@SuppressWarnings("serial")
@SpringView(name = MeetingClientView.VIEW_NAME)
public class MeetingClientView extends AbstractDashboardPanel implements MView {

	public static final String VIEW_NAME = "meeting-client";

	@Autowired
	GxMeetingService meetingService;

	@Autowired
	GxDataService dataService;

	private GxMeetingClient meetingClientCompnent;

	@Override
	public void enter(ViewChangeEvent event) {
		GxAuthenticatedMeetingUserWrapper meetingUser = new GxAuthenticatedMeetingUserWrapper(DashboardUtils.getLoggedInUser());
		GxMeeting meeting = meetingService.joinMeeting(meetingUser, "1");
		String stunUrl = "stun:127.0.0.1:54321";
		String turnUrl = "turn:127.0.0.1:3478?transport=tcp";
		String turnUsername = "guest";
		String turnCredentials = "guest";
		meetingClientCompnent.initializeWithMeetingUserAndMeeting(meetingUser, meeting, stunUrl, turnUrl, turnUsername, turnCredentials);
	}

	@Override
	public boolean beforeViewChange(ViewChangeEvent event) {
		return true;
	}

	@Override
	public void afterViewChange(ViewChangeEvent event) {
	}

	@Override
	protected String panelTitle() {
		return "Meeting Client";
	}

	@Override
	protected void postInitialize() {
		meetingClientCompnent = new GxMeetingClient();
		addComponent(meetingClientCompnent);
	}

	@Override
	protected boolean shouldShowHeader() {
		return true;
	}

}
