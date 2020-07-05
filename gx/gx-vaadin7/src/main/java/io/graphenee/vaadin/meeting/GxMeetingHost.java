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
import io.graphenee.vaadin.CardCollectionPanel;

@SuppressWarnings("serial")
@JavaScript({ "meeting-host.js" })
@StyleSheet({ "meeting.css" })
public class GxMeetingHost extends VerticalLayout {

	private BeanItemContainer<GxMeetingUser> meetingContainer;
	private CardCollectionPanel<GxMeetingUser> roomPanel;
	private GxMeetingUser user;
	private GxMeeting meeting;
	private Component roomComponent;
	private MButton startButton;
	private MButton endButton;
	private MButton cameraButton;
	private MButton screenButton;
	private MButton muteAllButton;

	private Boolean allMuted = false;

	public GxMeetingHost() {
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

		cameraButton = new MButton().withIcon(FontAwesome.VIDEO_CAMERA).withStyleName(ValoTheme.BUTTON_ICON_ONLY);
		screenButton = new MButton().withIcon(FontAwesome.DESKTOP).withStyleName(ValoTheme.BUTTON_ICON_ONLY);
		muteAllButton = new MButton().withIcon(FontAwesome.MICROPHONE).withStyleName(ValoTheme.BUTTON_ICON_ONLY);

		cameraButton.addClickListener(e -> {
			String statement = String.format("startCamera()");
			com.vaadin.ui.JavaScript.getCurrent().execute(statement);
		});

		screenButton.addClickListener(e -> {
			String statement = String.format("startScreen()");
			com.vaadin.ui.JavaScript.getCurrent().execute(statement);
		});

		muteAllButton.addClickListener(e -> {
			if (!allMuted) {
				com.vaadin.ui.JavaScript.getCurrent().execute("muteAllAttendees()");
				allMuted = true;
				muteAllButton.setIcon(FontAwesome.MICROPHONE_SLASH);
			} else {
				com.vaadin.ui.JavaScript.getCurrent().execute("unmuteAllAttendees()");
				allMuted = false;
				muteAllButton.setIcon(FontAwesome.MICROPHONE);
			}
		});

		toolbar.addComponents(startButton, endButton, cameraButton, screenButton, muteAllButton);

		return toolbar;
	}

	private Component getRoomComponent() {
		roomPanel = new CardCollectionPanel<GxMeetingUser>() {

			@Override
			protected void layoutCard(MPanel cardPanel, GxMeetingUser item) {
				cardPanel.setId(getVideoTagId(item.getUserId()) + "_container");
				cardPanel.setStyleName("hidden");
				cardPanel.removeStyleName("card-item");
				if (!item.getUserId().equals(user.getUserId())) {
					GxMeetingUserCardComponent component = new GxMeetingUserCardComponent(item);
					cardPanel.setContent(component.rebuild());
					cardPanel.setWidth("200px");
				}
			}

		};
		roomPanel.setSizeFull();

		CssLayout roomLayout = new CssLayout();
		roomLayout.setSizeFull();

		roomLayout.addComponent(roomPanel);

		Label localVideo = new Label();
		localVideo.setWidthUndefined();
		localVideo.setContentMode(ContentMode.HTML);
		String localVideoHtml = "<video id=\"localVideo\" width=\"200px\" height=\"150px\" autoplay muted></video>";
		localVideo.setValue(localVideoHtml);
		roomLayout.addComponent(localVideo);

		meetingContainer = new BeanItemContainer<>(GxMeetingUser.class);
		roomPanel.setCollectionContainer(meetingContainer);

		return roomLayout;
	}

	public void initializeWithMeetingUserAndMeeting(GxMeetingUser user, GxMeeting meeting) {
		this.user = user;
		this.meeting = meeting;
		if (user.getUserId().equals(meeting.getHost().getUserId())) {
			startButton.setVisible(true);
			endButton.setVisible(true);
		} else {
			startButton.setVisible(false);
			endButton.setVisible(false);
		}

		boolean started = meeting.isStarted();

		startButton.setEnabled(!started);
		endButton.setEnabled(started);
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

	public String getVideoTagId(String userId) {
		String sanitized = userId.trim().replace('-', '_').replace('.', '_');
		return "vid_" + sanitized;
	}

}
