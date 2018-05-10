package io.graphenee.security.vaadin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.fields.MCheckBox;
import org.vaadin.viritin.fields.MTextArea;

import com.graphenee.core.model.api.GxDataService;
import com.graphenee.core.model.bean.GxSecurityPolicyDocumentBean;
import com.graphenee.vaadin.TRAbstractForm;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.FormLayout;

@SpringComponent
@Scope("prototype")
public class GxSecurityPolicyDocumentForm extends TRAbstractForm<GxSecurityPolicyDocumentBean> {

	@Autowired
	GxDataService dataService;

	MTextArea documentJson;
	MCheckBox isDefault;

	@Override
	protected void addFieldsToForm(FormLayout form) {
		documentJson = new MTextArea("Policy (JSON)").withRequired(true);
		isDefault = new MCheckBox("Is Default?");
		form.addComponents(documentJson, isDefault);
	}

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected String formTitle() {
		return "Security Policy";
	}

}
