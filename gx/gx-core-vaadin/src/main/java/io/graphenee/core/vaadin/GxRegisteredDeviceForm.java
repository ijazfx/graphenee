package io.graphenee.core.vaadin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.fields.MCheckBox;
import org.vaadin.viritin.fields.MTextField;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.FormLayout;

import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxRegisteredDeviceBean;
import io.graphenee.vaadin.TRAbstractForm;

@SpringComponent
@Scope("prototype")
public class GxRegisteredDeviceForm extends TRAbstractForm<GxRegisteredDeviceBean> {

	private static final long serialVersionUID = 1L;

	@Autowired
	GxDataService dataService;

	MTextField systemName;
	MTextField uniqueId;

	@PropertyId("brand")
	MTextField brandName;

	MTextField ownerId;

	MCheckBox isActive;
	MCheckBox isTablet;

	@Override
	protected void addFieldsToForm(FormLayout form) {
		systemName = new MTextField("Platform Name").withRequired(true);
		uniqueId = new MTextField("Device Unique Id").withRequired(true);
		brandName = new MTextField("Brand Name").withRequired(true);
		ownerId = new MTextField("Device Owner Id").withRequired(true);
		isActive = new MCheckBox("Is Active");
		isTablet = new MCheckBox("Is Tablet");
		form.addComponents(systemName, brandName, uniqueId, ownerId, isActive, isTablet);
	}

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected String formTitle() {
		return "Device Details";
	}

	@Override
	protected String popupHeight() {
		return "300";
	}

	@Override
	protected String popupWidth() {
		return "475px";
	}
}
