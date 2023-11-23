package io.graphenee.workshop.vaadin;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.page.Push;

import io.graphenee.vaadin.flow.base.GxAbstractAppLayout;
import io.graphenee.vaadin.flow.base.GxAbstractFlowSetup;

@Push
public class MainLayout extends GxAbstractAppLayout {

	private static final long serialVersionUID = 1L;

	@Autowired
	GxAbstractFlowSetup flowSetup;

	@Override
	protected GxAbstractFlowSetup flowSetup() {
		return flowSetup;
	}

}
