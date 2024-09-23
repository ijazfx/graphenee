package io.graphenee.workshop.vaadin;

import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;

import io.graphenee.vaadin.flow.GxSecuredView;
import io.graphenee.vaadin.flow.GxVerticalLayoutView;

@GxSecuredView
@Route(value = "", layout = MainLayout.class)
@UIScope
@PreserveOnRefresh
public class HomeView extends GxVerticalLayoutView {

    private static final long serialVersionUID = 1L;

}
