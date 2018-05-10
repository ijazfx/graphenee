package com.graphenee.i18n.vaadin;

import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.fields.MCheckBox;
import org.vaadin.viritin.fields.MTextField;

import com.graphenee.core.model.bean.GxSupportedLocaleBean;
import com.graphenee.vaadin.TRAbstractForm;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.FormLayout;

@SpringComponent
@Scope("prototype")
public class GxSupportedLocaleForm extends TRAbstractForm<GxSupportedLocaleBean> {

	MTextField localeName;
	MTextField localeCode;
	MCheckBox isLeftToRight;
	MCheckBox isActive;

	@Override
	protected void addFieldsToForm(FormLayout form) {
		localeName = new MTextField("Locale Name").withRequired(true);
		localeCode = new MTextField("Locale Code").withRequired(true);
		isLeftToRight = new MCheckBox("Direction LTR?");
		isActive = new MCheckBox("Is Active?");
		form.addComponents(localeName, localeCode, isLeftToRight, isActive);
	}

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected String formTitle() {
		return "Supported Locale";
	}

	@Override
	protected String popupHeight() {
		return "250px";
	}

	@Override
	protected String popupWidth() {
		return "500px";
	}

}
