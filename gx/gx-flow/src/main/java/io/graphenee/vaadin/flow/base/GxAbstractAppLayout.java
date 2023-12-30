package io.graphenee.vaadin.flow.base;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.eventbus.Subscribe;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H5;
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
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.VaadinSession;

import io.graphenee.core.model.GxAuthenticatedUser;
import io.graphenee.vaadin.flow.GxEventBus;
import io.graphenee.vaadin.flow.GxEventBus.RemoveComponentEvent;
import io.graphenee.vaadin.flow.GxEventBus.ResizeComponentEvent;
import io.graphenee.vaadin.flow.GxEventBus.ShowComponentEvent;
import io.graphenee.vaadin.flow.GxEventBus.TargetArea;
import io.graphenee.vaadin.flow.component.GxStackLayout;
import jakarta.annotation.PostConstruct;
import lombok.Setter;

@Push
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
@CssImport("./styles/graphenee.css")
public abstract class GxAbstractAppLayout extends AppLayout implements RouterLayout, AppShellConfigurator {

	private static final long serialVersionUID = 1L;

	@Setter
	private GxAbstractAppLayoutDelegate delegate;

	@Autowired
	GxEventBus eventBus;

	private FlexLayout rootLayout;
	private GxStackLayout contentLayout;
	private GxStackLayout rightDrawerLayout;

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		eventBus.register(this);
		super.onAttach(attachEvent);
	}

	@Override
	protected void onDetach(DetachEvent detachEvent) {
		eventBus.unregister(this);
		super.onDetach(detachEvent);
	}

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
		titleVersionLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

		navbarLayout.add(toggle, titleVersionLayout);

		Div spacer = new Div();
		navbarLayout.add(spacer);
		navbarLayout.expand(spacer);

		Avatar avatar = null;
		if (flowSetup().loggedInUser() != null) {
			GxAuthenticatedUser user = flowSetup().loggedInUser();
			avatar = new Avatar(user.getFirstNameLastName());
			avatar.addThemeVariants(AvatarVariant.LUMO_SMALL);
			avatar.setColorIndex(0);
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
		return user.canDoAction(route, action);
	}

	private void generateMenuItems(SideNav drawer, GxAuthenticatedUser user) {
		flowSetup().menuItems().forEach(mi -> {
			if (canDoAction(user, "view", mi)) {
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
					generateMenuItems(i, mi, user);
					if (i.getChildren().count() > 0)
						drawer.addItem(i);
				} else {
					drawer.addItem(i);
				}
			}
		});
	}

	@Override
	public void setContent(Component content) {
		if (rootLayout == null) {
			rootLayout = new FlexLayout();
			rootLayout.setFlexDirection(FlexDirection.ROW);
			rootLayout.setSizeFull();

			contentLayout = new GxStackLayout();
			contentLayout.addClassName("gx-content");
			contentLayout.setSizeFull();

			rightDrawerLayout = new GxStackLayout();
			rightDrawerLayout.addClassName("gx-right-drawer");
			rightDrawerLayout.setSizeFull();
			rightDrawerLayout.setVisible(false);

			rootLayout.add(contentLayout, rightDrawerLayout);
			super.setContent(rootLayout);
		}
		while (!contentLayout.isEmpty()) {
			contentLayout.pop();
		}
		while (!rightDrawerLayout.isEmpty()) {
			rightDrawerLayout.pop();
		}
		contentLayout.push(content);
	}

	Map<Component, TargetArea> targetAreaMap = new HashMap<>();

	@Subscribe
	public void showComponent(ShowComponentEvent event) {
		UI.getCurrent().access(() -> {
			Component c = event.getComponent();
			targetAreaMap.put(c, event.getArea());
			if (event.getArea() == TargetArea.END_DRAWER && !c.equals(rightDrawerLayout.top())) {
				rightDrawerLayout.push(c);
			}
			UI.getCurrent().push();
		});
	}

	@Subscribe
	public void removeComponent(RemoveComponentEvent event) {
		UI.getCurrent().access(() -> {
			Component c = event.getComponent();
			TargetArea area = targetAreaMap.get(c);
			if (area == TargetArea.END_DRAWER && c.equals(rightDrawerLayout.top())) {
				rightDrawerLayout.pop();
				targetAreaMap.remove(c);
			}
			if (area == TargetArea.CONTENT && c.equals(contentLayout.top())) {
				contentLayout.pop();
				targetAreaMap.remove(c);
			}
			UI.getCurrent().push();
		});
	}

	@Subscribe
	public void resizeComponent(ResizeComponentEvent event) {
		UI.getCurrent().access(() -> {
			Component c = event.getComponent();
			TargetArea area = targetAreaMap.get(c);
			if (area == TargetArea.CONTENT && c.equals(contentLayout.top())) {
				contentLayout.pop();
				rightDrawerLayout.push(c);
				targetAreaMap.put(c, TargetArea.END_DRAWER);
			} else if (area == TargetArea.END_DRAWER && c.equals(rightDrawerLayout.top())) {
				rightDrawerLayout.pop();
				contentLayout.push(c);
				targetAreaMap.put(c, TargetArea.CONTENT);
			}
			UI.getCurrent().push();
		});
	}

	private void generateMenuItems(SideNavItem parent, GxMenuItem pmi, GxAuthenticatedUser user) {
		pmi.getChildren().forEach(mi -> {
			if (canDoAction(user, "view", mi)) {
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
					generateMenuItems(i, mi, user);
					if (i.getChildren().count() > 0)
						parent.addItem(i);
				} else {
					parent.addItem(i);
				}
			}
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
