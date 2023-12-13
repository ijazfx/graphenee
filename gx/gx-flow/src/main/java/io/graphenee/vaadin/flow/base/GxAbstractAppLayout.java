package io.graphenee.vaadin.flow.base;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.ContentAlignment;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexDirection;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.VaadinSession;

import io.graphenee.core.model.GxAuthenticatedUser;
import jakarta.annotation.PostConstruct;
import lombok.Setter;

@Push
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
@CssImport("./styles/graphenee.css")
public abstract class GxAbstractAppLayout extends AppLayout implements RouterLayout, AppShellConfigurator {

	private static final long serialVersionUID = 1L;

	@Setter
	private GxAbstractAppLayoutDelegate delegate;

	@PostConstruct
	private void postBuild() {
		DrawerToggle toggle = new DrawerToggle();
		toggle.addClassName("gx-app-layout-toggle");
		toggle.getStyle().set("color", "var(--lumo-base-color)");

		H1 title = new H1(flowSetup().appTitle());
		title.addClassName("gx-app-layout-title");
		title.getStyle().set("font-size", "var(--lumo-font-size-xl)").set("margin", "0");
		title.getStyle().set("color", "var(--lumo-base-color)");
		//title.setWidthFull();

		H5 version = new H5(flowSetup().appVersion());
		version.addClassName("gx-app-layout-version");
		version.getStyle().set("font-size", "var(--lumo-font-size-xs)").set("margin", "0");
		version.getStyle().set("color", "var(--lumo-base-color)");

		SideNav drawer = new SideNav();
		drawer.addClassName("gx-app-layout-drawer");
		drawer.setWidthFull();

		generateMenuItems(drawer);

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
		titleVersionLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

		navbarLayout.add(toggle, titleVersionLayout);

		Div spacer = new Div();
		navbarLayout.add(spacer);
		navbarLayout.expand(spacer);

		Avatar avatar = null;
		if (flowSetup().loggedInUser() != null) {
			GxAuthenticatedUser user = flowSetup().loggedInUser();
			avatar = new Avatar(user.getFirstNameLastName());
			avatar.getStyle().set("font-size", "var(--lumo-font-size-xl)").set("margin", "0");
			avatar.getStyle().set("background", "var(--lumo-base-color)");
			avatar.getStyle().set("color", "var(--lumo-primary-color)");
			Span space = new Span("");
			space.setWidth("12px");

			Button logout = new Button("Logout");
			logout.addClassName("gx-app-layout-logout");
			logout.getStyle().set("font-size", "var(--lumo-font-size-m)").set("margin", "0");
			logout.getStyle().set("color", "var(--lumo-base-color)");
			logout.addThemeVariants(ButtonVariant.LUMO_ICON);
			logout.addClickListener(cl -> {
				getUI().ifPresent(ui -> {
					VaadinSession.getCurrent().setAttribute(GxAuthenticatedUser.class, null);
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

	private void generateMenuItems(SideNav drawer) {
		flowSetup().menuItems().forEach(mi -> {
			SideNavItem i = new SideNavItem(mi.getLabel());
			i.addClassName("gx-nav-menuitem");
			i.addClassName("gx-nav-menuitem-root");
			i.setPrefixComponent(mi.getIcon());
			if (mi.getRoute() != null) {
				i.setPath(mi.getRoute());
			} else if (mi.getComponentClass() != null) {
				i.setPath(mi.getComponentClass());
			}
			if (mi.hasChildren()) {
				i.addClassName("gx-nav-menuitem-parent");
				generateMenuItems(i, mi);
			}
			drawer.addItem(i);
		});
	}

	private void generateMenuItems(SideNavItem parent, GxMenuItem pmi) {
		pmi.getChildren().forEach(mi -> {
			SideNavItem i = new SideNavItem(mi.getLabel());
			i.addClassName("gx-nav-menuitem");
			i.setPrefixComponent(mi.getIcon());
			if (mi.getRoute() != null) {
				i.setPath(mi.getRoute());
			} else if (mi.getComponentClass() != null) {
				i.setPath(mi.getComponentClass());
			}
			if (mi.hasChildren()) {
				i.addClassName("gx-nav-menuitem-parent");
				generateMenuItems(i, mi);
			}
			parent.addItem(i);
		});
	}

	protected abstract GxAbstractFlowSetup flowSetup();

	public static enum LogoPosition {
		TITLE_BAR,
		DRAWER
	}

	protected LogoPosition logoPosition() {
		return LogoPosition.TITLE_BAR;
	}

	public static interface GxAbstractAppLayoutDelegate {
		default void onLogout(UI ui) {
			ui.navigate("/");
		}
	}

}
