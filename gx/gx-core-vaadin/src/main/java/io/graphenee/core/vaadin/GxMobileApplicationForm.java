package io.graphenee.core.vaadin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.fields.MCheckBox;
import org.vaadin.viritin.fields.MTextField;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;

import io.graphenee.core.model.BeanFault;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxMobileApplicationBean;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.vaadin.TRAbstractForm;

@SpringComponent
@Scope("prototype")
public class GxMobileApplicationForm extends TRAbstractForm<GxMobileApplicationBean> {

	private static final long serialVersionUID = 1L;

	@Autowired
	GxDataService dataService;

	MTextField applicationName;

	MCheckBox isActive;
	ComboBox namespaceFaultComboBox;

	private GxNamespaceBean namespaceBean;

	@Override
	protected void addFieldsToForm(FormLayout form) {
		applicationName = new MTextField("Application Name").withRequired(true);
		isActive = new MCheckBox("Is Active");

		namespaceFaultComboBox = new ComboBox("Namespace");
		namespaceFaultComboBox.addItems(dataService.findNamespace());
		namespaceFaultComboBox.setRequired(true);
		namespaceFaultComboBox.addValueChangeListener(event -> {
			namespaceBean = (GxNamespaceBean) event.getProperty().getValue();
			if (namespaceBean != null) {
				if (!isBinding())
					getEntity().setGxNamespaceBeanFault(new BeanFault<Integer, GxNamespaceBean>(namespaceBean.getOid(), namespaceBean));
			}
		});

		form.addComponents(namespaceFaultComboBox, applicationName, isActive);
	}

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected void postBinding(GxMobileApplicationBean entity) {
		if (entity.getGxNamespaceBeanFault() != null) {
			namespaceFaultComboBox.setValue(entity.getGxNamespaceBeanFault().getBean());
		}
	}

	@Override
	protected String formTitle() {
		return getEntity().getApplicationName() != null ? getEntity().getApplicationName() : "Add Mobile Application";
	}

	@Override
	protected String popupHeight() {
		return "200";
	}

	@Override
	protected String popupWidth() {
		return "475px";
	}

}
