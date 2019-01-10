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
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

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
	MTextField templateName, templateCode;
	MTextField subject;
	MTextArea body, smsBody;
	MTextArea ccList;
	MTextArea bccList;
	MTextField senderEmailAddress;
	MLabel msg;

	MCheckBox isActive;

	protected VerticalLayout detailsTab;
	protected TabSheet mainTabSheet;
	final int perSMSMaxLength = 160;

	@Override
	protected void postBinding(GxEmailTemplateBean entity) {
		mainTabSheet.setSelectedTab(0);
	}

	@Override
	protected Component getFormComponent() {
		detailsTab = new MVerticalLayout().withSpacing(false);
		detailsTab.setSizeFull();

		detailsTab.addComponent(constructTemplateInfoForm());

		mainTabSheet = new TabSheet();
		mainTabSheet.setSizeFull();
		mainTabSheet.setStyleName(ValoTheme.TABSHEET_FRAMED);
		mainTabSheet.addComponent(emailTab());
		mainTabSheet.addComponent(smsTab());

		detailsTab.addComponent(mainTabSheet);
		detailsTab.setExpandRatio(mainTabSheet, 1);

		return detailsTab;

	}

	private FormLayout constructTemplateInfoForm() {
		FormLayout templateInfoForm = new MFormLayout().withStyleName(ValoTheme.FORMLAYOUT_LIGHT).withMargin(false);

		templateName = new MTextField("Name").withRequired(true);
		templateName.setMaxLength(50);
		templateName.setNullRepresentation(null);
		templateCode = new MTextField("Code").withRequired(true);
		templateCode.setMaxLength(100);
		templateCode.setNullRepresentation(null);
		isActive = new MCheckBox("Is Active?");
		templateInfoForm.addComponents(templateName, templateCode, isActive);
		return templateInfoForm;
	}

	private FormLayout emailTab() {
		FormLayout emailForm = new MFormLayout().withStyleName(ValoTheme.FORMLAYOUT_LIGHT);
		emailForm.setSizeFull();
		emailForm.setCaption("Email Message");

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

		senderEmailAddress = new MTextField("Sender");
		senderEmailAddress.setMaxLength(200);

		//		senderEmailAddress.setValue(SchoolDashboardUtils.getLoggedInSchool().getSenderEmailAddress());

		emailForm.addComponents(subject, body, ccList, bccList, senderEmailAddress);

		return emailForm;
	}

	private FormLayout smsTab() {
		FormLayout smsForm = new MFormLayout().withStyleName(ValoTheme.FORMLAYOUT_LIGHT);
		smsForm.setSizeFull();
		smsForm.setCaption("SMS Message");

		smsBody = new MTextArea("Body").withRequired(true);
		smsBody.setInputPrompt("Dear #{lastName}, \nThis is a test message.");
		smsBody.setRows(15);
		msg = new MLabel();

		smsBody.addTextChangeListener(event -> {
			Integer result = event.getText().length() / perSMSMaxLength;
			if (event.getText().length() > perSMSMaxLength) {
				if (event.getText().length() % perSMSMaxLength == 0)
					msg.setValue("The message will consume " + (result) + " no. of sms.");
				else
					msg.setValue("The message will consume " + (result + 1) + " no. of sms.");

			} else
				msg.setValue(null);
		});
		smsForm.addComponents(smsBody, msg);

		return smsForm;
	}

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected String formTitle() {
		return "Message Template";
	}

	@Override
	protected String popupHeight() {
		return "550px";
	}

}
