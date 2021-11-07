package io.graphenee.workshop.vaadin;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

import org.springframework.stereotype.Component;

import io.graphenee.vaadin.flow.base.GxAbstractFlowSetup;
import io.graphenee.vaadin.flow.base.GxMenuItem;
import io.graphenee.vaadin.flow.base.GxRegisteredDeviceListView;
import io.graphenee.vaadin.flow.security.GxSecurityGroupListView;
import io.graphenee.vaadin.flow.security.GxSecurityPolicyListView;
import io.graphenee.vaadin.flow.security.GxUserAccountListView;

@Component
@VaadinSessionScope
public class FlowSetup extends GxAbstractFlowSetup {

    @Override
    public String appTitle() {
        return "Graphenee Flow Workshop";
    }

    @Override
    public String appVersion() {
        return "1.0";
    }

    @Override
    public List<GxMenuItem> menuItems() {
        List<GxMenuItem> items = new ArrayList<>();

        GxMenuItem securityMenuItem = GxMenuItem.create("Security", VaadinIcon.SHIELD.create());
        securityMenuItem.add(GxMenuItem.create("User Accounts", VaadinIcon.USER.create(), GxUserAccountListView.class));
        securityMenuItem.add(GxMenuItem.create("Security Groups", VaadinIcon.TABLE.create(), GxSecurityGroupListView.class));
        securityMenuItem.add(GxMenuItem.create("Security Policies", VaadinIcon.TABLE.create(), GxSecurityPolicyListView.class));
        securityMenuItem.add(GxMenuItem.create("Registered Device", VaadinIcon.TABLE.create(), GxRegisteredDeviceListView.class));
        items.add(securityMenuItem);

        return items;
    }

    @Override
    public Class<? extends RouterLayout> routerLayout() {
        return MainLayout.class;
    }

}
