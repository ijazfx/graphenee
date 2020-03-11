package io.graphenee.flow;

import java.util.ArrayList;
import java.util.Collection;

import com.github.appreciated.app.layout.addons.notification.DefaultNotificationHolder;
import com.github.appreciated.app.layout.addons.notification.component.NotificationButton;
import com.github.appreciated.app.layout.addons.notification.entity.DefaultNotification;
import com.github.appreciated.app.layout.component.appbar.AppBarBuilder;
import com.github.appreciated.app.layout.component.applayout.LeftLayouts;
import com.github.appreciated.app.layout.component.builder.AppLayoutBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftAppMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftSubMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.items.LeftNavigationItem;
import com.github.appreciated.app.layout.component.router.AppLayoutRouterLayout;
import com.github.appreciated.app.layout.entity.DefaultBadgeHolder;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@JsModule("./styles/shared-styles.js")
public abstract class GxMaterialAppLayout extends AppLayoutRouterLayout<LeftLayouts.LeftResponsiveHybrid> {

	private static final long serialVersionUID = 1L;

	private DefaultNotificationHolder notifications;
	private DefaultBadgeHolder badge;

	public GxMaterialAppLayout() {
		notifications = new DefaultNotificationHolder(newStatus -> {
		});
		badge = new DefaultBadgeHolder(5);
		for (int i = 1; i < 6; i++) {
			notifications.add(new DefaultNotification("Test title" + i, "A rather long test description ..............." + i));
		}

		init(AppLayoutBuilder.get(LeftLayouts.LeftResponsiveHybrid.class).withTitle(createAppBarTitle()).withAppBar(createAppBar()).withAppMenu(createAppMenu()).build());
	}

	protected Component createAppMenu() {
		LeftAppMenuBuilder mb = LeftAppMenuBuilder.get();
		// create header
		// create menu using menuItems()...
		for (GxMenuItem mi : menuItems()) {
			mb.add(createMenuItem(mi));
		}
		// create footer
		return mb.build();
	}

	private Component createMenuItem(GxMenuItem mi) {
		if (mi.getChildren() != null && mi.getChildren().size() > 0) {
			return createSubMenu(mi);
		}
		return new LeftNavigationItem(mi.getCaption(), mi.getIcon(), mi.getComponent());
	}

	private Component createSubMenu(GxMenuItem mi) {
		LeftSubMenuBuilder mb = LeftSubMenuBuilder.get(mi.getCaption(), mi.getIcon());
		for (GxMenuItem child : mi.getChildren()) {
			mb.add(createMenuItem(child));
		}
		return mb.build();
	}

	private Component createAppBar() {
		return AppBarBuilder.get().add(new NotificationButton<>(VaadinIcon.BELL, notifications)).build();
	}

	private Component createAppBarTitle() {
		return new Label(appBarTitle());
	}

	protected String appBarTitle() {
		return "App Bar";
	}

	protected abstract Collection<GxMenuItem> menuItems();

	@Getter
	@Setter
	@Builder
	public static class GxMenuItem {
		private String caption;
		private Icon icon;
		private Class<? extends Component> component;
		private GxMenuItem parent;
		private Collection<GxMenuItem> children;

		public void add(GxMenuItem... child) {
			if (children == null) {
				synchronized (this) {
					if (children == null) {
						children = new ArrayList<>();
					}
				}
			}
			for (GxMenuItem mi : child) {
				mi.setParent(this);
				children.add(mi);
			}
		}

		public GxMenuItem addChild(String caption, Icon icon, Class<? extends Component> component) {
			GxMenuItem child = GxMenuItem.builder().caption(caption).icon(icon).component(component).build();
			add(child);
			return this;
		}

	}

}
