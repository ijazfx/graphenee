package io.graphenee.core.vaadin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.fields.MCheckBox;
import org.vaadin.viritin.fields.MTextField;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;

import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxSupportedLocaleBean;
import io.graphenee.core.model.bean.GxTermBean;
import io.graphenee.vaadin.TRAbstractForm;

@SpringComponent
@Scope("prototype")
public class GxResourceForm extends TRAbstractForm<GxTermBean> {

	@Autowired
	GxDataService dataService;

	MTextField termKey;
	MTextField termSingular;
	MTextField termPlural;
	MCheckBox isActive;
	ComboBox oidSupportedLocale;
	ComboBox oidNamespace;

	@Override
	protected void addFieldsToForm(FormLayout form) {
		termKey = new MTextField("Term Key").withRequired(true);
		termSingular = new MTextField("Singular").withRequired(true);
		termPlural = new MTextField("Plural");
		isActive = new MCheckBox("Is Active?", true);

		BeanContainer<Integer, GxSupportedLocaleBean> supportedLocaleContainer = new BeanContainer<>(GxSupportedLocaleBean.class);
		supportedLocaleContainer.setBeanIdProperty("oid");
		supportedLocaleContainer.addAll(dataService.findSupportedLocale());
		oidSupportedLocale = new ComboBox("Supported Locale");
		oidSupportedLocale.setContainerDataSource(supportedLocaleContainer);
		oidSupportedLocale.setItemCaptionPropertyId("namespace");
		oidSupportedLocale.setRequired(true);

		BeanContainer<Integer, GxNamespaceBean> namespaceContainer = new BeanContainer<>(GxNamespaceBean.class);
		namespaceContainer.setBeanIdProperty("oid");
		namespaceContainer.addAll(dataService.findNamespace());
		oidNamespace = new ComboBox("Namespace");
		oidNamespace.setContainerDataSource(namespaceContainer);
		oidNamespace.setItemCaptionPropertyId("namespace");
		oidNamespace.setRequired(true);

		form.addComponents(oidNamespace, oidSupportedLocale, termKey, termSingular, termPlural, isActive);
	}

	@Override
	protected boolean eagerValidationEnabled() {
		return false;
	}

	@Override
	protected String formTitle() {
		return "Term";
	}

}
