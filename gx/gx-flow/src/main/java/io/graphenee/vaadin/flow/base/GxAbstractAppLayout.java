package io.graphenee.vaadin.flow.base;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;

import com.github.appreciated.app.layout.component.appbar.AppBarBuilder;
import com.github.appreciated.app.layout.component.applayout.LeftLayouts;
import com.github.appreciated.app.layout.component.applayout.LeftLayouts.LeftHybrid;
import com.github.appreciated.app.layout.component.builder.AppLayoutBuilder;
import com.github.appreciated.app.layout.component.menu.left.LeftSubmenu;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftAppMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftSubMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.items.LeftNavigationItem;
import com.github.appreciated.app.layout.component.router.AppLayoutRouterLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.server.VaadinSession;

import io.graphenee.core.model.GxAuthenticatedUser;
import io.graphenee.vaadin.flow.utils.DashboardUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
@CssImport("./styles/gx-common.css")
public abstract class GxAbstractAppLayout extends AppLayoutRouterLayout<LeftLayouts.LeftHybrid> {

	private static final long serialVersionUID = 1L;

	@PostConstruct
	private void postBuild() {
		AppLayoutBuilder<LeftHybrid> builder = AppLayoutBuilder.get(LeftLayouts.LeftHybrid.class);
		builder = builder.withTitle(buildTitle());
		builder.withAppMenu(buildAppMenu());
		if (flowSetup().loggedInUser() != null) {
			builder.withAppBar(AppBarBuilder.get().add(buildProfileMenu()).build());
		}
		LeftHybrid layout = builder.build();
		init(layout);
	}

	private Component buildAppMenu() {
		LeftAppMenuBuilder builder = LeftAppMenuBuilder.get();
		List<GxMenuItem> menuItems = flowSetup().menuItems();
		if (menuItems != null && !menuItems.isEmpty()) {
			menuItems.forEach(mi -> {
				addMenuItemToAppMenu(mi, builder);
			});
		}
		return builder.build();
	}

	private void addMenuItemToAppMenu(GxMenuItem mi, LeftAppMenuBuilder builder) {
		GxAuthenticatedUser user = DashboardUtils.getLoggedInUser();
		if (mi.hasChildren()) {
			LeftSubMenuBuilder smb = LeftSubMenuBuilder.get(mi.getLabel(), mi.getIcon());

			AtomicBoolean anyMenu = new AtomicBoolean(false);
			mi.getChildren().forEach(smi -> {
				if (smi.getRoute() == null || user != null && user.canDoAction(smi.getRoute(), "view")) {
					addMenuItemToSubMenu(smi, smb);
					anyMenu.set(true);
				}
			});
			if (anyMenu.get()) {
				LeftSubmenu sm = smb.build();
				builder.add(sm);
			}
		} else {
			try {
				if (mi.getRoute() == null || user != null && user.canDoAction(mi.getRoute(), "view")) {
					LeftNavigationItem item = new LeftNavigationItem(mi.getLabel(), mi.getIcon(), mi.getComponentClass());
					builder.add(item);
				}
			} catch (Exception ex) {
				log.warn(ex.getMessage());
			}
		}
	}

	private void addMenuItemToSubMenu(GxMenuItem mi, LeftSubMenuBuilder builder) {
		GxAuthenticatedUser user = DashboardUtils.getLoggedInUser();
		if (mi.hasChildren()) {
			LeftSubMenuBuilder smb = LeftSubMenuBuilder.get(mi.getLabel(), mi.getIcon());
			mi.getChildren().forEach(smi -> {
				addMenuItemToSubMenu(smi, smb);
			});
			builder.add(smb.build());
		} else {
			try {
				if (mi.getRoute() == null || user != null && user.canDoAction(mi.getRoute(), "view")) {
					LeftNavigationItem item = new LeftNavigationItem(mi.getLabel(), mi.getIcon(), mi.getComponentClass());
					builder.add(item);
				}
			} catch (Exception ex) {
				log.warn(ex.getMessage());
			}
		}
	}

	private String buildTitle() {
		return flowSetup().appTitleWithVersion();
	}

	private Component buildProfileMenu() {
		GxAuthenticatedUser user = flowSetup().loggedInUser();

		MenuBar profileMenuBar = new MenuBar();
		profileMenuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY);
		profileMenuBar.addItem(user.getFirstNameLastName());
		profileMenuBar.addItem("|");
		profileMenuBar.addItem("Sign Out", event -> {
			VaadinSession.getCurrent().close();
		});

		return profileMenuBar;
	}

	protected abstract GxAbstractFlowSetup flowSetup();

}
