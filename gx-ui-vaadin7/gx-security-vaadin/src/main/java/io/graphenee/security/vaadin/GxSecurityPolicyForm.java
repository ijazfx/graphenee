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
package io.graphenee.security.vaadin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MCheckBox;
import org.vaadin.viritin.fields.MTextArea;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.google.common.base.Strings;
import com.vaadin.data.util.NestedMethodProperty;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxAccessKeyBean;
import io.graphenee.core.model.bean.GxSecurityGroupBean;
import io.graphenee.core.model.bean.GxSecurityPolicyBean;
import io.graphenee.core.model.bean.GxSecurityPolicyDocumentBean;
import io.graphenee.core.model.bean.GxUserAccountBean;
import io.graphenee.gx.theme.graphenee.GrapheneeTheme;
import io.graphenee.vaadin.TRAbstractForm;
import io.graphenee.vaadin.converter.BeanCollectionFaultToSetConverter;

@SpringComponent
@Scope("prototype")
public class GxSecurityPolicyForm extends TRAbstractForm<GxSecurityPolicyBean> {

	@Autowired
	GxDataService dataService;

	// ComboBox namespaceFault;
	MTextField securityPolicyName;
	MTextField securityPolicyDescription;
	MTextField priority;
	MCheckBox isActive;
	ComboBox securityPolicyDocumentComboBox;
	MTextArea jsonDocumentTextArea;

	GxSecurityPolicyDocumentBean selectedDocumentBean;
	TwinColSelect accessKeyCollectionFault;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void addFieldsToForm(FormLayout form) {

	}

	TwinColSelect userAccountCollectionFault;
	TwinColSelect securityGroupCollectionFault;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected Component getFormComponent() {
		// detail form
		MFormLayout form = new MFormLayout().withStyleName(ValoTheme.FORMLAYOUT_LIGHT).withMargin(false);
		// namespaceFault = new ComboBox("Namespace");
		// namespaceFault.setConverter(new
		// BeanFaultToBeanConverter(GxNamespaceBean.class));
		// namespaceFault.setRequired(true);

		securityPolicyName = new MTextField("Policy Name").withRequired(true);
		securityPolicyName.setMaxLength(50);
		securityPolicyDescription = new MTextField("Policy Description");
		securityPolicyDescription.setMaxLength(200);
		priority = new MTextField("Priority").withRequired(true);
		isActive = new MCheckBox("Is Active?");

		MButton createButton = new MButton("Create").withListener(event -> {
			selectedDocumentBean = new GxSecurityPolicyDocumentBean();
			selectedDocumentBean.setIsDefault(false);
			getEntity().getSecurityPolicyDocumentCollectionFault().add(selectedDocumentBean);
			securityPolicyDocumentComboBox.addItem(selectedDocumentBean);
			securityPolicyDocumentComboBox.select(selectedDocumentBean);
			adjustSaveButtonState();
		});
		MButton cloneButton = new MButton("Clone").withListener(event -> {
			if (selectedDocumentBean.getOid() != null) {
				GxSecurityPolicyDocumentBean cloned = new GxSecurityPolicyDocumentBean();
				cloned.setIsDefault(false);
				cloned.setDocumentJson(selectedDocumentBean.getDocumentJson());
				selectedDocumentBean = cloned;
				getEntity().getSecurityPolicyDocumentCollectionFault().add(selectedDocumentBean);
				securityPolicyDocumentComboBox.addItem(selectedDocumentBean);
				securityPolicyDocumentComboBox.select(selectedDocumentBean);
				adjustSaveButtonState();
			}
		});
		MButton deleteButton = new MButton("Delete").withListener(event -> {
			if (selectedDocumentBean != null) {
				getEntity().getSecurityPolicyDocumentCollectionFault().remove(selectedDocumentBean);
				securityPolicyDocumentComboBox.removeItem(selectedDocumentBean);
				securityPolicyDocumentComboBox.select(null);
				selectedDocumentBean = null;
				adjustSaveButtonState();
			}
		});
		MButton makeDefaultButton = new MButton("Make Default").withListener(event -> {
			if (selectedDocumentBean != null) {
				List<GxSecurityPolicyDocumentBean> documents = new ArrayList<>(getEntity().getSecurityPolicyDocumentCollectionFault().getBeans());
				documents.forEach(bean -> {
					bean.setIsDefault(false);
					getEntity().getSecurityPolicyDocumentCollectionFault().update(bean);
				});
				selectedDocumentBean.setIsDefault(true);
				event.getButton().setEnabled(false);
			}
		});

		securityPolicyDocumentComboBox = new ComboBox();
		securityPolicyDocumentComboBox.setWidth("170px");
		createButton.setEnabled(true);
		cloneButton.setEnabled(false);
		makeDefaultButton.setEnabled(false);
		deleteButton.setEnabled(false);
		securityPolicyDocumentComboBox.addValueChangeListener(event -> {
			selectedDocumentBean = (GxSecurityPolicyDocumentBean) securityPolicyDocumentComboBox.getValue();
			if (selectedDocumentBean != null) {
				makeDefaultButton.setEnabled(!selectedDocumentBean.getIsDefault());
				deleteButton.setEnabled(true);
				if (selectedDocumentBean.getOid() != null) {
					cloneButton.setEnabled(true);
				} else {
					cloneButton.setEnabled(false);
				}
				jsonDocumentTextArea.setEnabled(true);
				jsonDocumentTextArea.setRequired(true);
				jsonDocumentTextArea.setValue(selectedDocumentBean.getDocumentJson());
				jsonDocumentTextArea.discard();
				jsonDocumentTextArea.setPropertyDataSource(new NestedMethodProperty<>(selectedDocumentBean, "documentJson"));
				jsonDocumentTextArea.focus();
			} else {
				makeDefaultButton.setEnabled(false);
				cloneButton.setEnabled(false);
				deleteButton.setEnabled(false);
				jsonDocumentTextArea.setEnabled(false);
				jsonDocumentTextArea.discard();
				jsonDocumentTextArea.setPropertyDataSource(null);
				jsonDocumentTextArea.clear();
			}
		});

		CssLayout documentLayout = new CssLayout(securityPolicyDocumentComboBox, createButton, cloneButton, makeDefaultButton, deleteButton);
		documentLayout.setCaption("Policy Document");
		documentLayout.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		jsonDocumentTextArea = new MTextArea("Statements").withRows(15);
		jsonDocumentTextArea.addStyleName(GrapheneeTheme.STYLE_CODE);
		jsonDocumentTextArea.setInputPrompt("e.g.\ngrant all on all\nrevoke write on accounts");
		jsonDocumentTextArea.setEnabled(false);
		jsonDocumentTextArea.addValueChangeListener(event -> {
			if (selectedDocumentBean != null) {
				getEntity().getSecurityPolicyDocumentCollectionFault().update(selectedDocumentBean);
			}
			adjustSaveButtonState();
		});

		// form.addComponents(namespaceFault, securityPolicyName, priority,
		// isActive, documentLayout, jsonDocumentTextArea);
		form.addComponents(securityPolicyName, securityPolicyDescription, priority, isActive, documentLayout, jsonDocumentTextArea);

		// users
		userAccountCollectionFault = new TwinColSelect();
		userAccountCollectionFault.setConverter((Converter) new BeanCollectionFaultToSetConverter<GxUserAccountBean>());
		userAccountCollectionFault.setSizeFull();
		userAccountCollectionFault.setLeftColumnCaption("Available");
		userAccountCollectionFault.setRightColumnCaption("Applied To");

		// security policies
		securityGroupCollectionFault = new TwinColSelect();
		securityGroupCollectionFault.setConverter((Converter) new BeanCollectionFaultToSetConverter<GxSecurityPolicyBean>());
		securityGroupCollectionFault.setSizeFull();
		securityGroupCollectionFault.setLeftColumnCaption("Available");
		securityGroupCollectionFault.setRightColumnCaption("Applied To");

		// keys
		accessKeyCollectionFault = new TwinColSelect();
		accessKeyCollectionFault.setConverter((Converter) new BeanCollectionFaultToSetConverter<GxAccessKeyBean>());
		accessKeyCollectionFault.setSizeFull();
		accessKeyCollectionFault.setLeftColumnCaption("Available");
		accessKeyCollectionFault.setRightColumnCaption("Applied To");

		TabSheet mainTabSheet = new TabSheet();
		mainTabSheet.setStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
		mainTabSheet.setWidth("100%");
		mainTabSheet.setHeight("100%");

		mainTabSheet.addTab(form, "Details");
		mainTabSheet.addTab(new MVerticalLayout(userAccountCollectionFault).withFullHeight(), "Users");
		mainTabSheet.addTab(new MVerticalLayout(securityGroupCollectionFault).withFullHeight(), "Security Groups");
		mainTabSheet.addTab(new MVerticalLayout(accessKeyCollectionFault).withFullHeight(), "Access Keys");

		MVerticalLayout layout = new MVerticalLayout(mainTabSheet).withMargin(false);
		layout.setSizeFull();
		return layout;
	}

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected String formTitle() {
		return "Security Policy";
	}

	@Override
	protected void postBinding(GxSecurityPolicyBean entity) {
		List<GxUserAccountBean> userAccounts = dataService.findUserAccountByNamespace(entity.getNamespaceFault().getBean());
		userAccountCollectionFault.addItems(userAccounts);
		List<GxSecurityGroupBean> securityGroups = dataService.findSecurityGroupByNamespace(entity.getNamespaceFault().getBean());
		securityGroupCollectionFault.addItems(securityGroups);
		// namespaceFault.addItems(dataService.findNamespace());

		securityPolicyDocumentComboBox.clear();
		jsonDocumentTextArea.clear();
		securityPolicyDocumentComboBox.addItems(entity.getSecurityPolicyDocumentCollectionFault().getBeans());
		securityPolicyDocumentComboBox.select(entity.getDefaultSecurityPolicyDocumentBean());

		List<GxAccessKeyBean> accessKeyBeans = dataService.findAccessKeyByIsActive(true);
		accessKeyCollectionFault.addItems(accessKeyBeans);
	}

	@Override
	protected String popupWidth() {
		return "700px";
	}

	@Override
	public boolean isValid() {
		if (selectedDocumentBean != null && Strings.isNullOrEmpty(selectedDocumentBean.getDocumentJson())) {
			return false;
		}
		return super.isValid();
	}

}
