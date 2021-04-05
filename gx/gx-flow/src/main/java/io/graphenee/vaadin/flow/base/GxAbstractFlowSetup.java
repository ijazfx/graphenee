package io.graphenee.vaadin.flow.base;

import java.util.List;

import com.vaadin.flow.router.RouterLayout;

public abstract class GxAbstractFlowSetup {

	public String appTitle() {
		return "Flow Application";
	}
    
    public String appVersion() {
        return "1.0";
    }

    public String defaultRoute() {
        return "";
    }

    public abstract Class<? extends RouterLayout> routerLayout();

    public abstract List<GxMenuItem> menuItems();

}
