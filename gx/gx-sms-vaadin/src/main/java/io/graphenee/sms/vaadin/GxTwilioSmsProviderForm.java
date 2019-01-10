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
package io.graphenee.sms.vaadin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.fields.MCheckBox;
import org.vaadin.viritin.fields.MPasswordField;
import org.vaadin.viritin.fields.MTextArea;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.label.MLabel;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;

import io.graphenee.core.model.bean.GxSmsProviderBean;
import io.graphenee.sms.proto.GxSmsConfigProtos;
import io.graphenee.vaadin.TRAbstractForm;

public class GxTwilioSmsProviderForm extends TRAbstractForm<GxSmsProviderBean> {

	private static final long serialVersionUID = 1L;

	private static final Logger L = LoggerFactory.getLogger(GxTwilioSmsProviderForm.class);

	MLabel providerNameLabel;
	MTextArea namespaceDescription;
	MCheckBox isPrimary;

	GxSmsConfigProtos.TwilioConfig.Builder configBuilder;

	private MTextField senderId;
	private MTextField accountSid;
	private MPasswordField authToken;

	@Override
	protected void addFieldsToForm(FormLayout form) {
		providerNameLabel = new MLabel().withCaption("Provider");
		isPrimary = new MCheckBox("Is Primary?");
		form.addComponents(providerNameLabel, isPrimary);
		accountSid = new MTextField("Account SID");
		accountSid.addTextChangeListener(event -> {
			if (!isBinding()) {
				String value = event.getText();
				if (value != null)
					configBuilder.setAccountSid(value);
				else
					configBuilder.clearAccountSid();
			}
		});
		authToken = new MPasswordField("Auth Token");
		authToken.addTextChangeListener(event -> {
			if (!isBinding()) {
				String value = event.getText();
				if (value != null)
					configBuilder.setAuthToken(value);
				else
					configBuilder.clearAuthToken();
			}
		});
		senderId = new MTextField("Sender ID");
		senderId.addTextChangeListener(event -> {
			if (!isBinding()) {
				String value = event.getText();
				if (value != null)
					configBuilder.setSenderId(value);
				else
					configBuilder.clearSenderId();
			}
		});
		form.addComponents(accountSid, authToken, senderId);
	}

	@Override
	protected void postBinding(GxSmsProviderBean entity) {
		providerNameLabel.setValue(entity.getProviderName());
		try {
			configBuilder = GxSmsConfigProtos.TwilioConfig.parseFrom(entity.getConfigData()).toBuilder();
			accountSid.setValue(configBuilder.getAccountSid());
			authToken.setValue(configBuilder.getAuthToken());
			senderId.setValue(configBuilder.getSenderId());
		} catch (Exception e) {
			configBuilder = GxSmsConfigProtos.TwilioConfig.newBuilder();
			L.warn(e.getMessage(), e);
		}
	}

	@Override
	protected void save(ClickEvent e) {
		getEntity().setConfigData(configBuilder.build().toByteArray());
		super.save(e);
	}

	@Override
	protected String popupHeight() {
		return "300px";
	}

	@Override
	protected String popupWidth() {
		return "500px";
	}

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected String formTitle() {
		return "Twilio SMS Configuration";
	}

}
