package com.graphenee.i18n.vaadin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.MBeanFieldGroup;
import org.vaadin.viritin.fields.MCheckBox;
import org.vaadin.viritin.fields.MTextField;

import com.graphenee.core.model.BeanFault;
import com.graphenee.core.model.api.GxDataService;
import com.graphenee.core.model.bean.GxNamespaceBean;
import com.graphenee.core.model.bean.GxSupportedLocaleBean;
import com.graphenee.core.model.bean.GxTermBean;
import com.graphenee.vaadin.TRAbstractForm;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;

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
