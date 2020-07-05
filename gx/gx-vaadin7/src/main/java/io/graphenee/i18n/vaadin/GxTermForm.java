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

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.fields.MCheckBox;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.core.model.BeanFault;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxSupportedLocaleBean;
import io.graphenee.core.model.bean.GxTermBean;
import io.graphenee.core.model.jpa.repository.GxSupportedLocaleRepository;
import io.graphenee.i18n.api.LocalizerService;
import io.graphenee.vaadin.TRAbstractForm;

@SpringComponent
@Scope("prototype")
public class GxTermForm extends TRAbstractForm<GxTermBean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	GxDataService dataService;

	@Autowired
	GxSupportedLocaleRepository supportedLocaleRepo;

	@Autowired
	GxTermTablePanel gxTermTablePanel;

	@Autowired
	LocalizerService localizer;

	MTextField termKey;

	MCheckBox isActive;
	ComboBox supportedLocaleComboBox;
	ComboBox namespaceFaultComboBox;

	private GxNamespaceBean namespaceBean;

	private Button saveButton;

	private ProgressBar busyIndicator = new ProgressBar();

	Map<GxSupportedLocaleBean, GxTermBean> terms;

	@SuppressWarnings({ "unchecked" })
	@Override
	protected Component getFormComponent() {
		MFormLayout termKeyForm = new MFormLayout().withStyleName(ValoTheme.FORMLAYOUT_LIGHT).withMargin(false);
		termKey = new MTextField("Term Key").withRequired(true);
		isActive = new MCheckBox("Is Active?", true);

		namespaceFaultComboBox = new ComboBox("Namespace");
		namespaceFaultComboBox.addItems(dataService.findNamespace());
		namespaceFaultComboBox.setRequired(true);
		namespaceFaultComboBox.addValueChangeListener(event -> {
			namespaceBean = (GxNamespaceBean) event.getProperty().getValue();
			if (namespaceBean != null) {
				if (!isBinding())
					getEntity().setNamespaceFault(new BeanFault<Integer, GxNamespaceBean>(namespaceBean.getOid(), namespaceBean));
			}
		});

		gxTermTablePanel.initializeWithEntity(getEntity());
		gxTermTablePanel.build().withMargin(true);

		termKeyForm.addComponents(namespaceFaultComboBox, termKey, isActive);

		MVerticalLayout layout = new MVerticalLayout();
		layout.addComponents(termKeyForm, gxTermTablePanel);

		return layout;
	}

	public void saveGxTermEntities() {
		gxTermTablePanel.availableTerms.forEach(term -> {
			term.setTermKey(termKey.getValue());
			term.setNamespaceFault(new BeanFault<Integer, GxNamespaceBean>(namespaceBean.getOid(), namespaceBean));
			term.setIsActive(isActive.getValue());
			dataService.save(term);
			localizer.invalidateTerm(term.getTermKey());
		});
	}

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected String formTitle() {
		if (getEntity().getOid() == null) {
			return "Term";
		}
		return getEntity().getTermKey();
	}

	@Override
	protected void postBinding(GxTermBean entity) {
		if (entity.getNamespaceFault() != null) {
			namespaceFaultComboBox.setValue(entity.getNamespaceFault().getBean());
		}
		gxTermTablePanel.refresh();
	}

	@Override
	protected String popupHeight() {
		return "475px";
	}

	@Override
	protected String popupWidth() {
		return "700px";
	}

	@Override
	public void setSaveButton(Button saveButton) {
		this.saveButton = saveButton;
		saveButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(Button.ClickEvent event) {
				try {
					saveButton.setEnabled(false);
					busyIndicator.setVisible(true);
					saveGxTermEntities();
					UI.getCurrent().push();
				} finally {
					saveButton.setEnabled(true);
					busyIndicator.setVisible(false);
					UI.getCurrent().push();
				}
			}
		});
		super.setSaveButton(saveButton);
	}
}
