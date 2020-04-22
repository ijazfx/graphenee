package io.graphenee.vaadin.meeting;

import java.net.URI;

import org.vaadin.viritin.button.MButton;

import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.core.model.GxMeeting;
import io.graphenee.core.model.GxMeetingUser;
import io.graphenee.vaadin.ui.GxNotification;

@SuppressWarnings("serial")
@JavaScript({ "meeting-client.js" })
@StyleSheet({ "meeting.css" })
public class GxMeetingClient extends VerticalLayout {

	private GxMeetingUser user;
	private GxMeeting meeting;
	private MButton joinButton;
	private MButton leaveButton;
	private MButton cameraButton;
	private MButton screenButton;

	public GxMeetingClient() {
		setWidth("100%");
		setHeight("100%");
		setMargin(false);
		setSpacing(false);
		buildComponent();
	}

	private void buildComponent() {
		addComponent(getToolbar());
		Component roomComponent = getRoomComponent();
		addComponent(roomComponent);
		setExpandRatio(roomComponent, 1);
	}

	private Component getToolbar() {
		CssLayout toolbar = new CssLayout();
		toolbar.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

		joinButton = new MButton("Join").withStyleName(ValoTheme.BUTTON_PRIMARY).withListener(e -> {
			if (meeting == null || !meeting.isStarted()) {
				GxNotification.tray("Notification", "Meeting is not yet started or has ended already.").show(Page.getCurrent());
				return;
			}
			meeting.join(user);
			join(user);
			joinButton.setEnabled(false);
			leaveButton.setEnabled(true);
		});
		leaveButton = new MButton("Leave").withStyleName(ValoTheme.BUTTON_DANGER).withListener(e -> {
			meeting.leave(user);
			leave(user);
			leaveButton.setEnabled(false);
			joinButton.setEnabled(true);
		});
		cameraButton = new MButton().withIcon(FontAwesome.VIDEO_CAMERA).withStyleName(ValoTheme.BUTTON_ICON_ONLY);
		screenButton = new MButton().withIcon(FontAwesome.DESKTOP).withStyleName(ValoTheme.BUTTON_ICON_ONLY);

		cameraButton.addClickListener(e -> {
			String statement = String.format("startCamera()");
			com.vaadin.ui.JavaScript.getCurrent().execute(statement);
		});

		screenButton.addClickListener(e -> {
			String statement = String.format("startScreen()");
			com.vaadin.ui.JavaScript.getCurrent().execute(statement);
		});

		toolbar.addComponents(joinButton, leaveButton, cameraButton, screenButton);

		return toolbar;
	}

	private Component getRoomComponent() {
		CssLayout roomLayout = new CssLayout();
		roomLayout.setSizeFull();

		Label hostVideo = new Label();
		hostVideo.setSizeFull();
		hostVideo.setContentMode(ContentMode.HTML);
		String hostVideoHtml = "<video id=\"remoteVideo\" width=\"100%\" height=\"100%\" autoplay></video>";
		hostVideo.setValue(hostVideoHtml);
		roomLayout.addComponent(hostVideo);

		Label localVideo = new Label();
		localVideo.setWidthUndefined();
		localVideo.setContentMode(ContentMode.HTML);
		String localVideoHtml = "<video id=\"localVideo\" width=\"200px\" height=\"150px\" autoplay muted></video>";
		localVideo.setValue(localVideoHtml);
		roomLayout.addComponent(localVideo);

		return roomLayout;
	}

	public void initializeWithMeetingUserAndMeeting(GxMeetingUser user, GxMeeting meeting) {
		this.user = user;
		this.meeting = meeting;
		if (meeting != null && user.getUserId().equals(meeting.getHost().getUserId())) {
			joinButton.setVisible(false);
			leaveButton.setVisible(false);
		} else {
			joinButton.setVisible(true);
			leaveButton.setVisible(true);
		}

		boolean online = meeting != null && meeting.isOnline(user);

		joinButton.setEnabled(!online);
		leaveButton.setEnabled(online);
		initializeWebSocket(user);
	}

	private void initializeWebSocket(GxMeetingUser user) {
		URI pageUri = Page.getCurrent().getLocation();
		StringBuilder sb = new StringBuilder();
		if (pageUri.getScheme().startsWith("https"))
			sb.append("wss://").append(pageUri.getHost());
		else
			sb.append("ws://").append(pageUri.getHost());
		if (pageUri.getPort() != -1 && pageUri.getPort() != 443) {
			sb.append(":").append(pageUri.getPort());
		}
		sb.append("/socket");
		String statement = String.format("initializeWebSocket('%s?authToken=%s', '%s')", sb.toString(), user.getUserId(), user.getUserId());
		com.vaadin.ui.JavaScript.getCurrent().execute(statement);
	}

	private void join(GxMeetingUser user) {
		String statement = String.format("joinMeeting()");
		com.vaadin.ui.JavaScript.getCurrent().execute(statement);
	}

	private void leave(GxMeetingUser user) {
		String statement = String.format("leaveMeeting()");
		com.vaadin.ui.JavaScript.getCurrent().execute(statement);
	}

}
