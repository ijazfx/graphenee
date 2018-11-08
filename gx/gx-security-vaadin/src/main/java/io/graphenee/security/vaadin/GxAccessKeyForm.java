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

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.fields.MCheckBox;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.core.enums.AccessKeyType;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxAccessKeyBean;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxSecurityGroupBean;
import io.graphenee.core.model.bean.GxSecurityPolicyBean;
import io.graphenee.vaadin.TRAbstractForm;
import io.graphenee.vaadin.converter.BeanCollectionFaultToSetConverter;

@SpringComponent
@Scope("prototype")
public class GxAccessKeyForm extends TRAbstractForm<GxAccessKeyBean> {
	private static final long serialVersionUID = 1L;

	@Autowired
	GxDataService dataService;
	ComboBox accessKeyType;
	MLabel accessKey, secret;
	MCheckBox isActive;

	TwinColSelect securityGroupCollectionFault;
	TwinColSelect securityPolicyCollectionFault;

	private GxNamespaceBean namespaceBean;

	@Override
	protected Component getFormComponent() {
		MFormLayout form = new MFormLayout().withStyleName(ValoTheme.FORMLAYOUT_LIGHT).withMargin(false);
		accessKey = new MLabel().withCaption("Access Key");
		secret = new MLabel().withCaption("Secret");
		accessKeyType = new ComboBox("Key Type");
		accessKeyType.setRequired(true);
		isActive = new MCheckBox("Is Active?");

		form.addComponents(accessKey, secret, accessKeyType, isActive);

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

		TabSheet mainTabSheet = new TabSheet();
		mainTabSheet.setStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
		mainTabSheet.setWidth("100%");
		mainTabSheet.setHeight("100%");

		mainTabSheet.addTab(form, "Details");
		mainTabSheet.addTab(new MVerticalLayout(securityGroupCollectionFault).withFullHeight(), "Security Groups");
		mainTabSheet.addTab(new MVerticalLayout(securityPolicyCollectionFault).withFullHeight(), "Security Policies");

		MVerticalLayout layout = new MVerticalLayout(mainTabSheet).withMargin(false);
		layout.setSizeFull();
		return layout;
	}

	@Override
	protected void postBinding(GxAccessKeyBean entity) {
		accessKey.setValue(entity.getAccessKey().toString());
		secret.setValue(entity.getSecret().toString());
		accessKeyType.addItems(Arrays.asList(AccessKeyType.values()));
		List<GxSecurityGroupBean> securityGroups = namespaceBean != null ? dataService.findSecurityGroupByNamespace(namespaceBean) : dataService.findSecurityGroup();
		securityGroupCollectionFault.addItems(securityGroups);

		List<GxSecurityPolicyBean> securityPolicies = namespaceBean != null ? dataService.findSecurityPolicyByNamespace(namespaceBean) : dataService.findSecurityPolicy();
		securityPolicyCollectionFault.addItems(securityPolicies);
	}

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected String formTitle() {
		return "Access Key";
	}

	public void initializeWithNamespace(GxNamespaceBean namespaceBean) {
		this.namespaceBean = namespaceBean;
	}

	@Override
	protected String popupWidth() {
		return "700px";
	}

}
