package io.graphenee.vaadin.flow.base;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.RouterLayout;

import jakarta.annotation.PostConstruct;

@Push
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
@CssImport("./styles/gx-common.css")
public abstract class GxAbstractAppLayout extends AppLayout implements RouterLayout, AppShellConfigurator {

	private static final long serialVersionUID = 1L;

	@PostConstruct
	private void postBuild() {
		DrawerToggle toggle = new DrawerToggle();

		H1 title = new H1(flowSetup().appTitleWithVersion());
		title.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");

		SideNav drawer = new SideNav();

		generateMenuItems(drawer);

		addToDrawer(drawer);
		addToNavbar(toggle, title);
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

}
