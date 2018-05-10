package com.graphenee.workshop.vaadin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.graphenee.core.exception.AuthenticationFailedException;
import com.graphenee.core.vaadin.SystemView;
import com.graphenee.i18n.vaadin.LocalizationView;
import com.graphenee.security.vaadin.SecurityView;
import com.graphenee.vaadin.AbstractDashboardSetup;
import com.graphenee.vaadin.TRMenuItem;
import com.graphenee.vaadin.TRSimpleMenuItem;
import com.graphenee.vaadin.domain.DashboardUser;
import com.graphenee.vaadin.domain.MockUser;
import com.graphenee.vaadin.event.DashboardEvent.UserLoginRequestedEvent;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Image;

@Service
@UIScope
public class WorkshopDashboardSetup extends AbstractDashboardSetup {

	private ViewProvider viewProvider;

	public WorkshopDashboardSetup(ViewProvider viewProvider) {
		this.viewProvider = viewProvider;
	}

	@Override
	public String applicationTitle() {
		return "Graphenee Workshop";
	}

	@Override
	public Image applicationLogo() {
		return null;
	}

	@Override
	protected List<TRMenuItem> menuItems() {
		List<TRMenuItem> menus = new ArrayList<>();
		menus.add(TRSimpleMenuItem.createMenuItemForView(MetroStyleDashboardView.VIEW_NAME, "Home", FontAwesome.HOME));
		TRSimpleMenuItem dmMenuItem = TRSimpleMenuItem.createMenuItem("Data Maintenance", FontAwesome.WRENCH);
		dmMenuItem.addChild(TRSimpleMenuItem.createMenuItemForView(SystemView.VIEW_NAME, "System", FontAwesome.SERVER));
		dmMenuItem.addChild(TRSimpleMenuItem.createMenuItemForView(LocalizationView.VIEW_NAME, "Localization", FontAwesome.GLOBE));
		dmMenuItem.addChild(TRSimpleMenuItem.createMenuItemForView(SecurityView.VIEW_NAME, "Security", FontAwesome.USER_SECRET));
		menus.add(dmMenuItem);
		return menus;
	}

	@Override
	protected List<TRMenuItem> profileMenuItems() {
		return null;
	}

	@Override
	public String dashboardViewName() {
		return MetroStyleDashboardView.VIEW_NAME;
	}

	@Override
	public void registerViewProviders(Navigator navigator) {
		navigator.addProvider(viewProvider);
	}

	@Override
	public DashboardUser authenticate(UserLoginRequestedEvent event) throws AuthenticationFailedException {
		return new MockUser();
	}

}
