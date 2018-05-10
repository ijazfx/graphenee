package io.graphenee.core.vaadin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.fields.MCheckBox;
import org.vaadin.viritin.fields.MTextField;

import com.graphenee.core.model.BeanFault;
import com.graphenee.core.model.api.GxDataService;
import com.graphenee.core.model.bean.GxCityBean;
import com.graphenee.core.model.bean.GxCountryBean;
import com.graphenee.core.model.bean.GxStateBean;
import com.graphenee.vaadin.BeanFaultContainer;
import com.graphenee.vaadin.TRAbstractForm;
import com.graphenee.vaadin.component.BeanFaultComboBox;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.FormLayout;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxCityForm extends TRAbstractForm<GxCityBean> {

	@Autowired
	GxDataService gxDataService;

	@PropertyId("cityName")
	MTextField cityName;

	@PropertyId("cityCode")
	MTextField cityCode;

	@PropertyId("isActive")
	MCheckBox isActive;

	@PropertyId("countryBeanFault")
	BeanFaultComboBox countryBeanFaultComboBox;

	@PropertyId("stateBeanFault")
	BeanFaultComboBox stateBeanFaultComboBox;

	@Override
	protected void addFieldsToForm(FormLayout form) {
		cityName = new MTextField("City Name").withRequired(true);
		isActive = new MCheckBox("Is Active?");

		countryBeanFaultComboBox = new BeanFaultComboBox("Country");
		countryBeanFaultComboBox.setRequired(true);
		BeanFaultContainer<Integer, GxCountryBean> countryBeanFaultContainer = new BeanFaultContainer<>("oid");
		countryBeanFaultContainer.setBeans(gxDataService.findCountry());
		countryBeanFaultComboBox.setContainerDataSource(countryBeanFaultContainer);
		countryBeanFaultComboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		countryBeanFaultComboBox.setItemCaptionPropertyId("countryName");

		stateBeanFaultComboBox = new BeanFaultComboBox("State");
		BeanFaultContainer<Integer, GxStateBean> stateBeanFaultContainer = new BeanFaultContainer<>("oid");
		stateBeanFaultContainer.setBeans(gxDataService.findState());
		stateBeanFaultComboBox.setContainerDataSource(stateBeanFaultContainer);
		stateBeanFaultComboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		stateBeanFaultComboBox.setItemCaptionPropertyId("stateName");
		countryBeanFaultComboBox.addValueChangeListener(event -> {
			BeanFault<Integer, GxCountryBean> selectedCountry = (BeanFault<Integer, GxCountryBean>) countryBeanFaultComboBox.getValue();
			stateBeanFaultComboBox.setValue(null);
			stateBeanFaultContainer.removeAllItems();
			if (selectedCountry != null) {
				stateBeanFaultContainer.setBeans(gxDataService.findStateByCountry(selectedCountry.getOid()));
			} else {
				stateBeanFaultContainer.setBeans(gxDataService.findState());
			}
		});

		form.addComponents(cityName, countryBeanFaultComboBox, stateBeanFaultComboBox, isActive);
	}

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected String formTitle() {
		return "City";
	}

	@Override
	protected String popupHeight() {
		return "250px";
	}

	@Override
	protected String popupWidth() {
		return "450px";
	}

}
