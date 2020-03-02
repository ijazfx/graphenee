package io.graphenee.flow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLink;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@JsModule("./styles/shared-styles.js")
public abstract class GxAppLayout extends AppLayout {

	private static final long serialVersionUID = 1L;
	private final Tabs menu;

	public GxAppLayout() {
		setPrimarySection(Section.DRAWER);
		addToNavbar(true, new DrawerToggle());
		menu = createMenuTabs();
		addToDrawer(menu);
	}

	private Tabs createMenuTabs() {
		final Tabs tabs = new Tabs();
		tabs.setOrientation(Tabs.Orientation.VERTICAL);
		tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
		tabs.setId("tabs");
		tabs.add(getAvailableTabs());
		return tabs;
	}

	private Tab[] getAvailableTabs() {
		final List<Tab> tabs = new ArrayList<>();
		for (GxMenuItem menuItem : menuItems()) {
			tabs.add(createTab(menuItem.getCaption(), menuItem.getComponent()));
		}
		return tabs.toArray(new Tab[tabs.size()]);
	}

	private Tab createTab(String title, Class<? extends Component> viewClass) {
		return createTab(populateLink(new RouterLink(null, viewClass), title));
	}

	private Tab createTab(Component content) {
		final Tab tab = new Tab();
		tab.add(content);
		return tab;
	}

	private <T extends HasComponents> T populateLink(T a, String title) {
		a.add(title);
		return a;
	}

	@Override
	protected void afterNavigation() {
		super.afterNavigation();
		selectTab();
	}

	private void selectTab() {
		String target = RouteConfiguration.forSessionScope().getUrl(getContent().getClass());
		Optional<Component> tabToSelect = menu.getChildren().filter(tab -> {
			Component child = tab.getChildren().findFirst().get();
			return child instanceof RouterLink && ((RouterLink) child).getHref().equals(target);
		}).findFirst();
		tabToSelect.ifPresent(tab -> menu.setSelectedTab((Tab) tab));
	}

	protected abstract Collection<GxMenuItem> menuItems();

	@Getter
	@Setter
	@Builder
	public static class GxMenuItem {
		private String caption;
		private Class<? extends Component> component;
	}

}
