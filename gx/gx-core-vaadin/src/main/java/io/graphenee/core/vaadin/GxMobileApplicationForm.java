package io.graphenee.core.vaadin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.fields.MCheckBox;
import org.vaadin.viritin.fields.MTextField;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.FormLayout;

import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxMobileApplicationBean;
import io.graphenee.vaadin.TRAbstractForm;

@SpringComponent
@Scope("prototype")
public class GxMobileApplicationForm extends TRAbstractForm<GxMobileApplicationBean> {

	private static final long serialVersionUID = 1L;

	@Autowired
	GxDataService dataService;

	MTextField applicationName;

	MCheckBox isActive;

	@Override
	protected void addFieldsToForm(FormLayout form) {
		applicationName = new MTextField("Application Name").withRequired(true);
		isActive = new MCheckBox("Is Active");

		form.addComponents(applicationName, isActive);
	}

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected String formTitle() {
		return "Mobile Application Details";
	}

	@Override
	protected String popupHeight() {
		return "200";
	}

	@Override
	protected String popupWidth() {
		return "475px";
	}

}
