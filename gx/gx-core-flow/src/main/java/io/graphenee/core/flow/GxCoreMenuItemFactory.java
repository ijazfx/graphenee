package io.graphenee.core.flow;

import com.vaadin.flow.component.icon.VaadinIcon;

import io.graphenee.core.flow.device_mgmt.GxRegisteredDeviceListView;
import io.graphenee.core.flow.documents.GxDocumentExplorerView;
import io.graphenee.core.flow.domain.GxDomainListView;
import io.graphenee.core.flow.email_template.GxEmailTemplateListView;
import io.graphenee.core.flow.i18n.GxSupportedLocaleView;
import io.graphenee.core.flow.i18n.GxTermView;
import io.graphenee.core.flow.namespace.GxNamespaceListView;
import io.graphenee.core.flow.security.GxAuditLogListView;
import io.graphenee.core.flow.security.GxPasswordPolicyView;
import io.graphenee.core.flow.security.GxSecurityGroupListView;
import io.graphenee.core.flow.security.GxSecurityPolicyListView;
import io.graphenee.core.flow.security.GxUserAccountListView;
import io.graphenee.core.flow.sms.GxSmsProviderListView;
import io.graphenee.vaadin.flow.GxMenuItem;

public class GxCoreMenuItemFactory {

	public static GxMenuItem securityMenuItem() {
		GxMenuItem menu = GxMenuItem.create("Security", VaadinIcon.SHIELD.create());
		menu.add(GxMenuItem.create("Users", VaadinIcon.USERS.create(), GxUserAccountListView.class));
		menu.add(GxMenuItem.create("Groups", VaadinIcon.GROUP.create(), GxSecurityGroupListView.class));
		menu.add(GxMenuItem.create("Policies", VaadinIcon.TASKS.create(), GxSecurityPolicyListView.class));
		menu.add(GxMenuItem.create("Password Policy", VaadinIcon.PASSWORD.create(), GxPasswordPolicyView.class));
		menu.add(GxMenuItem.create("Audit Log", VaadinIcon.RECORDS.create(), GxAuditLogListView.class));
		return menu;
	}

	public static GxMenuItem domainMenuItem() {
		GxMenuItem menu = GxMenuItem.create("Domains", VaadinIcon.BROWSER.create(), GxDomainListView.class);
		return menu;
	}
	
	public static GxMenuItem namespaceMenuItem() {
		GxMenuItem menu = GxMenuItem.create("Namespaces", VaadinIcon.ASTERISK.create(), GxNamespaceListView.class);
		return menu;
	}

	public static GxMenuItem registeredDevicesMenuItem() {
		GxMenuItem menu = GxMenuItem.create("Registered Devices", VaadinIcon.MOBILE.create(), GxRegisteredDeviceListView.class);
		return menu;
	}

	public static GxMenuItem smsProvidersMenuItem() {
		GxMenuItem menu = GxMenuItem.create("SMS Providers", VaadinIcon.CHAT.create(), GxSmsProviderListView.class);
		return menu;
	}

	public static GxMenuItem messageTemplateMenuItem() {
		GxMenuItem menu = GxMenuItem.create("Message Templates", VaadinIcon.ENVELOPE_OPEN_O.create(), GxEmailTemplateListView.class);
		return menu;
	}

	public static GxMenuItem documentsMenuItem() {
		GxMenuItem menu = GxMenuItem.create("Documents", VaadinIcon.FOLDER_O.create(), GxDocumentExplorerView.class);
		return menu;
	}

	public static GxMenuItem i18nMenuItem() {
		GxMenuItem menu = GxMenuItem.create("I18n", VaadinIcon.GLOBE.create());
		menu.add(GxMenuItem.create("Languages", VaadinIcon.GLOBE.create(), GxSupportedLocaleView.class));
		menu.add(GxMenuItem.create("Translations", VaadinIcon.KEYBOARD.create(), GxTermView.class));
		return menu;
	}

	public static GxMenuItem setupMenuItem() {
		GxMenuItem menu = GxMenuItem.create("Setup", VaadinIcon.COGS.create());
		menu.add(securityMenuItem());
		menu.add(i18nMenuItem());
		menu.add(namespaceMenuItem());
		menu.add(domainMenuItem());
		menu.add(registeredDevicesMenuItem());
		menu.add(smsProvidersMenuItem());
		return menu;
	}

}
