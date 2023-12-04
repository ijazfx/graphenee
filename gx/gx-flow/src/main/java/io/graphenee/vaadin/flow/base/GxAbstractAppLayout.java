package io.graphenee.vaadin.flow.base;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
@CssImport("./styles/gx-common.css")
public abstract class GxAbstractAppLayout extends AppLayout implements RouterLayout, AppShellConfigurator {

	private static final long serialVersionUID = 1L;

	@Setter
	private GxAbstractAppLayoutDelegate delegate;

	@PostConstruct
	private void postBuild() {
		DrawerToggle toggle = new DrawerToggle();
		toggle.getStyle().set("color", "var(--lumo-base-color)");

		H1 title = new H1(flowSetup().appTitleWithVersion());
		title.getStyle().set("font-size", "var(--lumo-font-size-xl)").set("margin", "0");
		title.getStyle().set("color", "var(--lumo-base-color)");
		title.setWidthFull();

		SideNav drawer = new SideNav();
		drawer.setWidthFull();

		generateMenuItems(drawer);

		VerticalLayout drawerLayout = new VerticalLayout();
		drawerLayout.setWidthFull();
		drawerLayout.add(drawer);

		addToDrawer(drawerLayout);

		addToNavbar(toggle, title);

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

			addToNavbar(hl);
		}
	}

	private void generateMenuItems(SideNav drawer) {
		flowSetup().menuItems().forEach(mi -> {
			SideNavItem i = new SideNavItem(mi.getLabel());
			i.setPrefixComponent(mi.getIcon());
			if (mi.getRoute() != null) {
				i.setPath(mi.getRoute());
			} else if (mi.getComponentClass() != null) {
				i.setPath(mi.getComponentClass());
			}
			if (mi.hasChildren()) {
				generateMenuItems(i, mi);
			}
			drawer.addItem(i);
		});
	}

	private void generateMenuItems(SideNavItem parent, GxMenuItem pmi) {
		pmi.getChildren().forEach(mi -> {
			SideNavItem i = new SideNavItem(mi.getLabel());
			i.setPrefixComponent(mi.getIcon());
			if (mi.getRoute() != null) {
				i.setPath(mi.getRoute());
			} else if (mi.getComponentClass() != null) {
				i.setPath(mi.getComponentClass());
			}
			if (mi.hasChildren()) {
				generateMenuItems(i, mi);
			}
			parent.addItem(i);
		});
	}

	protected abstract GxAbstractFlowSetup flowSetup();

	public static interface GxAbstractAppLayoutDelegate {
		default void onLogout(UI ui) {
			ui.navigate("/");
		}
	}

}
