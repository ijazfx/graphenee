package io.graphenee.vaadin.flow;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.flow.server.StreamResource;
import org.apache.logging.log4j.util.Strings;

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
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import io.graphenee.common.GxAuthenticatedUser;
import jakarta.annotation.PostConstruct;
import lombok.Setter;

/**
 * An abstract app layout.
 */
public abstract class GxAbstractAppLayout extends AppLayout {

	private static final long serialVersionUID = 1L;

	@Setter
	private GxAbstractAppLayoutDelegate delegate;

	@PostConstruct
	private void postBuild() {
		DrawerToggle toggle = new DrawerToggle();
		toggle.addClassName("gx-app-layout-toggle");
		// toggle.getStyle().set("color", "var(--lumo-base-color)");

		Span title = new Span(flowSetup().appTitle());
		title.addClassName("gx-app-layout-title");
		// title.getStyle().set("font-size", "var(--lumo-font-size-xl)").set("margin",
		// "0");
		// title.getStyle().set("color", "var(--lumo-base-color)");
		// title.setWidthFull();

		Span version = new Span(flowSetup().appVersion());
		version.addClassName("gx-app-layout-version");
		// version.getStyle().set("font-size", "var(--lumo-font-size-xs)").set("margin",
		// "0");
		// version.getStyle().set("color", "var(--lumo-base-color)");

		SideNav drawer = new SideNav();
		drawer.addClassName("gx-app-layout-drawer");
		drawer.setWidthFull();

		generateMenuItems(drawer, flowSetup().loggedInUser());

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

		Avatar avatar = null;
		if (flowSetup().loggedInUser() != null) {
			GxAuthenticatedUser user = flowSetup().loggedInUser();
			avatar = new Avatar(user.getFirstNameLastName());
			avatar.addThemeVariants(AvatarVariant.LUMO_LARGE);
			if (user.getProfilePhoto() != null) {
				StreamResource resource = new StreamResource("Profile Picture", () -> new java.io.ByteArrayInputStream(user.getProfilePhoto()));
				avatar.setImageResource(resource);
			}
			customizeAvatar(avatar);
			Span space = new Span("");
			space.setWidth("12px");

			Button logout = new Button("Logout");
			logout.addClassName("gx-app-layout-logout");
			logout.getStyle().set("font-size", "var(--lumo-font-size-m)").set("margin", "0");
			logout.getStyle().set("color", "var(--lumo-base-color)");
			logout.addThemeVariants(ButtonVariant.LUMO_ICON);
			logout.addClickListener(cl -> {
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

			HorizontalLayout hl = new HorizontalLayout();
			hl.add(avatar, logout);
			hl.setMargin(true);

			if (logoPosition() == LogoPosition.TITLE_BAR) {
				avatar.addClassName("gx-avatar-small");
				navbarLayout.add(avatar, logout);
			} else {
				avatar.addClassName("gx-avatar-large");
				drawerLayout.addComponentAsFirst(avatar);
			}

			navbarLayout.add(logout);
		}

		addToNavbar(navbarLayout);

	}

	/**
	 * Customizes the avatar.
	 * 
	 * @param avatar The avatar to customize.
	 */
	protected void customizeAvatar(Avatar avatar) {
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
		for (GxMenuItem mi : flowSetup().menuItems()) {
			SideNavItem i = new SideNavItem(mi.getLabel());
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
			SideNavItem i = new SideNavItem(mi.getLabel());
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

}
