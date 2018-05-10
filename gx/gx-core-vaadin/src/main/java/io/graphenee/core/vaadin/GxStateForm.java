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
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TwinColSelect;

import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxCityBean;
import io.graphenee.core.model.bean.GxCountryBean;
import io.graphenee.core.model.bean.GxStateBean;
import io.graphenee.vaadin.BeanFaultContainer;
import io.graphenee.vaadin.TRAbstractForm;
import io.graphenee.vaadin.component.BeanFaultComboBox;
import io.graphenee.vaadin.converter.SetToCollectionConverter;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxStateForm extends TRAbstractForm<GxStateBean> {

	@Autowired
	GxDataService gxDataService;

	@PropertyId("stateName")
	MTextField stateName;

	@PropertyId("stateCode")
	MTextField stateCode;

	@PropertyId("isActive")
	MCheckBox isActive;

	@PropertyId("cityBeans")
	TwinColSelect citiesTwinCol;

	@PropertyId("countryBeanFault")
	BeanFaultComboBox countryBeanFaultComboBox;

	@Override
	protected void addFieldsToForm(FormLayout form) {
		stateName = new MTextField("State Name").withRequired(true);
		stateCode = new MTextField("State Code").withRequired(true);
		isActive = new MCheckBox("Is Active?");

		citiesTwinCol = new TwinColSelect("Cities");
		BeanItemContainer<GxCityBean> cityBeanFaultContainer = new BeanItemContainer<>(GxCityBean.class);
		cityBeanFaultContainer.addAll(gxDataService.findCity());
		citiesTwinCol.setContainerDataSource(cityBeanFaultContainer);
		citiesTwinCol.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		citiesTwinCol.setItemCaptionPropertyId("cityName");
		citiesTwinCol.setConverter((Converter) new SetToCollectionConverter());
		citiesTwinCol.setWidth("100%");

		countryBeanFaultComboBox = new BeanFaultComboBox("Country");
		countryBeanFaultComboBox.setRequired(true);
		BeanFaultContainer<Integer, GxCountryBean> countryBeanFaultContainer = new BeanFaultContainer<>("oid");
		countryBeanFaultContainer.setBeans(gxDataService.findCountry());
		countryBeanFaultComboBox.setContainerDataSource(countryBeanFaultContainer);
		countryBeanFaultComboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		countryBeanFaultComboBox.setItemCaptionPropertyId("countryName");

		form.addComponents(stateName, stateCode, countryBeanFaultComboBox, citiesTwinCol, isActive);
	}

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected String formTitle() {
		return "State";
	}

	@Override
	protected String popupHeight() {
		return "450px";
	}

	@Override
	protected String popupWidth() {
		return "600px";
	}

}
