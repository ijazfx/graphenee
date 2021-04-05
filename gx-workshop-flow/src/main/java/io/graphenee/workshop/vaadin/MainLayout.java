package io.graphenee.workshop.vaadin;

import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.material.Material;

import org.springframework.beans.factory.annotation.Autowired;

import io.graphenee.vaadin.flow.base.GxAbstractAppLayout;
import io.graphenee.vaadin.flow.base.GxAbstractFlowSetup;

public class MainLayout extends GxAbstractAppLayout {

    private static final long serialVersionUID = 1L;

    @Autowired
    GxAbstractFlowSetup flowSetup;

    @Override
    protected GxAbstractFlowSetup flowSetup() {
        return flowSetup;
    }

}
