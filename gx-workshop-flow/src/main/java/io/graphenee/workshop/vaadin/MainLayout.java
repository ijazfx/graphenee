package io.graphenee.workshop.vaadin;

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
