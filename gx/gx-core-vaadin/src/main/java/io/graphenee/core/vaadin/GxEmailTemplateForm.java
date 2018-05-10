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
package io.graphenee.core.vaadin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.fields.MCheckBox;
import org.vaadin.viritin.fields.MTextArea;
import org.vaadin.viritin.fields.MTextField;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.FormLayout;

import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxEmailTemplateBean;
import io.graphenee.vaadin.TRAbstractForm;
import io.graphenee.vaadin.component.BeanFaultComboBox;

@SpringComponent
@Scope("prototype")
public class GxEmailTemplateForm extends TRAbstractForm<GxEmailTemplateBean> {

	@Autowired
	GxDataService gxDataService;

	BeanFaultComboBox namespaceBeanFault;
	MTextField templateName;
	MTextField subject;
	MTextArea body;
	MTextArea ccList;
	MTextArea bccList;

	MCheckBox isActive;

	@Override
	protected void addFieldsToForm(FormLayout form) {
		// namespaceBeanFault = new BeanFaultComboBox("Namespace");
		// namespaceBeanFault.setItemCaptionPropertyId("namespace");
		// BeanFaultContainer<Integer, GxNamespaceBean> namespaceContainer = new
		// BeanFaultContainer<>("oid");
		// namespaceContainer.setBeans(gxDataService.findNamespace());
		// namespaceBeanFault.setContainerDataSource(namespaceContainer);
		// namespaceBeanFault.setRequired(true);

		templateName = new MTextField("Name").withRequired(true);
		templateName.setMaxLength(50);

		subject = new MTextField("Subject").withRequired(true);
		subject.setMaxLength(500);

		body = new MTextArea("Body").withRequired(true);
		body.setInputPrompt("Dear #{lastName}, \nThis is a test message.");
		body.setRows(10);

		ccList = new MTextArea("CC To").withRequired(false);
		ccList.setInputPrompt("Separate email addresses with , or ;");
		ccList.setRows(2);
		ccList.setMaxLength(500);

		bccList = new MTextArea("BCC To").withRequired(false);
		bccList.setInputPrompt("Separate email addresses with , or ;");
		bccList.setRows(2);
		bccList.setMaxLength(500);

		isActive = new MCheckBox("Is Active?");

		// form.addComponents(namespaceBeanFault, templateName, subject, body,
		// ccList, bccList, isActive);
		form.addComponents(templateName, subject, body, ccList, bccList, isActive);
	}

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected String formTitle() {
		return "Email Template";
	}

}
