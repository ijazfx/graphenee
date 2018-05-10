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
package io.graphenee.i18n.vaadin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.MBeanFieldGroup;
import org.vaadin.viritin.fields.MCheckBox;
import org.vaadin.viritin.fields.MTextField;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;

import io.graphenee.core.model.BeanFault;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxSupportedLocaleBean;
import io.graphenee.core.model.bean.GxTermBean;
import io.graphenee.vaadin.TRAbstractForm;

@SpringComponent
@Scope("prototype")
public class GxTermForm extends TRAbstractForm<GxTermBean> {

	@Autowired
	GxDataService dataService;

	MTextField termKey;
	MTextField termSingular;
	MTextField termPlural;
	MCheckBox isActive;
	ComboBox supportedLocaleComboBox;
	ComboBox namespaceFaultComboBox;

	@Override
	protected void addFieldsToForm(FormLayout form) {
		termKey = new MTextField("Term Key").withRequired(true);
		termSingular = new MTextField("Singular").withRequired(true);
		termPlural = new MTextField("Plural");
		isActive = new MCheckBox("Is Active?", true);

		supportedLocaleComboBox = new ComboBox("Supported Locale");
		supportedLocaleComboBox.addItems(dataService.findSupportedLocale());
		supportedLocaleComboBox.setRequired(true);
		supportedLocaleComboBox.addValueChangeListener(event -> {
			GxSupportedLocaleBean supportedLocaleBean = (GxSupportedLocaleBean) event.getProperty().getValue();
			if (supportedLocaleBean != null) {
				getEntity().setSupportedLocaleFault(new BeanFault<Integer, GxSupportedLocaleBean>(supportedLocaleBean.getOid(), supportedLocaleBean));
			}
		});

		namespaceFaultComboBox = new ComboBox("Namespace");
		namespaceFaultComboBox.addItems(dataService.findNamespace());
		namespaceFaultComboBox.setRequired(true);
		namespaceFaultComboBox.addValueChangeListener(event -> {
			GxNamespaceBean namespaceBean = (GxNamespaceBean) event.getProperty().getValue();
			if (namespaceBean != null) {
				getEntity().setNamespaceFault(new BeanFault<Integer, GxNamespaceBean>(namespaceBean.getOid(), namespaceBean));
			}
		});

		form.addComponents(supportedLocaleComboBox, namespaceFaultComboBox, termKey, termSingular, termPlural, isActive);
	}

	@Override
	protected boolean eagerValidationEnabled() {
		return false;
	}

	@Override
	protected String formTitle() {
		return "Term";
	}

	@Override
	protected MBeanFieldGroup<GxTermBean> bindEntity(GxTermBean entity) {
		if (entity.getSupportedLocaleFault() != null) {
			supportedLocaleComboBox.setValue(entity.getSupportedLocaleFault().getBean());
		}
		if (entity.getNamespaceFault() != null) {
			namespaceFaultComboBox.setValue(entity.getNamespaceFault().getBean());
		}
		return super.bindEntity(entity);
	}

	@Override
	protected String popupHeight() {
		return "300px";
	}

	@Override
	protected String popupWidth() {
		return "500px";
	}

}
