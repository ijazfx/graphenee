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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.fields.MCheckBox;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxAccessKeyBean;
import io.graphenee.core.model.bean.GxSecurityGroupBean;
import io.graphenee.core.model.bean.GxSecurityPolicyBean;
import io.graphenee.core.model.bean.GxUserAccountBean;
import io.graphenee.vaadin.TRAbstractForm;
import io.graphenee.vaadin.converter.BeanCollectionFaultToSetConverter;

@SpringComponent
@Scope("prototype")
public class GxSecurityGroupForm extends TRAbstractForm<GxSecurityGroupBean> {

	private static final long serialVersionUID = 1L;

	@Autowired
	GxDataService dataService;

	// ComboBox namespaceFault;
	MTextField securityGroupName;
	MTextField securityGroupDescription;
	MTextField priority;
	MCheckBox isActive;

	TwinColSelect userAccountCollectionFault;
	TwinColSelect securityPolicyCollectionFault;
	TwinColSelect accessKeyCollectionFault;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected Component getFormComponent() {
		// detail form
		MFormLayout form = new MFormLayout().withStyleName(ValoTheme.FORMLAYOUT_LIGHT).withMargin(false);
		// namespaceFault = new ComboBox("Namespace");
		// namespaceFault.setConverter(new
		// BeanFaultToBeanConverter(GxNamespaceBean.class));
		// namespaceFault.setRequired(true);

		securityGroupName = new MTextField("Group Name").withRequired(true);
		securityGroupName.setMaxLength(50);
		securityGroupDescription = new MTextField("Group Description");
		securityGroupDescription.setMaxLength(200);
		priority = new MTextField("Priority").withRequired(true);
		isActive = new MCheckBox("Is Active?");
		// form.addComponents(namespaceFault, securityGroupName, priority,
		// isActive);
		form.addComponents(securityGroupName, securityGroupDescription, priority, isActive);

		// users
		userAccountCollectionFault = new TwinColSelect();
		userAccountCollectionFault.setConverter((Converter) new BeanCollectionFaultToSetConverter<GxUserAccountBean>());
		userAccountCollectionFault.setSizeFull();
		userAccountCollectionFault.setLeftColumnCaption("Available");
		userAccountCollectionFault.setRightColumnCaption("Members");

		// security policies
		securityPolicyCollectionFault = new TwinColSelect();
		securityPolicyCollectionFault.setConverter((Converter) new BeanCollectionFaultToSetConverter<GxSecurityPolicyBean>());
		securityPolicyCollectionFault.setSizeFull();
		securityPolicyCollectionFault.setLeftColumnCaption("Available");
		securityPolicyCollectionFault.setRightColumnCaption("Applied");

		// keys
		accessKeyCollectionFault = new TwinColSelect();
		accessKeyCollectionFault.setConverter((Converter) new BeanCollectionFaultToSetConverter<GxAccessKeyBean>());
		accessKeyCollectionFault.setSizeFull();
		accessKeyCollectionFault.setLeftColumnCaption("Available");
		accessKeyCollectionFault.setRightColumnCaption("Included");

		TabSheet mainTabSheet = new TabSheet();
		mainTabSheet.setStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
		mainTabSheet.setWidth("100%");
		mainTabSheet.setHeight("100%");

		mainTabSheet.addTab(form, "Details");
		mainTabSheet.addTab(new MVerticalLayout(userAccountCollectionFault).withFullHeight(), "Users");
		mainTabSheet.addTab(new MVerticalLayout(securityPolicyCollectionFault).withFullHeight(), "Security Policies");
		mainTabSheet.addTab(new MVerticalLayout(accessKeyCollectionFault).withFullHeight(), "Access Keys");

		MVerticalLayout layout = new MVerticalLayout(mainTabSheet).withMargin(false);
		layout.setSizeFull();
		return layout;
	}

	@Override
	protected void postBinding(GxSecurityGroupBean entity) {
		List<GxUserAccountBean> userAccounts = dataService.findUserAccountByNamespace(entity.getNamespaceFault().getBean());
		userAccountCollectionFault.addItems(userAccounts);
		List<GxSecurityPolicyBean> securityPolicies = dataService.findSecurityPolicyByNamespace(entity.getNamespaceFault().getBean());
		securityPolicyCollectionFault.addItems(securityPolicies);
		List<GxAccessKeyBean> accessKeyBeans = dataService.findAccessKeyByIsActive(true);
		accessKeyCollectionFault.addItems(accessKeyBeans);
		// namespaceFault.addItems(dataService.findNamespace());
	}

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected String formTitle() {
		return "Security Group";
	}

	@Override
	protected String popupWidth() {
		return "700px";
	}

}
