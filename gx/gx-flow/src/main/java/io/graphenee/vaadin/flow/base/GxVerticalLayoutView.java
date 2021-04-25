package io.graphenee.vaadin.flow.base;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class GxVerticalLayoutView extends GxAbstractLayoutView {
    
    public GxVerticalLayoutView() {
        addClassName("gx-vertical-layout-view");
    }

    private static final long serialVersionUID = 1L;

    @Override
    protected Component getLayoutComponent() {
        VerticalLayout layoutComponent = new VerticalLayout();
        layoutComponent.setSizeFull();
        layoutComponent.setMargin(false);
        layoutComponent.setPadding(false);
        layoutComponent.setSpacing(false);
        return layoutComponent;
    }

}
