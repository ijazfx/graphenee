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

public class GxAwsSmsProviderForm extends TRAbstractForm<GxSmsProviderBean> {

	private static final long serialVersionUID = 1L;

	private static final Logger L = LoggerFactory.getLogger(GxAwsSmsProviderForm.class);

	MLabel providerNameLabel;
	MTextArea namespaceDescription;
	MCheckBox isPrimary;

	GxSmsConfigProtos.AwsSmsConfig.Builder configBuilder;

	private MTextField awsAccessKeyId;

	private MPasswordField awsSecretKey;

	private MTextField awsRegion;

	private MTextField senderId;

	@Override
	protected void addFieldsToForm(FormLayout form) {
		providerNameLabel = new MLabel().withCaption("Provider");
		isPrimary = new MCheckBox("Is Primary?");
		form.addComponents(providerNameLabel, isPrimary);
		awsAccessKeyId = new MTextField("Access Key ID");
		awsAccessKeyId.addTextChangeListener(event -> {
			if (!isBinding()) {
				String value = event.getText();
				if (value != null)
					configBuilder.setAwsAccessKeyId(value);
				else
					configBuilder.clearAwsAccessKeyId();
			}
		});
		awsSecretKey = new MPasswordField("Secret Key");
		awsSecretKey.addTextChangeListener(event -> {
			if (!isBinding()) {
				String value = event.getText();
				if (value != null)
					configBuilder.setAwsSecretKey(value);
				else
					configBuilder.clearAwsSecretKey();
			}
		});
		awsRegion = new MTextField("Region");
		awsRegion.addTextChangeListener(event -> {
			if (!isBinding()) {
				String value = event.getText();
				if (value != null)
					configBuilder.setAwsRegion(value);
				else
					configBuilder.clearAwsRegion();
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
		form.addComponents(awsAccessKeyId, awsSecretKey, awsRegion, senderId);
	}

	@Override
	protected void postBinding(GxSmsProviderBean entity) {
		providerNameLabel.setValue(entity.getProviderName());
		try {
			configBuilder = GxSmsConfigProtos.AwsSmsConfig.parseFrom(entity.getConfigData()).toBuilder();
			awsAccessKeyId.setValue(configBuilder.getAwsAccessKeyId());
			awsSecretKey.setValue(configBuilder.getAwsSecretKey());
			awsRegion.setValue(configBuilder.getAwsRegion());
			senderId.setValue(configBuilder.getSenderId());
		} catch (Exception e) {
			configBuilder = GxSmsConfigProtos.AwsSmsConfig.newBuilder();
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
		return "AWS SMS Configuration";
	}

}
