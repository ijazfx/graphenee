package io.graphenee.vaadin.flow.security;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.router.AfterNavigationEvent;

import org.springframework.beans.factory.annotation.Autowired;

import io.graphenee.vaadin.flow.base.GxVerticalLayoutView;

public class GxSecurityGroupListView extends GxVerticalLayoutView {
    private static final long serialVersionUID = 1L;

    public static final String VIEW_NAME = "security-groups";

    @Autowired
    GxSecurityGroupList list;

    @Override
    protected void decorateLayout(HasComponents rootLayout) {
        rootLayout.add(list);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        list.refresh();
    }

    @Override
    protected String getCaption() {
        return "Security Groups";
    }

}