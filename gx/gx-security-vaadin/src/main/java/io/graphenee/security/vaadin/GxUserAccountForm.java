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
import org.vaadin.viritin.fields.MPasswordField;
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
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxSecurityGroupBean;
import io.graphenee.core.model.bean.GxSecurityPolicyBean;
import io.graphenee.core.model.bean.GxUserAccountBean;
import io.graphenee.vaadin.TRAbstractForm;
import io.graphenee.vaadin.converter.BeanCollectionFaultToSetConverter;

@SpringComponent
@Scope("prototype")
public class GxUserAccountForm extends TRAbstractForm<GxUserAccountBean> {

	private static final long serialVersionUID = 1L;

	@Autowired
	GxDataService dataService;

	MTextField username;
	MTextField firstName;
	MTextField lastName;
	MTextField fullNameNative;
	MTextField email;
	MPasswordField password;
	MCheckBox isLocked;
	MCheckBox isActive;
	MCheckBox isPasswordChangeRequired;

	TwinColSelect securityGroupCollectionFault;
	TwinColSelect securityPolicyCollectionFault;
	TwinColSelect accessKeyCollectionFault;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected Component getFormComponent() {
		// detail form
		MFormLayout form = new MFormLayout().withStyleName(ValoTheme.FORMLAYOUT_LIGHT).withMargin(false);
		username = new MTextField("Username").withRequired(true);
		username.setMaxLength(50);
		firstName = new MTextField("First Name").withRequired(false);
		firstName.setMaxLength(30);
		lastName = new MTextField("Last Name").withRequired(false);
		lastName.setMaxLength(30);
		fullNameNative = new MTextField("Full Name (Native)").withRequired(false);
		fullNameNative.setMaxLength(100);
		email = new MTextField("Email").withRequired(false);
		email.setMaxLength(200);
		password = new MPasswordField("Password").withRequired(false);
		password.setMaxLength(200);
		isPasswordChangeRequired = new MCheckBox("Password Change Required?");
		isLocked = new MCheckBox("Is Locked?");
		isActive = new MCheckBox("Is Active?");
		form.addComponents(username, firstName, lastName, fullNameNative, email, password, isPasswordChangeRequired, isLocked, isActive);

		// security groups
		securityGroupCollectionFault = new TwinColSelect();
		securityGroupCollectionFault.setConverter((Converter) new BeanCollectionFaultToSetConverter<GxSecurityGroupBean>());
		securityGroupCollectionFault.setSizeFull();
		securityGroupCollectionFault.setLeftColumnCaption("Available");
		securityGroupCollectionFault.setRightColumnCaption("Assigned");

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
		accessKeyCollectionFault.setRightColumnCaption("Members");

		TabSheet mainTabSheet = new TabSheet();
		mainTabSheet.setStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
		mainTabSheet.setWidth("100%");
		mainTabSheet.setHeight("100%");

		mainTabSheet.addTab(form, "Details");
		mainTabSheet.addTab(new MVerticalLayout(securityGroupCollectionFault).withFullHeight(), "Security Groups");
		mainTabSheet.addTab(new MVerticalLayout(securityPolicyCollectionFault).withFullHeight(), "Security Policies");
		mainTabSheet.addTab(new MVerticalLayout(accessKeyCollectionFault).withFullHeight(), "Access Keys");

		MVerticalLayout layout = new MVerticalLayout(mainTabSheet).withMargin(false);
		layout.setSizeFull();
		return layout;
	}

	@Override
	protected void postBinding(GxUserAccountBean entity) {
		GxNamespaceBean namespace = null;
		if (entity.getNamespaceFault() != null) {
			namespace = entity.getNamespaceFault().getBean();
		}
		List<GxSecurityGroupBean> securityGroups = namespace != null ? dataService.findSecurityGroupByNamespace(namespace) : dataService.findSecurityGroup();
		securityGroupCollectionFault.addItems(securityGroups);

		List<GxSecurityPolicyBean> securityPolicies = namespace != null ? dataService.findSecurityPolicyByNamespace(namespace) : dataService.findSecurityPolicy();
		securityPolicyCollectionFault.addItems(securityPolicies);

		List<GxAccessKeyBean> accessKeyBeans = dataService.findAccessKeyByIsActiveAndGxUserAccountIsNull(true);
		accessKeyBeans.addAll(entity.getAccessKeyCollectionFault().getBeans());
		accessKeyCollectionFault.addItems(accessKeyBeans);

	}

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected String formTitle() {
		return "User Account";
	}

	@Override
	protected String popupWidth() {
		return "700px";
	}

}
