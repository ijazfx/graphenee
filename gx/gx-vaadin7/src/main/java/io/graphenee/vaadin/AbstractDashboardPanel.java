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
package io.graphenee.vaadin;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.vaadin.viritin.button.MButton;

import com.google.common.eventbus.Subscribe;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.core.model.GxAuthenticatedUser;
import io.graphenee.vaadin.event.DashboardEvent;
import io.graphenee.vaadin.event.DashboardEventBus;
import io.graphenee.vaadin.util.DashboardUtils;
import io.graphenee.vaadin.util.VaadinUtils;

@SuppressWarnings("serial")
public abstract class AbstractDashboardPanel extends VerticalLayout {

	private HorizontalLayout toolbar;
	private VerticalLayout componentLayout;
	private HorizontalLayout header;
	private Label titleLabel;
	private MButton notificationButton;
	private BadgeWrapper notificationBadge;

	public AbstractDashboardPanel() {
		if (!isSpringComponent()) {
			postConstruct();
		}
	}

	protected boolean isSpringComponent() {
		return this.getClass().getAnnotation(SpringComponent.class) != null || this.getClass().getAnnotation(SpringView.class) != null;
	}

	@PostConstruct
	private void postConstruct() {
		setSizeFull();
		setStyleName("dashboard-panel");
		Component header = buildHeader();
		super.addComponent(header);
		super.setExpandRatio(header, 0.0f);
		header.setVisible(shouldShowHeader());
		componentLayout = buildComponentLayout();
		super.addComponent(componentLayout);
		super.setExpandRatio(componentLayout, 1.0f);
		postInitialize();
	}

	protected boolean shouldShowHeader() {
		return false;
	}

	protected boolean shouldShowActionButton() {
		return false;
	}

	private VerticalLayout buildComponentLayout() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.addStyleName("viewlayout");
		layout.setMargin(false);
		layout.setSpacing(true);
		return layout;
	}

	private Component buildHeader() {
		header = new HorizontalLayout();
		header.setMargin(false);
		header.setSpacing(false);
		header.setDefaultComponentAlignment(Alignment.TOP_LEFT);
		header.addStyleName("viewheader");
		Responsive.makeResponsive(header);

		titleLabel = new Label(localizedSingularValue(panelTitle()));
		titleLabel.setWidth("100%");
		titleLabel.addStyleName(ValoTheme.LABEL_H1);
		titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		header.addComponent(titleLabel);
		Component toolbar = buildToolbar();
		header.addComponent(toolbar);
		header.setWidth("100%");
		header.setExpandRatio(titleLabel, 1);

		return header;
	}

	private Component buildToolbar() {
		toolbar = new HorizontalLayout();
		toolbar.addStyleName("toolbar");
		toolbar.setSpacing(true);
		toolbar.setMargin(false);
		toolbar.setVisible(true);

		notificationButton = new MButton().withStyleName(ValoTheme.BUTTON_ICON_ONLY, ValoTheme.BUTTON_BORDERLESS).withIcon(FontAwesome.BELL);
		notificationButton.addClickListener(event -> {
			UI.getCurrent().getNavigator().navigateTo("notifications");
		});

		notificationBadge = new BadgeWrapper(notificationButton);
		notificationBadge.setId(BadgeWrapper.NOTIFICATIONS_BADGE_ID);

		GxAuthenticatedUser user = DashboardUtils.getLoggedInUser();
		if (user != null && user.getUnreadNotificationCount() > 0) {
			notificationBadge.setBadgeValue(user.getUnreadNotificationCount() + "");
		} else {
			notificationBadge.clearBadgeValue();
		}

		toolbar.addComponent(notificationBadge);
		toolbar.setComponentAlignment(notificationBadge, Alignment.MIDDLE_RIGHT);
		notificationBadge.setVisible(shouldShowNotifications());

		DashboardEventBus.sessionInstance().register(this);

		return toolbar;
	}

	protected boolean shouldShowNotifications() {
		return true;
	}

	protected abstract String panelTitle();

	protected abstract void postInitialize();

	protected void addButtons(Button... buttons) {
		toolbar.setVisible(true);
		for (Button button : buttons) {
			toolbar.addComponent(button);
		}
	}

	protected void addComponentsToToolbar(Component... components) {
		toolbar.setVisible(true);
		for (Component component : components) {
			toolbar.addComponent(component);
		}
	}

	public void addComponentWithExpandRatio(Component component, float expandRatio) {
		componentLayout.addComponent(component);
		componentLayout.setExpandRatio(component, expandRatio);
	}

	@Override
	public void addComponent(Component component) {
		componentLayout.addComponent(component);
	}

	protected String localizedSingularValue(String key) {
		return VaadinUtils.localizedSingularValue(key);
	}

	protected String localizedPluralValue(String key) {
		return VaadinUtils.localizedSingularValue(key);
	}

	protected void localizeRecursively(Component component) {
		VaadinUtils.localizeRecursively(component);
	}

	protected String localizedSingularValue(Locale locale, String key) {
		return VaadinUtils.localizedSingularValue(key);
	}

	protected String localizedPluralValue(Locale locale, String key) {
		return VaadinUtils.localizedSingularValue(key);
	}

	protected void localizeRecursively(Locale locale, Component component) {
		VaadinUtils.localizeRecursively(component);
	}

	public void setPanelTitle(String panelTitle) {
		if (titleLabel != null) {
			if (panelTitle != null)
				titleLabel.setValue(panelTitle);
			else
				titleLabel.setValue(panelTitle());
		}
	}

	@Subscribe
	public void onBadgeUpdateEvent(DashboardEvent.BadgeUpdateEvent event) {
		if (event.getBadgeId().equals(notificationBadge.getId())) {
			UI ui = DashboardUtils.getCurrentUI(event.getUser());
			if (ui != null && ui.isAttached()) {
				ui.access(() -> {
					String value = event.getBadgeValue();
					if (value == null || value.equals("0")) {
						notificationBadge.clearBadgeValue();
					} else {
						notificationBadge.setBadgeValue(value);
					}
					ui.push();
				});
			}
		}
	}

}
