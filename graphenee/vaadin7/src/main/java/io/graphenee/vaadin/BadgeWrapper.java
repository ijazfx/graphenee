package io.graphenee.vaadin;

import com.google.common.base.Strings;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

public class BadgeWrapper extends CssLayout {

	public static final String NOTIFICATIONS_BADGE_ID = "notifications";

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

	public void badgeUp(int count) {
		if (Strings.isNullOrEmpty(badge.getCaption())) {
			setBadgeValue(count + "");
		} else {
			try {
				String oldValue = badge.getCaption();
				Integer badge = Integer.parseInt(oldValue);
				badge += count;
				setBadgeValue(badge + "");
			} catch (Exception ex) {
				setBadgeValue("1");
			}
		}
	}

	public void setBadgeValue(String value) {
		badge.setCaption(value);
		badge.setVisible(value != null);
	}

}
