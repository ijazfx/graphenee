package io.graphenee.workshop.vaadin;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.dependency.CssImport;

import io.graphenee.core.flow.security.GxUserAccountProfileForm;
import io.graphenee.core.model.jpa.repository.GxUserAccountRepository;
import io.graphenee.vaadin.flow.GxAbstractAppLayout;
import io.graphenee.vaadin.flow.GxAbstractFlowSetup;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CssImport("./styles/app.css")
public class MainLayout extends GxAbstractAppLayout {

	private static final long serialVersionUID = 1L;

	@Autowired
	GxAbstractFlowSetup flowSetup;

	@Autowired
	GxUserAccountRepository userAccountRepository;

	@Autowired
	GxUserAccountProfileForm form;

	@Override
	protected GxAbstractFlowSetup flowSetup() {
		return flowSetup;
	}

}
