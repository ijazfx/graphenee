package io.graphenee.workshop.vaadin;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.dependency.CssImport;

import io.graphenee.vaadin.flow.GxAbstractAppLayout;
import io.graphenee.vaadin.flow.GxAbstractFlowSetup;

@CssImport("./styles/app.css")
public class MainLayout extends GxAbstractAppLayout {

	private static final long serialVersionUID = 1L;

	@Autowired
	GxAbstractFlowSetup flowSetup;

	@Override
	protected GxAbstractFlowSetup flowSetup() {
		return flowSetup;
	}

}
