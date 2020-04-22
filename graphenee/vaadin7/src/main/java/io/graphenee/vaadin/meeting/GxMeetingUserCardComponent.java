package io.graphenee.vaadin.meeting;

import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.core.model.GxMeetingUser;
import io.graphenee.vaadin.AbstractCardComponent;

@SuppressWarnings("serial")
public class GxMeetingUserCardComponent extends AbstractCardComponent<GxMeetingUser> {

	private Boolean muted = false;

	public GxMeetingUserCardComponent(GxMeetingUser user) {
		super(user);
	}

	@Override
	protected void addComponentToLayout(MVerticalLayout layout, GxMeetingUser user) {
		setMargin(false);
		setSpacing(false);
		layout.setMargin(false);
		layout.setSpacing(false);
		Label video = new Label();
		video.setPrimaryStyleName("remotePeerStream");
		video.setContentMode(ContentMode.HTML);
		String html = "<span class=\"remotePeerTitle\">" + user.getFullName() + "</span>" + "<video id=\"" + getVideoTagId(user.getUserId())
				+ "\" width=\"200px\" height=\"150px\" autoplay></video>";
		video.setValue(html);
		layout.add(video);
	}

	@Override
	protected void addButtonsToFooter(HorizontalLayout additionalButtonToolbar) {
		MButton muteButton = new MButton();
		muteButton.withStyleName(ValoTheme.BUTTON_QUIET);
		muteButton.addStyleName(ValoTheme.BUTTON_SMALL);
		muteButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		muteButton.setIcon(FontAwesome.MICROPHONE);
		muteButton.addClickListener(event -> {
			if (!muted) {
				String statement = String.format("muteAttendee('%s')", getEntity().getUserId());
				com.vaadin.ui.JavaScript.getCurrent().execute(statement);
				muted = true;
				muteButton.setIcon(FontAwesome.MICROPHONE_SLASH);
			} else {
				String statement = String.format("unmuteAttendee('%s')", getEntity().getUserId());
				com.vaadin.ui.JavaScript.getCurrent().execute(statement);
				muted = false;
				muteButton.setIcon(FontAwesome.MICROPHONE);
			}
		});
		additionalButtonToolbar.addComponent(muteButton);
	}

	public String getVideoTagId(String userId) {
		String sanitized = userId.trim().replace('-', '_').replace('.', '_');
		return "vid_" + sanitized;
	}

	@Override
	protected String getCardWidth() {
		return "200px";
	}

	@Override
	protected String getCardHeight() {
		return "150px";
	}

}
