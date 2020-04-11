package io.graphenee.vaadin;

import com.google.common.eventbus.Subscribe;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

import io.graphenee.vaadin.event.DashboardEvent;

public class BadgeWrapper extends CssLayout {

	private String badgeId;
	private Label badge;

	public BadgeWrapper(Component component) {
		setWidth("100%");
		setPrimaryStyleName("gx-badge-wrapper");
		this.badgeId = null;
		badge = new Label();
		badge.setStyleName("gx-badge");
		clearBadgeValue();
		addComponents(component, badge);
	}

	public void clearBadgeValue() {
		badge.setCaption(null);
		badge.setVisible(false);
	}

	public void setBadgeValue(String value) {
		badge.setCaption(value);
		badge.setVisible(value != null);
	}

	@Subscribe
	public void onBadgeEvent(DashboardEvent.BadgeUpdateEvent event) {
		if (event.getBadgeId().equals(badgeId)) {
			setBadgeValue(event.getBadgeValue());
		}
	}

}
