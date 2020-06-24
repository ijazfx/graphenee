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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import com.google.common.base.Strings;
import com.google.common.eventbus.Subscribe;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
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
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.core.enums.GenderEnum;
import io.graphenee.core.model.GxAuthenticatedUser;
import io.graphenee.gx.theme.graphenee.GrapheneeTheme;
import io.graphenee.vaadin.event.DashboardEvent.BadgeUpdateEvent;
import io.graphenee.vaadin.event.DashboardEvent.PostViewChangeEvent;
import io.graphenee.vaadin.event.DashboardEvent.UserProfileRenderEvent;
import io.graphenee.vaadin.event.DashboardEventBus;
import io.graphenee.vaadin.event.TRButtonClickListener;
import io.graphenee.vaadin.util.DashboardUtils;

/**
 * A responsive menu component providing user information and the controls for
 * primary navigation between the views.
 */
public abstract class AbstractDashboardMenu extends CustomComponent {

	private static final long serialVersionUID = 1L;

	public static final String ID = "dashboard-menu";
	public static final String REPORTS_BADGE_ID = "dashboard-menu-reports-badge";
	public static final String NOTIFICATIONS_BADGE_ID = "dashboard-menu-notifications-badge";
	private static final String STYLE_VISIBLE = "valo-menu-visible";
	private static final String STYLE_SELECTED = "selected";
	private MenuItem userMenuItem;

	private volatile boolean shouldBuild = true;

	public AbstractDashboardMenu() {
		if (!isSpringComponent()) {
			postConstruct();
		}
	}

	public AbstractDashboardMenu(List<Component> menuItems) {
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
		if (shouldBuild) {
			synchronized (this) {
				if (shouldBuild) {
					setId(ID);
					setPrimaryStyleName("valo-menu");
					setSizeUndefined();
					DashboardEventBus.sessionInstance().register(this);
					setCompositionRoot(buildContent());
					postBuild();
					shouldBuild = false;
				}
			}
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

	private GxAuthenticatedUser getCurrentUser() {
		return (GxAuthenticatedUser) VaadinSession.getCurrent().getAttribute(GxAuthenticatedUser.class.getName());
	}

	private Component buildUserMenu() {
		if (userMenu == null) {
			userMenu = new MenuBar();
		}
		userMenu.removeItems();
		userMenu.addStyleName("user-menu");
		final GxAuthenticatedUser user = getCurrentUser();
		if (user != null) {
			byte[] photoBytes = user.getProfilePhoto();
			Resource photo = null;
			if (photoBytes != null) {
				photo = new StreamResource(new StreamResource.StreamSource() {

					@Override
					public InputStream getStream() {
						ByteArrayInputStream bais = new ByteArrayInputStream(photoBytes);
						return bais;
					}
				}, user.getUsername() + "_photo");
			} else {
				if (user.getGender() == GenderEnum.Female) {
					photo = dashboardSetup().femaleAvatar();
				} else {
					photo = dashboardSetup().maleAvatar();
				}
			}
			boolean shouldAddSeparator = false;
			userMenuItem = userMenu.addItem("", photo, null);
			userMenuItem.setText(user.getFirstNameLastName());
			if (dashboardSetup().profileComponent() != null) {
				shouldAddSeparator = true;
				userMenuItem.addItem("Profile", new Command() {

					@Override
					public void menuSelected(final MenuItem selectedItem) {
						BaseProfileForm profileForm = dashboardSetup().profileComponent();
						GxAuthenticatedUser user = DashboardUtils.getLoggedInUser();
						profileForm.setEntity(GxAuthenticatedUser.class, user);
						profileForm.openInModalPopup();
					}
				});
			}
			userMenuItem.addItem("Change Password", new Command() {
				@Override
				public void menuSelected(final MenuItem selectedItem) {
					Page.getCurrent().setLocation("/reset-password");
				}
			});
			if (shouldAddSeparator) {
				userMenuItem.addSeparator();
			}
			if (userMenuItems() != null && !userMenuItems().isEmpty()) {
				for (TRMenuItem menuItem : userMenuItems()) {
					userMenuItem.addItem(menuItem.caption(), menuItem.icon(), menuItem.command());
				}
			}
			if (user != null) {
				userMenuItem.addItem("Sign Out", new Command() {
					@Override
					public void menuSelected(final MenuItem selectedItem) {
						VaadinSession.getCurrent().setAttribute(GxAuthenticatedUser.class, null);
						Page.getCurrent().reload();
					}
				});
			}
		}
		return userMenu;

	}

	private Component buildToggleButton() {
		Button valoMenuToggleButton = new Button("Menu", new TRButtonClickListener() {

			@Override
			public void onButtonClick(ClickEvent event) {
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
		backButton.withListener(new TRButtonClickListener() {

			@Override
			public void onButtonClick(ClickEvent event) {
				String viewName = null;
				focusedMenuItem = focusedMenuItem.getParent() != null ? focusedMenuItem.getParent().getParent() : null;
				if (focusedMenuItem == null) {
					buttonsMap.keySet().forEach(mi -> {
						buttonsMap.get(mi).setVisible(mi.getParent() == null);
					});
					backButton.setVisible(false);
					viewName = menuItems().get(0).viewName();
				} else {
					buttonsMap.values().forEach(vmib -> vmib.setVisible(false));
					focusedMenuItem.getChildren().forEach(mi -> {
						buttonsMap.get(mi).setVisible(true);
					});
					backButton.setVisible(true);
					viewName = focusedMenuItem.getChildren().iterator().next().viewName();
				}
				//				if (viewName == null)
				//					viewName = dashboardSetup().dashboardViewName();
				if (viewName != null)
					UI.getCurrent().getNavigator().navigateTo(viewName);
			}
		});

		generateValoMenuItemButtons(menuItemsLayout, items);
		menuItemsLayout.addComponent(backButton, 0);
		buttonsMap.keySet().forEach(mi -> {
			buttonsMap.get(mi).setVisible(mi.getParent() == null);
		});
		return menuItemsLayout;

	}

	HashMap<TRMenuItem, ValoMenuItemButton> buttonsMap = new HashMap<>();
	private ValoMenuItemButton backButton;
	private MenuBar userMenu;
	private TRMenuItem focusedMenuItem = null;

	private void generateValoMenuItemButtons(CssLayout menuItemsLayout, Collection<TRMenuItem> items) {
		if (items != null && !items.isEmpty()) {
			for (TRMenuItem menuItem : items) {
				if (buttonsMap.containsKey(menuItem))
					continue;
				ValoMenuItemButton valoMenuItemButton = new ValoMenuItemButton(menuItem.hasChildren() ? null : menuItem.viewName(), menuItem.caption(), menuItem.icon())
						.withListener(event -> {
							focusedMenuItem = menuItem;
							if (menuItem.viewName() != null)
								UI.getCurrent().getNavigator().navigateTo(menuItem.viewName());
							if (menuItem.hasChildren()) {
								buttonsMap.values().forEach(vmib -> vmib.setVisible(false));
								menuItem.getChildren().forEach(mi -> {
									buttonsMap.get(mi).setVisible(true);
								});
								backButton.setVisible(true);
							}
						});
				valoMenuItemButton.setBadgeId(menuItem.badgeId());
				valoMenuItemButton.setBadge(menuItem.badge());
				menuItemsLayout.addComponent(valoMenuItemButton);
				buttonsMap.put(menuItem, valoMenuItemButton);
				if (menuItem.hasChildren()) {
					generateValoMenuItemButtons(menuItemsLayout, menuItem.getChildren());
				}
			}
		}
	}

	protected List<TRMenuItem> menuItems() {
		return dashboardSetup().menuItems();
	}

	protected List<TRMenuItem> userMenuItems() {
		return dashboardSetup().profileMenuItems();
	}

	protected void postInitialize() {
	}

	public static class ValoMenuItemButton extends CssLayout {

		private static final long serialVersionUID = 1L;

		private final String viewName;
		private Button button;
		private String badgeId;
		private Label badge;

		public ValoMenuItemButton(String title, Resource icon) {
			this(null, title, icon, null);
		}

		public ValoMenuItemButton(String title, Resource icon, ClickListener listener) {
			this(null, title, icon);
			if (listener != null) {
				button.addClickListener(listener);
			}
		}

		public ValoMenuItemButton(String viewName, String title, Resource icon) {
			this(viewName, title, icon, null);
		}

		public ValoMenuItemButton(String viewName, String title, Resource icon, ClickListener listener) {
			setWidth("100%");
			setPrimaryStyleName("valo-menu-item");
			setStyleName("badgewrapper");
			this.viewName = viewName;
			this.badgeId = null;
			button = new Button();
			button.setCaption(title);
			button.setIcon(icon);
			button.setPrimaryStyleName("valo-menu-item");
			badge = new Label();
			badge.setVisible(false);
			badge.setWidthUndefined();
			badge.setPrimaryStyleName(ValoTheme.MENU_BADGE);
			withListener(listener);
			addComponents(button, badge);
		}

		public void setBadgeId(String badgeId) {
			this.badgeId = badgeId;
		}

		public void setBadge(String value) {
			badge.setValue(value);
			badge.setVisible(!Strings.isNullOrEmpty(value));
		}

		public ValoMenuItemButton withListener(ClickListener listener) {
			if (listener != null) {
				button.addClickListener(listener);
			}
			return this;
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

	@Subscribe
	public void postViewChange(final PostViewChangeEvent event) {
		// After a successful view change the menu can be hidden in mobile view.
		getCompositionRoot().removeStyleName(STYLE_VISIBLE);
		buttonsMap.values().forEach(vmib -> vmib.removeStyleName(STYLE_SELECTED));
		for (TRMenuItem mi : buttonsMap.keySet()) {
			ValoMenuItemButton vmib = buttonsMap.get(mi);
			if (!Strings.isNullOrEmpty(vmib.viewName)) {
				if (vmib.viewName.equals(event.getViewName() + "/" + event.getParameters())
						|| (vmib.viewName.equals(event.getViewName()) && event.getParameters().equalsIgnoreCase(""))) {
					vmib.addStyleName(STYLE_SELECTED);
					focusedMenuItem = mi;
				}
			}
		}
		if (focusedMenuItem.getParent() == null) {
			backButton.setVisible(false);
			buttonsMap.keySet().forEach(mi -> {
				buttonsMap.get(mi).setVisible(mi.getParent() == null);
			});
		} else {
			backButton.setVisible(true);
			buttonsMap.keySet().forEach(mi -> {
				buttonsMap.get(mi).setVisible(mi.getParent() != null && mi.getParent().equals(focusedMenuItem.getParent()));
			});
		}
	}

	@Subscribe
	public void updateBadge(final BadgeUpdateEvent event) {
		UI.getCurrent().access(() -> {
			buttonsMap.values().forEach(button -> {
				if (button.badgeId != null && button.badgeId.equals(event.getBadgeId())) {
					button.setBadge(event.getBadgeValue());
					button.markAsDirty();
				}
			});
			UI.getCurrent().push();
		});
	}

	@Subscribe
	public void onUserProfileRenderEvent(UserProfileRenderEvent event) {
		buildUserMenu();
	}

}
