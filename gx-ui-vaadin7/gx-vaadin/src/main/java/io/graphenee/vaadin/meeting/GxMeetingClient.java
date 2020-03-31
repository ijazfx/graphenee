package io.graphenee.vaadin.meeting;

import java.net.URI;

import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MPanel;

import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.data.util.BeanItemContainer;
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
import io.graphenee.gx.theme.graphenee.GrapheneeTheme;
import io.graphenee.vaadin.CardCollectionPanel;
import io.graphenee.vaadin.ui.GxNotification;

@SuppressWarnings("serial")
@JavaScript({ "meeting.js" })
@StyleSheet({ "meeting.css" })
public class GxMeetingClient extends VerticalLayout {

	private BeanItemContainer<GxMeetingUser> meetingContainer;
	private CardCollectionPanel<GxMeetingUser> roomPanel;
	private GxMeetingUser user;
	private GxMeeting meeting;
	private Component roomComponent;
	private MButton startButton;
	private MButton endButton;
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
		roomComponent = getRoomComponent();
		addComponent(roomComponent);
		setExpandRatio(roomComponent, 1);
	}

	private Component getToolbar() {
		CssLayout toolbar = new CssLayout();
		toolbar.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		startButton = new MButton("Start").withStyleName(ValoTheme.BUTTON_PRIMARY).withListener(e -> {
			meeting.start(user);
			meetingContainer.removeAllItems();
			meetingContainer.addBean(user);
			for (GxMeetingUser u : meeting.getInvitees()) {
				if (!u.getUserId().equals(user.getUserId()))
					meetingContainer.addBean(u);
			}
			roomPanel.refresh();
			startButton.setEnabled(false);
			endButton.setEnabled(true);
		});

		endButton = new MButton("End").withStyleName(ValoTheme.BUTTON_DANGER).withListener(e -> {
			meeting.end(user);
			meetingContainer.removeAllItems();
			roomPanel.refresh();
			endButton.setEnabled(false);
			startButton.setEnabled(true);
		});

		joinButton = new MButton("Join").withStyleName(ValoTheme.BUTTON_PRIMARY).withListener(e -> {
			if (!meeting.isStarted()) {
				GxNotification.tray("Notification", "Meeting is not yet started or has ended already.").show(Page.getCurrent());
				return;
			}
			meeting.join(user);
			meetingContainer.removeAllItems();
			meetingContainer.addBean(user);
			for (GxMeetingUser u : meeting.getInvitees()) {
				if (!u.getUserId().equals(user.getUserId()))
					meetingContainer.addBean(u);
			}
			roomPanel.refresh();
			requestOffer(user);
			joinButton.setEnabled(false);
			leaveButton.setEnabled(true);
		});
		leaveButton = new MButton("Leave").withStyleName(ValoTheme.BUTTON_DANGER).withListener(e -> {
			meeting.leave(user);
			meetingContainer.removeAllItems();
			roomPanel.refresh();
			leaveButton.setEnabled(false);
			joinButton.setEnabled(true);
		});
		cameraButton = new MButton().withIcon(FontAwesome.VIDEO_CAMERA).withStyleName(ValoTheme.BUTTON_ICON_ONLY);
		screenButton = new MButton().withIcon(FontAwesome.DESKTOP).withStyleName(ValoTheme.BUTTON_ICON_ONLY);

		cameraButton.addClickListener(e -> {
			String statement = String.format("startCamera('%s', '%s')", user.getUserId(), getVideoTagId(user.getUserId()));
			com.vaadin.ui.JavaScript.getCurrent().execute(statement);
		});

		screenButton.addClickListener(e -> {
			String statement = String.format("startScreen('%s', '%s')", user.getUserId(), getVideoTagId(user.getUserId()));
			com.vaadin.ui.JavaScript.getCurrent().execute(statement);
		});

		toolbar.addComponents(startButton, endButton, joinButton, leaveButton, cameraButton, screenButton);

		return toolbar;
	}

	private Component getRoomComponent() {
		roomPanel = new CardCollectionPanel<GxMeetingUser>() {

			@Override
			protected void layoutCard(MPanel cardPanel, GxMeetingUser item) {
				cardPanel.setStyleName(GrapheneeTheme.STYLE_ELEVATED);
				Label video = new Label();
				video.setWidthUndefined();
				video.setContentMode(ContentMode.HTML);
				String html = "<video id=\"" + getVideoTagId(item.getUserId()) + "\" width=\"200px\" height=\"150px\" autoplay></video>";
				video.setValue(html);
				cardPanel.setContent(video);
				cardPanel.setWidth("200px");
				cardPanel.setHeight("150px");
			}

		};

		meetingContainer = new BeanItemContainer<>(GxMeetingUser.class);
		roomPanel.setCollectionContainer(meetingContainer);

		return roomPanel;
	}

	public void initializeWithMeetingUserAndMeeting(GxMeetingUser user, GxMeeting meeting) {
		this.user = user;
		this.meeting = meeting;
		if (meeting != null) {
			if (user.getUserId().equals(meeting.getHost().getUserId())) {
				startButton.setVisible(true);
				endButton.setVisible(true);
				joinButton.setVisible(false);
				leaveButton.setVisible(false);
			} else {
				startButton.setVisible(false);
				endButton.setVisible(false);
				joinButton.setVisible(true);
				leaveButton.setVisible(true);
			}

			boolean online = meeting.isOnline(user);
			boolean started = meeting.isStarted();

			startButton.setEnabled(!started);
			endButton.setEnabled(started);
			joinButton.setEnabled(!online && started);
			leaveButton.setEnabled(online && started);

			initializeWebSocket(user);
		} else {
			startButton.setVisible(false);
			endButton.setVisible(false);
			joinButton.setVisible(false);
			leaveButton.setVisible(false);
		}
	}

	private void initializeWebSocket(GxMeetingUser user) {
		URI pageUri = Page.getCurrent().getLocation();
		StringBuilder sb = new StringBuilder();
		if (pageUri.getScheme().startsWith("https"))
			sb.append("wss://");
		else
			sb.append("ws://");
		sb.append(pageUri.getHost()).append(":").append(pageUri.getPort()).append("/socket");
		String statement = String.format("initializeWebSocket('%s?authToken=%s', '%s', '%s')", sb.toString(), user.getUserId(), user.getUserId(), getVideoTagId(user.getUserId()));
		com.vaadin.ui.JavaScript.getCurrent().execute(statement);
	}

	private void requestOffer(GxMeetingUser user) {
		String statement = String.format("requestOffer('%s', '%s')", user.getUserId(), getVideoTagId(user.getUserId()));
		com.vaadin.ui.JavaScript.getCurrent().execute(statement);
	}

	public String getVideoTagId(String userId) {
		String sanitized = userId.trim().replace('-', '_').replace('.', '_');
		return "vid_" + sanitized;
	}

}
