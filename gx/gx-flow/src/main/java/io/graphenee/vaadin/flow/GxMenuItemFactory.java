package io.graphenee.vaadin.flow;

import com.vaadin.flow.component.icon.VaadinIcon;

import io.graphenee.vaadin.flow.base.GxMenuItem;
import io.graphenee.vaadin.flow.device_mgmt.GxRegisteredDeviceListView;
import io.graphenee.vaadin.flow.namespace.GxNamespaceListView;
import io.graphenee.vaadin.flow.security.GxSecurityGroupListView;
import io.graphenee.vaadin.flow.security.GxSecurityPolicyListView;
import io.graphenee.vaadin.flow.security.GxUserAccountListView;
import io.graphenee.vaadin.flow.sms.GxSmsProviderListView;

public class GxMenuItemFactory {
	
	public static GxMenuItem securityMenuItem() {
		GxMenuItem menu = GxMenuItem.create("Security", VaadinIcon.SHIELD.create());
		menu.add(GxMenuItem.create("Users", VaadinIcon.USERS.create(), GxUserAccountListView.class));
		menu.add(GxMenuItem.create("Groups", VaadinIcon.GROUP.create(), GxSecurityGroupListView.class));
		menu.add(GxMenuItem.create("Policies", VaadinIcon.TASKS.create(), GxSecurityPolicyListView.class));
		return menu;
	}
	
	public static GxMenuItem namespaceMenuItem() {
		GxMenuItem menu = GxMenuItem.create("Namespaces", VaadinIcon.ASTERISK.create(), GxNamespaceListView.class);
		return menu;
	}
	
	public static GxMenuItem registerdDevicesMenuItem() {
		GxMenuItem menu = GxMenuItem.create("Registered Devices", VaadinIcon.MOBILE.create(), GxRegisteredDeviceListView.class);
		return menu;
	}

	public static GxMenuItem smsProvidersMenuItem() {
		GxMenuItem menu = GxMenuItem.create("SMS Providers", VaadinIcon.CHAT.create(), GxSmsProviderListView.class);
		return menu;
	}
	
	public static GxMenuItem setupMenuItem() {
		GxMenuItem menu = GxMenuItem.create("Setup", VaadinIcon.COGS.create());
		menu.add(securityMenuItem());
		menu.add(namespaceMenuItem());
		menu.add(registerdDevicesMenuItem());
		menu.add(smsProvidersMenuItem());
		return menu;
	}

}
