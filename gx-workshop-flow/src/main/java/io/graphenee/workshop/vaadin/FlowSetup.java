package io.graphenee.workshop.vaadin;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

import org.springframework.stereotype.Component;

import io.graphenee.vaadin.flow.GxMenuItemFactory;
import io.graphenee.vaadin.flow.base.GxAbstractFlowSetup;
import io.graphenee.vaadin.flow.base.GxMenuItem;
import io.graphenee.vaadin.flow.doc_mgmt.GxDocumentExplorerView;

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

        items.add(GxMenuItemFactory.securityMenuItem());
        items.add(GxMenuItemFactory.setupMenuItem());
        items.add(GxMenuItem.create("Documents", VaadinIcon.FOLDER_O.create(), GxDocumentExplorerView.class));

        return items;
    }

    @Override
    public Class<? extends RouterLayout> routerLayout() {
        return MainLayout.class;
    }

}
