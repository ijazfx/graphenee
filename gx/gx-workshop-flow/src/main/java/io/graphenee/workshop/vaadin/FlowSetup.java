package io.graphenee.workshop.vaadin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.flow.GxCoreMenuItemFactory;
import io.graphenee.vaadin.flow.GxAbstractFlowSetup;
import io.graphenee.vaadin.flow.GxMenuItem;

@SpringComponent
@Scope("prototype")
public class FlowSetup extends GxAbstractFlowSetup {

    @Override
    public List<GxMenuItem> menuItems() {
        List<GxMenuItem> items = new ArrayList<>();

        items.add(GxCoreMenuItemFactory.setupMenuItem());
        items.add(GxCoreMenuItemFactory.documentsMenuItem());
        items.add(GxCoreMenuItemFactory.messageTemplateMenuItem());

        return items;
    }

    @Override
    public Class<? extends RouterLayout> routerLayout() {
        return MainLayout.class;
    }

    @Override
    public String appTitle() {
        return "My First Graphenee Application";
    }

    @Override
    public String appVersion() {
        return "1.0";
    }

    @Override
    public Image appLogo() {
        return new Image("frontend/images/graphenee.png", "logo");
    }

}
