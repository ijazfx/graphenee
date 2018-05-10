package com.graphenee.core.vaadin;

import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.fields.MCheckBox;
import org.vaadin.viritin.fields.MTextArea;
import org.vaadin.viritin.fields.MTextField;

import com.graphenee.core.model.bean.GxNamespaceBean;
import com.graphenee.vaadin.TRAbstractForm;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.FormLayout;

@SpringComponent
@Scope("prototype")
public class GxNamespaceForm extends TRAbstractForm<GxNamespaceBean> {

	MTextField namespace;
	MTextArea namespaceDescription;
	MCheckBox isActive;

	@Override
	protected void addFieldsToForm(FormLayout form) {
		namespace = new MTextField("Namespace").withRequired(true);
		namespaceDescription = new MTextArea("Description");
		isActive = new MCheckBox("Is Active?");
		form.addComponents(namespace, namespaceDescription, isActive);
	}

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected String formTitle() {
		return "Namespace";
	}

}
