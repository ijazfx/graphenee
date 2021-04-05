package io.graphenee.vaadin.flow.base;

import java.util.List;

import javax.annotation.PostConstruct;

import com.github.appreciated.app.layout.component.applayout.LeftLayouts;
import com.github.appreciated.app.layout.component.applayout.LeftLayouts.LeftResponsiveHybrid;
import com.github.appreciated.app.layout.component.builder.AppLayoutBuilder;
import com.github.appreciated.app.layout.component.menu.left.LeftSubmenu;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftAppMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftSubMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.items.LeftClickableItem;
import com.github.appreciated.app.layout.component.router.AppLayoutRouterLayout;
import com.vaadin.flow.component.Component;

public abstract class GxAbstractAppLayout extends AppLayoutRouterLayout<LeftLayouts.LeftResponsiveHybrid> {

    private static final long serialVersionUID = 1L;

    @PostConstruct
    private void postBuild() {
        AppLayoutBuilder<LeftResponsiveHybrid> builder = AppLayoutBuilder.get(LeftLayouts.LeftResponsiveHybrid.class);
        builder = builder.withTitle(buildTitle());
        builder.withAppMenu(buildAppMenu());
        init(builder.build());
    }

    private Component buildAppMenu() {
        LeftAppMenuBuilder builder = LeftAppMenuBuilder.get();
        List<GxMenuItem> menuItems = flowSetup().menuItems();
        if (menuItems != null && !menuItems.isEmpty()) {
            menuItems.forEach(mi -> {
                addMenuItemToAppMenu(mi, builder);
            });
        }
        return builder.build();
    }

    private void addMenuItemToAppMenu(GxMenuItem mi, LeftAppMenuBuilder builder) {
        if (mi.hasChildren()) {
            LeftSubMenuBuilder smb = LeftSubMenuBuilder.get(mi.getLabel(), mi.getIcon());
            mi.getChildren().forEach(smi -> {
                addMenuItemToSubMenu(smi, smb);
            });
            LeftSubmenu sm = smb.build();
            sm.setCloseMenuOnNavigation(false);
            builder.add(sm);
        } else {
            LeftClickableItem item = new LeftClickableItem(mi.getLabel(), mi.getIcon(), cl -> {
                getUI().ifPresent(ui -> {
                    ui.navigate(mi.getRoute());
                });
            });
            builder.add(item);
        }
    }

    private void addMenuItemToSubMenu(GxMenuItem mi, LeftSubMenuBuilder builder) {
        if (mi.hasChildren()) {
            LeftSubMenuBuilder smb = LeftSubMenuBuilder.get(mi.getLabel(), mi.getIcon());
            mi.getChildren().forEach(smi -> {
                addMenuItemToSubMenu(smi, smb);
            });
            builder.add(smb.build());
        } else {
            LeftClickableItem item = new LeftClickableItem(mi.getLabel(), mi.getIcon(), cl -> {
                getUI().ifPresent(ui -> {
                    ui.navigate(mi.getRoute());
                });
            });
            builder.add(item);
        }
    }

    private String buildTitle() {
        return flowSetup().appTitle() + " - v" + flowSetup().appVersion();
    }

    protected abstract GxAbstractFlowSetup flowSetup();

}
