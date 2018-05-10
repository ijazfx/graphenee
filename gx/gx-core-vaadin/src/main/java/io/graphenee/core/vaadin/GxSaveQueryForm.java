package io.graphenee.core.vaadin;

import org.vaadin.viritin.fields.MTextField;

import com.vaadin.ui.FormLayout;

import io.graphenee.core.model.bean.GxSavedQueryBean;
import io.graphenee.vaadin.TRAbstractForm;

@SuppressWarnings("serial")
public class GxSaveQueryForm extends TRAbstractForm<GxSavedQueryBean> {

	MTextField queryName;

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected String formTitle() {
		return "Save Query Form";
	}

	@Override
	protected void addFieldsToForm(FormLayout form) {
		queryName = new MTextField("Query Name").withRequired(true);
		form.addComponent(queryName);
	}

	@Override
	protected String popupHeight() {
		return "150px";
	}

	@Override
	protected String popupWidth() {
		return "400px";
	}

}
