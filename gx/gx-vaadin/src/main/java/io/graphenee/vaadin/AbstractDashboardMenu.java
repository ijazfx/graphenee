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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import javax.annotation.PostConstruct;

import com.google.common.eventbus.Subscribe;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.core.callback.TRVoidCallback;
import io.graphenee.gx.theme.graphenee.GrapheneeTheme;
import io.graphenee.vaadin.domain.DashboardUser;
import io.graphenee.vaadin.event.DashboardEvent.NotificationsCountUpdatedEvent;
import io.graphenee.vaadin.event.DashboardEvent.PostViewChangeEvent;
import io.graphenee.vaadin.event.DashboardEvent.ProfileUpdatedEvent;
import io.graphenee.vaadin.event.DashboardEvent.ReportsCountUpdatedEvent;
import io.graphenee.vaadin.event.DashboardEvent.UserLoggedOutEvent;
import io.graphenee.vaadin.event.DashboardEventBus;

/**
 * A responsive menu component providing user information and the controls for
 * primary navigation between the views.
 */
@SuppressWarnings({ "serial", "unchecked" })
public abstract class AbstractDashboardMenu extends CustomComponent {

	public static final String ID = "dashboard-menu";
	public static final String REPORTS_BADGE_ID = "dashboard-menu-reports-badge";
	public static final String NOTIFICATIONS_BADGE_ID = "dashboard-menu-notifications-badge";
	private static final String STYLE_VISIBLE = "valo-menu-visible";
	private Label notificationsBadge;
	private Label reportsBadge;
	private MenuItem userMenuItem;
	private List<Component> menuItems;
	private boolean isBuilt;

	public AbstractDashboardMenu() {
		if (!isSpringComponent()) {
			postConstruct();
		}
	}

	public AbstractDashboardMenu(List<Component> menuItems) {
		this.menuItems = menuItems;
		if (!isSpringComponent()) {
			postConstruct();
		}
	}

	protected boolean isSpringComponent() {
		return this.getClass().getAnnotation(SpringComponent.class) != null;
	}

	@PostConstruct
	private void postConstruct() {
		postInitialize();
	}

	public AbstractDashboardMenu build() {
		if (!isBuilt) {
			setPrimaryStyleName("valo-menu");
			setId(ID);
			setSizeUndefined();
			// There's only one DashboardMenu per UI so this doesn't need to be
			// unregistered from the UI-scoped DashboardEventBus.
			DashboardEventBus.sessionInstance().register(this);
			setCompositionRoot(buildContent());
			postBuild();
			isBuilt = true;
		}
		return this;
	}

	protected void postBuild() {
	}

	private Component buildContent() {
		final CssLayout menuContent = new CssLayout();
		menuContent.addStyleName("sidebar");
		menuContent.addStyleName(ValoTheme.MENU_PART);
		menuContent.addStyleName("no-vertical-drag-hints");
		menuContent.addStyleName("no-horizontal-drag-hints");
		menuContent.setWidth(null);
		menuContent.setHeight("100%");

		menuContent.addComponent(buildTitle());
		menuContent.addComponent(buildUserMenu());
		menuContent.addComponent(buildToggleButton());
		menuContent.addComponent(buildMenuItems());

		return menuContent;
	}

	private Component buildTitle() {
		VerticalLayout layout = new VerticalLayout();
		Image logo = dashboardSetup().dashboardLogo();
		if (logo != null) {
			layout.setStyleName("valo-menu-title-with-image");
			logo.setHeight("80px");
			layout.addComponent(logo);
		}
		Component title = new Label(dashboardSetup().dashboardTitle(), ContentMode.HTML);
		HorizontalLayout titleWrapper = new HorizontalLayout(title);
		titleWrapper.setComponentAlignment(title, Alignment.MIDDLE_CENTER);
		titleWrapper.addStyleName("valo-menu-title");
		layout.addComponents(titleWrapper);
		return layout;

	}

	protected abstract AbstractDashboardSetup dashboardSetup();

	private DashboardUser getCurrentUser() {
		return (DashboardUser) VaadinSession.getCurrent().getAttribute(DashboardUser.class.getName());
	}

	private Component buildUserMenu() {
		final MenuBar userMenu = new MenuBar();
		userMenu.addStyleName("user-menu");
		final DashboardUser user = getCurrentUser();
		userMenuItem = userMenu.addItem("", user.getProfilePhoto(), null);
		updateUserName(null);
		boolean shouldAddSeparator = false;
		if (dashboardSetup().profileComponent() != null) {
			shouldAddSeparator = true;
			userMenuItem.addItem("Edit Profile", new Command() {
				@Override
				public void menuSelected(final MenuItem selectedItem) {
					AbstractComponent profileComponent = dashboardSetup().profileComponent();
					if (profileComponent instanceof TRAbstractForm<?>) {
						TRAbstractForm<?> form = (TRAbstractForm<?>) profileComponent;
						form.openInModalPopup();
					} else {
						Window window = new Window("Profile", profileComponent);
						window.setModal(true);
						UI.getCurrent().addWindow(window);
						window.focus();
					}
				}
			});
		}
		if (dashboardSetup().preferencesComponent() != null) {
			shouldAddSeparator = true;
			userMenuItem.addItem("Preferences", new Command() {
				@Override
				public void menuSelected(final MenuItem selectedItem) {
					AbstractComponent preferencesComponent = dashboardSetup().preferencesComponent();
					if (preferencesComponent instanceof TRAbstractForm<?>) {
						TRAbstractForm<?> form = (TRAbstractForm<?>) preferencesComponent;
						form.openInModalPopup();
					} else {
						Window window = new Window("Preferences", preferencesComponent);
						window.setModal(true);
						UI.getCurrent().addWindow(window);
						window.focus();
					}
				}
			});
		}
		if (shouldAddSeparator) {
			userMenuItem.addSeparator();
		}
		if (userMenuItems() != null && !userMenuItems().isEmpty()) {
			for (TRMenuItem menuItem : userMenuItems()) {
				userMenuItem.addItem(menuItem.caption(), menuItem.icon(), menuItem.command());
			}
		}
		userMenuItem.addItem("Sign Out", new Command() {
			@Override
			public void menuSelected(final MenuItem selectedItem) {
				DashboardEventBus.sessionInstance().post(new UserLoggedOutEvent());
			}
		});
		return userMenu;
	}

	private Component buildToggleButton() {
		Button valoMenuToggleButton = new Button("Menu", new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				if (getCompositionRoot().getStyleName().contains(STYLE_VISIBLE)) {
					getCompositionRoot().removeStyleName(STYLE_VISIBLE);
				} else {
					getCompositionRoot().addStyleName(STYLE_VISIBLE);
				}
			}
		});
		valoMenuToggleButton.setIcon(FontAwesome.LIST);
		valoMenuToggleButton.addStyleName("valo-menu-toggle");
		valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_SMALL);
		return valoMenuToggleButton;
	}

	private Component buildMenuItems() {
		CssLayout menuItemsLayout = new CssLayout();
		menuItemsLayout.addStyleName("valo-menuitems");
		Collection<TRMenuItem> items = menuItems();
		backButton = new ValoMenuItemButton("Back", GrapheneeTheme.BACK_ICON);
		backButton.setVisible(false);
		backButton.addClickListener(event -> {
			if (!backStack.isEmpty())
				backStack.pop().execute();
		});
		generateValoMenuItemButtons(menuItemsLayout, items);
		menuItemsLayout.addComponent(backButton, 0);
		return menuItemsLayout;

	}

	HashMap<TRMenuItem, ValoMenuItemButton> buttonsMap = new HashMap<>();
	private ValoMenuItemButton backButton;
	private Stack<TRVoidCallback> backStack = new Stack<>();

	private void generateValoMenuItemButtons(CssLayout menuItemsLayout, Collection<TRMenuItem> items) {
		buttonsMap.values().forEach(button -> {
			button.setVisible(false);
		});
		if (items != null && !items.isEmpty()) {
			for (TRMenuItem menuItem : items) {
				if (buttonsMap.containsKey(menuItem))
					continue;
				ValoMenuItemButton valoMenuItemButton = null;

				if (menuItem.hasChildren()) {
					valoMenuItemButton = new ValoMenuItemButton(menuItem.caption(), menuItem.icon()).withListener(event -> {
						backStack.push(() -> {
							if (menuItem.getParent() != null) {
								generateValoMenuItemButtons(menuItemsLayout, menuItem.getParent().getChildren());
								menuItem.getParent().getChildren().forEach(child -> {
									buttonsMap.get(child).setVisible(true);
								});
								backButton.setVisible(true);
							} else {
								generateValoMenuItemButtons(menuItemsLayout, menuItems());
								menuItems().forEach(child -> {
									buttonsMap.get(child).setVisible(true);
								});
								backButton.setVisible(false);
							}
						});
						generateValoMenuItemButtons(menuItemsLayout, menuItem.getChildren());
						menuItem.getChildren().forEach(child -> {
							buttonsMap.get(child).setVisible(true);
						});
						backButton.setVisible(true);
					});
					menuItemsLayout.addComponent(valoMenuItemButton);
					buttonsMap.put(menuItem, valoMenuItemButton);
				} else {
					valoMenuItemButton = new ValoMenuItemButton(menuItem.viewName(), menuItem.caption(), menuItem.icon()).withListener(event -> {
						UI.getCurrent().getNavigator().navigateTo(menuItem.viewName());
					});
					menuItemsLayout.addComponent(valoMenuItemButton);
				}

				buttonsMap.put(menuItem, valoMenuItemButton);
			}
		}
	}

	protected List<TRMenuItem> menuItems() {
		return dashboardSetup().menuItems();
	}

	protected List<TRMenuItem> userMenuItems() {
		return dashboardSetup().profileMenuItems();
	}

	private Component buildBadgeWrapper(final Component menuItemButton, final Component badgeLabel) {
		CssLayout dashboardWrapper = new CssLayout(menuItemButton);
		dashboardWrapper.addStyleName("badgewrapper");
		dashboardWrapper.addStyleName(ValoTheme.MENU_ITEM);
		badgeLabel.addStyleName(ValoTheme.MENU_BADGE);
		badgeLabel.setWidthUndefined();
		badgeLabel.setVisible(false);
		dashboardWrapper.addComponent(badgeLabel);
		return dashboardWrapper;
	}

	protected void postInitialize() {
	}

	@Override
	public void attach() {
		super.attach();
		updateNotificationsCount(null);
	}

	@Subscribe
	public void postViewChange(final PostViewChangeEvent event) {
		// After a successful view change the menu can be hidden in mobile view.
		getCompositionRoot().removeStyleName(STYLE_VISIBLE);
	}

	@Subscribe
	public void updateNotificationsCount(final NotificationsCountUpdatedEvent event) {
		if (notificationsBadge != null) {
			int unreadNotificationsCount = 0; // AbstractDashboardUI.getDataProvider().getUnreadNotificationsCount();
			notificationsBadge.setValue(String.valueOf(unreadNotificationsCount));
			notificationsBadge.setVisible(unreadNotificationsCount > 0);
		}
	}

	@Subscribe
	public void updateReportsCount(final ReportsCountUpdatedEvent event) {
		reportsBadge.setValue(String.valueOf(event.getCount()));
		reportsBadge.setVisible(event.getCount() > 0);
	}

	@Subscribe
	public void updateUserName(final ProfileUpdatedEvent event) {
		DashboardUser user = getCurrentUser();
		String userFullName = user.getFirstNameLastName();
		userMenuItem.setText(userFullName);
		userMenuItem.setIcon(user.getProfilePhoto());
	}

	public static class ValoMenuItemButton extends Button {

		private static final String STYLE_SELECTED = "selected";

		private final String viewName;

		public ValoMenuItemButton(String title, Resource icon) {
			this(null, title, icon, null);
		}

		public ValoMenuItemButton(String title, Resource icon, ClickListener listener) {
			this(null, title, icon);
			if (listener != null) {
				addClickListener(listener);
			}
		}

		public ValoMenuItemButton(String viewName, String title, Resource icon) {
			this(viewName, title, icon, null);
		}

		public ValoMenuItemButton(String viewName, String title, Resource icon, ClickListener listener) {
			this.viewName = viewName;
			setPrimaryStyleName("valo-menu-item");
			setIcon(icon);
			setCaption(title);
			DashboardEventBus.sessionInstance().register(this);
			withListener(listener);
		}

		public ValoMenuItemButton withListener(ClickListener listener) {
			if (listener != null) {
				addClickListener(listener);
			}
			return this;
		}

		@Subscribe
		public void postViewChange(final PostViewChangeEvent event) {
			removeStyleName(STYLE_SELECTED);
			if (viewName != null) {
				if (viewName.equals(event.getViewName() + "/" + event.getParameters()))
					addStyleName(STYLE_SELECTED);
				else if (viewName.equals(event.getViewName()) && event.getParameters().equalsIgnoreCase(""))
					addStyleName(STYLE_SELECTED);
			}
		}

		@Override
		public void setVisible(boolean visible) {
			super.setVisible(visible);
			if (visible) {
				addStyleName("show");
				removeStyleName("hide");
			} else {
				removeStyleName("show");
				addStyleName("hide");
			}
		}

	}

}
