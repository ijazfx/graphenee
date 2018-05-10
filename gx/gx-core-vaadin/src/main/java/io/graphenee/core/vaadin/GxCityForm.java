/*******************************************************************************
 * Copyright (c) 2016, 2018 Farrukh Ijaz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package io.graphenee.core.vaadin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.fields.MCheckBox;
import org.vaadin.viritin.fields.MTextField;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.FormLayout;

import io.graphenee.core.model.BeanFault;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxCityBean;
import io.graphenee.core.model.bean.GxCountryBean;
import io.graphenee.core.model.bean.GxStateBean;
import io.graphenee.vaadin.BeanFaultContainer;
import io.graphenee.vaadin.TRAbstractForm;
import io.graphenee.vaadin.component.BeanFaultComboBox;

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
