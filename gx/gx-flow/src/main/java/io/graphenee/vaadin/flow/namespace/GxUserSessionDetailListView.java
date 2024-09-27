package io.graphenee.vaadin.flow.namespace;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.router.AfterNavigationEvent;

import io.graphenee.vaadin.flow.base.GxSecuredView;
import io.graphenee.vaadin.flow.base.GxVerticalLayoutView;

@GxSecuredView(GxUserSessionDetailListView.VIEW_NAME)
public class GxUserSessionDetailListView extends GxVerticalLayoutView{
    private static final long serialVersionUID = 1L;

    public static final String VIEW_NAME = "user-session";

    @Autowired
    GxUserSessionDetailList list;

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
        return "User Sessions";
    }

}
