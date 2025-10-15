package io.graphenee.vaadin.flow;

import java.util.List;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.router.RouterLayout;

import io.graphenee.common.GxAuthenticatedUser;
import io.graphenee.vaadin.flow.utils.DashboardUtils;

public abstract class GxAbstractFlowSetup {

	public String appTitle() {
		return "Graphenee";
	}

	public String appVersion() {
		return "1.0";
	}

	public String appTitleWithVersion() {
		return String.format("%1$s v%2$s", appTitle(), appVersion());
	}

	public String defaultRoute() {
		return "";
	}

	public GxAuthenticatedUser loggedInUser() {
		return DashboardUtils.getLoggedInUser();
	}

	public abstract Class<? extends RouterLayout> routerLayout();

	public abstract List<GxMenuItem> menuItems();

	public Image appLogo() {
		Image i = new Image();
		i.setSrc("frontend/images/graphenee.png");
		return i;
	}

}
