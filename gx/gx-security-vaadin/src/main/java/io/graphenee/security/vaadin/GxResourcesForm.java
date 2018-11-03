package io.graphenee.security.vaadin;

import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.fields.MCheckBox;
import org.vaadin.viritin.fields.MTextField;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.FormLayout;

import io.graphenee.core.model.bean.GxResourceBean;
import io.graphenee.vaadin.TRAbstractForm;

@SpringComponent
@Scope("prototype")
public class GxResourcesForm extends TRAbstractForm<GxResourceBean> {

	MTextField resourceName, resourceDescription;
	MCheckBox isActive;

	@Override
	protected void addFieldsToForm(FormLayout form) {
		resourceName = new MTextField("resource name");
		resourceName.setRequired(true);
		resourceDescription = new MTextField("resource description");
		isActive = new MCheckBox("Is Active?", true);
		form.addComponents(resourceName, resourceDescription, isActive);
	}

	@Override
	protected boolean eagerValidationEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected String formTitle() {
		// TODO Auto-generated method stub
		return "Resource Form";
	}

}
