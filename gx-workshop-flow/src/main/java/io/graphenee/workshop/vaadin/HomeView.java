package io.graphenee.workshop.vaadin;

import com.vaadin.flow.router.Route;

import io.graphenee.vaadin.flow.GxSecuredView;
import io.graphenee.vaadin.flow.GxVerticalLayoutView;

@GxSecuredView
@Route(value = "", layout = MainLayout.class)
public class HomeView extends GxVerticalLayoutView {

    private static final long serialVersionUID = 1L;

}
