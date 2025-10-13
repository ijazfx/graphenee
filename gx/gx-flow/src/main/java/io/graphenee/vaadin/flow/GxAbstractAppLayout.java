package io.graphenee.vaadin.flow;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.Direction;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.ContentAlignment;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexDirection;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.streams.DownloadEvent;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;
import com.vaadin.flow.server.streams.InputStreamDownloadCallback;
import com.vaadin.flow.server.streams.InputStreamDownloadHandler;

import io.graphenee.common.GxAuthenticatedUser;
import jakarta.annotation.PostConstruct;
import lombok.Setter;

/**
 * An abstract app layout.
 */
public abstract class GxAbstractAppLayout extends AppLayout implements LocaleChangeObserver {

	private static final long serialVersionUID = 1L;

	@Setter
	private GxAbstractAppLayoutDelegate delegate;

	private Span title = new Span();

	private Span version = new Span();

	private SideNav drawer = new SideNav();

	@PostConstruct
	private void postBuild() {
		DrawerToggle toggle = new DrawerToggle();
		toggle.addClassName("gx-app-layout-toggle");
		// toggle.getStyle().set("color", "var(--lumo-base-color)");

		title.addClassName("gx-app-layout-title");
		// title.getStyle().set("font-size", "var(--lumo-font-size-xl)").set("margin",
		// "0");
		// title.getStyle().set("color", "var(--lumo-base-color)");
		// title.setWidthFull();

		version.addClassName("gx-app-layout-version");
		// version.getStyle().set("font-size", "var(--lumo-font-size-xs)").set("margin",
		// "0");
		// version.getStyle().set("color", "var(--lumo-base-color)");

		drawer.addClassName("gx-app-layout-drawer");
		drawer.setWidthFull();

		FlexLayout drawerLayout = new FlexLayout();
		drawerLayout.setFlexDirection(FlexDirection.COLUMN);
		drawerLayout.setAlignItems(Alignment.CENTER);
		drawerLayout.addClassName("gx-app-layout-drawer-layout");
		// drawerLayout.setMargin(false);
		// drawerLayout.setPadding(false);
		drawerLayout.setWidthFull();
		drawerLayout.add(drawer);

		addToDrawer(drawerLayout);

		FlexLayout navbarLayout = new FlexLayout();
		navbarLayout.setSizeFull();
		navbarLayout.addClassName("gx-app-layout-navbar");
		navbarLayout.setAlignContent(ContentAlignment.START);
		navbarLayout.setAlignItems(Alignment.CENTER);
		navbarLayout.getStyle().setHeight("var(--lumo-size-xl)");

		HorizontalLayout titleVersionLayout = new HorizontalLayout(title, version);
		titleVersionLayout.addClassName("gx-app-layout-title-version");
		titleVersionLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

		navbarLayout.add(toggle, titleVersionLayout);

		Div spacer = new Div();
		navbarLayout.add(spacer);
		navbarLayout.expand(spacer);
		navbarLayout.add(new GxLanguageBar());

		Avatar avatar = null;
		if (flowSetup().loggedInUser() != null) {
			GxAuthenticatedUser user = flowSetup().loggedInUser();
			avatar = new Avatar(user.getFirstNameLastName());
			avatar.addThemeVariants(AvatarVariant.LUMO_LARGE);
			if (user.getProfilePhoto() != null) {
				DownloadHandler dh = new InputStreamDownloadHandler(new InputStreamDownloadCallback() {

					@Override
					public DownloadResponse complete(DownloadEvent downloadEvent) throws IOException {
						return new DownloadResponse(new java.io.ByteArrayInputStream(user.getProfilePhoto()),
								"Profile Picture", null,
								user.getProfilePhoto().length);
					}

				});
				avatar.setImageHandler(dh);
			}
			avatar.getElement().addEventListener("click", e -> {
				GxAbstractEntityForm<?> profileForm = getProfileForm(user);
				if (profileForm != null) {
					showProfileDialog(profileForm);
				}
			});
			customizeAvatar(avatar);
			Span space = new Span("");
			space.setWidth("12px");

			Button logout = new Button("Logout");
			logout.addClassName("gx-app-layout-logout");
			logout.getStyle().set("font-size", "var(--lumo-font-size-m)").set("margin", "0");
			logout.getStyle().set("color", "var(--lumo-base-color)");
			logout.addThemeVariants(ButtonVariant.LUMO_ICON);
			logout.addClickListener(cl -> {
				SecurityContextHolder.clearContext();
				VaadinSession.getCurrent().getSession().invalidate();
				VaadinSession.getCurrent().close();
				getUI().ifPresent(ui -> {
					if (delegate != null) {
						delegate.onLogout(ui);
					} else {
						ui.navigate("/");
					}
				});
			});

			if (logoPosition() == LogoPosition.TITLE_BAR) {
				avatar.addClassName("gx-avatar-small");
				navbarLayout.add(avatar);
			} else {
				avatar.addClassName("gx-avatar-large");
				drawerLayout.addComponentAsFirst(avatar);
			}

			navbarLayout.add(logout);
		}

		addToNavbar(navbarLayout);

	}

	private <T> void showProfileDialog(GxAbstractEntityForm<T> profileForm) {
		T entity = profileForm.getEntity();
		profileForm.showInDialog(entity);
		profileForm.setDelegate(new GxAbstractEntityForm.EntityFormDelegate<T>() {
			@Override
			public void onSave(T entity) {
				saveProfile(entity);

			}
		});
	}

	/**
	 * Customizes the avatar.
	 * 
	 * @param avatar The avatar to customize.
	 */
	protected void customizeAvatar(Avatar avatar) {
	}

	/**
	 * Get the profile form.
	 *
	 * @param user The user for form.
	 */
	protected GxAbstractEntityForm<?> getProfileForm(GxAuthenticatedUser user) {
		return null;
	}

	/**
	 * Saves the user profile.
	 *
	 * @param user The user to save.
	 */
	protected void saveProfile(Object user) {
	}

	private boolean canDoAction(GxAuthenticatedUser user, String action, GxMenuItem mi) {
		if (user == null)
			return false;
		String route = mi.getRoute();
		if (Strings.isBlank(route) && mi.getComponentClass() != null) {
			Class<?> klass = mi.getComponentClass();
			if (klass.isAnnotationPresent(GxSecuredView.class)) {
				GxSecuredView annotation = klass.getAnnotation(GxSecuredView.class);
				if (annotation.value() != null) {
					route = annotation.value();
				} else if (klass.isAnnotationPresent(Route.class)) {
					Route routeAnnotation = this.getClass().getAnnotation(Route.class);
					route = routeAnnotation.value();
				}
			}
		}
		if (Strings.isBlank(route))
			return true;
		Map<String, Object> keyValueMap = new HashMap<>();
		keyValueMap.put("username", user.getUsername());
		return user.canDoAction(route, action, keyValueMap);
	}

	private void generateMenuItems(SideNav drawer, GxAuthenticatedUser user) {
		drawer.removeAll();
		for (GxMenuItem mi : flowSetup().menuItems()) {
			SideNavItem i = new SideNavItem(getTranslation(mi.getLabel()));
			i.addClassName("gx-nav-menuitem");
			i.addClassName("gx-nav-menuitem-root");
			i.setPrefixComponent(mi.getIcon());
			boolean added = false;
			if (canDoAction(user, "view", mi)) {
				if (mi.getRoute() != null) {
					i.setPath(mi.getRoute());
					drawer.addItem(i);
					added = true;
				} else if (mi.getComponentClass() != null) {
					i.setPath(mi.getComponentClass());
					drawer.addItem(i);
					added = true;
				}
			}
			if (mi.hasChildren()) {
				i.addClassName("gx-nav-menuitem-parent");
				int count = generateMenuItems(i, mi, user);
				if (count > 0 && !added)
					drawer.addItem(i);
			}
		}
	}

	private Integer generateMenuItems(SideNavItem parent, GxMenuItem pmi, GxAuthenticatedUser user) {
		int count = 0;
		for (GxMenuItem mi : pmi.getChildren()) {
			SideNavItem i = new SideNavItem(getTranslation(mi.getLabel()));
			i.addClassName("gx-nav-menuitem");
			i.addClassName("gx-nav-menuitem-child");
			i.setPrefixComponent(mi.getIcon());
			boolean added = false;
			if (canDoAction(user, "view", mi)) {
				if (mi.getRoute() != null) {
					i.setPath(mi.getRoute());
					parent.addItem(i);
					added = true;
					count++;
				} else if (mi.getComponentClass() != null) {
					i.setPath(mi.getComponentClass());
					parent.addItem(i);
					added = true;
					count++;
				}
			}
			if (mi.hasChildren()) {
				i.addClassName("gx-nav-menuitem-parent");
				count = generateMenuItems(i, mi, user);
				if (count > 0 && !added)
					parent.addItem(i);
			}
		}
		return count;
	}

	/**
	 * Gets the flow setup.
	 * 
	 * @return The flow setup.
	 */
	protected abstract GxAbstractFlowSetup flowSetup();

	/**
	 * An enum that represents the position of the logo.
	 */
	public static enum LogoPosition {
		/**
		 * The logo is in the title bar.
		 */
		TITLE_BAR,
		/**
		 * The logo is in the drawer.
		 */
		DRAWER
	}

	/**
	 * Gets the position of the logo.
	 * 
	 * @return The position of the logo.
	 */
	protected LogoPosition logoPosition() {
		return LogoPosition.TITLE_BAR;
	}

	/**
	 * A delegate for the app layout.
	 */
	public static interface GxAbstractAppLayoutDelegate {
		/**
		 * Called when the user logs out.
		 * 
		 * @param ui The UI.
		 */
		default void onLogout(UI ui) {
			ui.navigate("/");
		}
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		String lang = event.getLocale().getLanguage().split("_")[0];
		boolean isRtl = "ar, he, fa, ur, ps, sd, ckb, ug, yi".contains(lang);
		event.getUI().setDirection(isRtl ? Direction.RIGHT_TO_LEFT : Direction.LEFT_TO_RIGHT);
		event.getUI().access(() -> {
			localizeUI(event.getUI());
		});
	}

	protected void localizeUI(UI ui) {
		title.setText(flowSetup().appTitle());
		version.setText(flowSetup().appVersion());
		generateMenuItems(drawer, flowSetup().loggedInUser());
	}

}