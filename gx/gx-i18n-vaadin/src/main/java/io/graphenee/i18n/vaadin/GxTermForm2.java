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
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.data.Item;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.core.model.BeanFault;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxSupportedLocaleBean;
import io.graphenee.core.model.bean.GxTermBean;
import io.graphenee.vaadin.TRAbstractForm;  

@SpringComponent
@Scope("prototype")
public class GxTermForm2 extends TRAbstractForm<GxTermBean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	GxDataService dataService;

	MTextField termKey;
	MTextField termSingular;
	MTextField termPlural;
	TextField singularTermUrdu;
	TextField singularTermArabic;
	TextField singularTermEnglish;
	TextField singularTermChinees;
	MCheckBox isActive;
	ComboBox supportedLocaleComboBox;
	ComboBox namespaceFaultComboBox;

	private TextField pluralTermUrdu;

	private TextField pluralTermArabic;

	private TextField pluralTermEnglish;

	private TextField pluralTermChinees;
	
	@SuppressWarnings("unchecked")
	@Override
	protected Component getFormComponent() {
		
		MFormLayout termKeyForm = new MFormLayout().withStyleName(ValoTheme.FORMLAYOUT_LIGHT).withMargin(false);
		
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
			
		termKeyForm.addComponents(supportedLocaleComboBox, namespaceFaultComboBox, termKey);
		
		Table singularTable = new Table("Singular Term");
		singularTable.setSizeFull();
		singularTable.setPageLength(singularTable.size());
		
		singularTable.addContainerProperty("Urdu Term", TextField.class, null);
		singularTable.addContainerProperty("Arabic Term", TextField.class, null);
		singularTable.addContainerProperty("English Term", TextField.class, null);
		singularTable.addContainerProperty("Chinees Term", TextField.class, null);
		
		singularTermUrdu = new TextField();
		singularTermArabic = new TextField();
		singularTermEnglish = new TextField();
		singularTermChinees = new TextField();
		
		Object singularItem = singularTable.addItem();
		Item singularRow = singularTable.getItem(singularItem);
		
		singularRow.getItemProperty("Urdu Term").setValue(singularTermUrdu);
		singularRow.getItemProperty("Arabic Term").setValue(singularTermArabic);
		singularRow.getItemProperty("English Term").setValue(singularTermEnglish);
		singularRow.getItemProperty("Chinees Term").setValue(singularTermChinees);
		
		singularTermUrdu.setSizeFull();
		singularTermArabic.setSizeFull();
		singularTermEnglish.setSizeFull();
		singularTermChinees.setSizeFull();
		
		singularTermUrdu.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		singularTermArabic.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		singularTermEnglish.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		singularTermChinees.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		Table pluralTable = new Table("Plural Term");
		pluralTable.setSizeFull();
		pluralTable.setPageLength(pluralTable.size());
		
		pluralTable.addContainerProperty("Urdu Term", TextField.class, null);
		pluralTable.addContainerProperty("Arabic Term", TextField.class, null);
		pluralTable.addContainerProperty("English Term", TextField.class, null);
		pluralTable.addContainerProperty("Chinees Term", TextField.class, null);
		
		pluralTermUrdu = new TextField();
		pluralTermArabic = new TextField();
		pluralTermEnglish = new TextField();
		pluralTermChinees = new TextField();
		
		Object pluralItem = pluralTable.addItem();
		Item pluralRow = pluralTable.getItem(pluralItem);
		
		pluralRow.getItemProperty("Urdu Term").setValue(pluralTermUrdu);
		pluralRow.getItemProperty("Arabic Term").setValue(pluralTermArabic);
		pluralRow.getItemProperty("English Term").setValue(pluralTermEnglish);
		pluralRow.getItemProperty("Chinees Term").setValue(pluralTermChinees);
		
		pluralTermUrdu.setSizeFull();
		pluralTermArabic.setSizeFull();
		pluralTermEnglish.setSizeFull();
		pluralTermChinees.setSizeFull();
		
		pluralTermUrdu.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		pluralTermArabic.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		pluralTermEnglish.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		pluralTermChinees.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		MVerticalLayout layout = new MVerticalLayout(termKeyForm,singularTable, pluralTable, isActive);
		return layout;
	}
	
//	@Override
//	protected void addFieldsToForm(FormLayout form) {
//		termKey = new MTextField("Term Key").withRequired(true);
//		termSingular = new MTextField("Singular").withRequired(true);
//		termPlural = new MTextField("Plural");
//		isActive = new MCheckBox("Is Active?", true);
//		supportedLocaleComboBox = new ComboBox("Supported Locale");
//		supportedLocaleComboBox.addItems(dataService.findSupportedLocale());
//		supportedLocaleComboBox.setRequired(true);
//		supportedLocaleComboBox.addValueChangeListener(event -> {
//			GxSupportedLocaleBean supportedLocaleBean = (GxSupportedLocaleBean) event.getProperty().getValue();
//			if (supportedLocaleBean != null) {
//				getEntity().setSupportedLocaleFault(new BeanFault<Integer, GxSupportedLocaleBean>(supportedLocaleBean.getOid(), supportedLocaleBean));
//			}
//		});
//
//		namespaceFaultComboBox = new ComboBox("Namespace");
//		namespaceFaultComboBox.addItems(dataService.findNamespace());
//		namespaceFaultComboBox.setRequired(true);
//		namespaceFaultComboBox.addValueChangeListener(event -> {
//			GxNamespaceBean namespaceBean = (GxNamespaceBean) event.getProperty().getValue();
//			if (namespaceBean != null) {
//				getEntity().setNamespaceFault(new BeanFault<Integer, GxNamespaceBean>(namespaceBean.getOid(), namespaceBean));
//			}
//		});
//
//		form.addComponents(supportedLocaleComboBox, namespaceFaultComboBox, termKey, termSingular, termPlural, isActive);
//	}

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
		return "450px";
	}

	@Override
	protected String popupWidth() {
		return "700px";
	}

}
