package io.graphenee.vaadin.flow.base;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinSession;

import io.graphenee.core.model.GxAuthenticatedUser;
import io.graphenee.vaadin.flow.security.UserSignOutEvent;
import io.graphenee.vaadin.flow.utils.DashboardUtils;

@CssImport("./styles/gx-common.css")
public abstract class GxAbstractLayoutView extends Div implements BeforeEnterObserver, AfterNavigationObserver {

	@Autowired
	ApplicationEventPublisher eventPublisher;

	private static final long serialVersionUID = 1L;
	private Tabs tabs;

	public GxAbstractLayoutView() {
		setSizeFull();
		getElement().getStyle().set("margin", "0");
		getElement().getStyle().set("padding", "0");
		addClassName("gx-layout-view");
	}

	@PostConstruct
	private void postBuild() {
		add(getCaptionComponent());
		List<GxTabItem> tabItems = new ArrayList<>();
		addTabsToView(tabItems);
		Component rootLayout = getLayoutComponent();
		if (rootLayout instanceof HasComponents) {
			decorateLayout((HasComponents) rootLayout);
			addTabs((HasComponents) rootLayout, tabItems);
			add(rootLayout);
		}
	}

	private void addTabs(HasComponents rootLayout, List<GxTabItem> tabItems) {
		if (tabItems.size() == 0)
			return;
		Div selectedTab = new Div();
		selectedTab.setSizeFull();
		selectedTab.getStyle().set("overflow-x", "hidden");
		tabs = new Tabs();
		tabs.setWidthFull();
		Component[] tabComponents = new Component[tabItems.size()];

		tabItems.sort(Comparator.comparing(GxTabItem::getIndex));
		for (int i = 0; i < tabItems.size(); i++) {
			GxTabItem tabItem = tabItems.get(i);
			Tab tab = new Tab(tabItem.getLabel());
			tabs.add(tab);
			Component component = tabItem.getComponent();
			tabComponents[i] = component;
			if (i == 0) {
				selectedTab.add(tabComponents[i]);
			}
		}

		tabs.addSelectedChangeListener(event -> {
			Integer selectedIndex = tabs.getSelectedIndex();
			Component selectedComponent = tabComponents[selectedIndex];
			selectedTab.removeAll();
			selectedTab.add(selectedComponent);
			onTabChange(tabs.getSelectedIndex(), tabs.getSelectedTab(), selectedComponent);
		});

		rootLayout.add(tabs, selectedTab);
	}

	protected void onTabChange(Integer index, Tab tab, Component component) {
	}

	protected void addTabsToView(List<GxTabItem> tabItems) {
	}

	protected abstract Component getLayoutComponent();

	protected void decorateLayout(HasComponents rootLayout) {
	}

	protected String getCaption() {
		return null;
	}

	protected Component getCaptionComponent() {
		H3 lblCaption = new H3(getCaption());
		lblCaption.getElement().getStyle().set("padding", "0");
		lblCaption.getElement().getStyle().set("font-size", "var(--lumo-font-size-xl");
		lblCaption.getElement().getStyle().set("color", "var(--lumo-primary-color");
		lblCaption.getElement().getStyle().set("margin-bottom", "0.4em");
		lblCaption.getElement().getStyle().set("margin-left", "0.85em");
		lblCaption.getElement().getStyle().set("margin-top", "1em");
		lblCaption.getElement().getStyle().set("border-bottom", "0.2em solid var(--lumo-primary-color)");
		lblCaption.getElement().getStyle().set("display", "inline-block");
		return lblCaption;
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		GxAuthenticatedUser user = VaadinSession.getCurrent().getAttribute(GxAuthenticatedUser.class);
		if (user != null) {
			String identifier = user.getUsername() + DashboardUtils.getMacAddress()
					+ VaadinRequest.getCurrent().getHeader("User-Agent").replaceAll(" ", "");
			eventPublisher.publishEvent(new UserSignOutEvent(0, identifier));
		}

		if (this.getClass().isAnnotationPresent(GxSecuredView.class)) {
			GxSecuredView annotation = this.getClass().getAnnotation(GxSecuredView.class);
			String route = null;
			if (annotation.value() != null) {
				route = annotation.value();
			} else if (this.getClass().isAnnotationPresent(Route.class)) {
				Route routeAnnotation = this.getClass().getAnnotation(Route.class);
				route = routeAnnotation.value();
			}
			if (user == null) {
				event.rerouteTo("login", route);
			} else if (!route.equals("") && !user.canDoAction(route, "view")) {
				event.rerouteTo("");
			}
		}
	}

	protected boolean isLoggedIn() {
		return VaadinSession.getCurrent().getAttribute(GxAuthenticatedUser.class) != null;
	}

	@SuppressWarnings("unchecked")
	protected <T extends GxAuthenticatedUser> T loggedInUser() {
		return (T) VaadinSession.getCurrent().getAttribute(GxAuthenticatedUser.class);
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
	}

}
