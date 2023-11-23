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
package io.graphenee.vaadin.flow.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationException;

import io.graphenee.core.model.bean.GxSmsProviderBean;
import io.graphenee.sms.proto.GxSmsConfigProtos;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm;

public class GxAwsSmsProviderForm extends GxAbstractEntityForm<GxSmsProviderBean> {

	private static final long serialVersionUID = 1L;

	private static final Logger L = LoggerFactory.getLogger(GxAwsSmsProviderForm.class);

	TextArea namespaceDescription;
	Checkbox isPrimary;

	GxSmsConfigProtos.AwsSmsConfig.Builder configBuilder;

	private TextField awsAccessKeyId;

	private PasswordField awsSecretKey;

	private TextField awsRegion;

	private TextField senderId;

	public GxAwsSmsProviderForm() {
		super(GxSmsProviderBean.class);
	}

	@Override
	protected void postBinding(GxSmsProviderBean entity) {
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
	public void validateForm() throws ValidationException {
		getEntity().setConfigData(configBuilder.build().toByteArray());
		super.validateForm();
	}

	@Override
	protected String formTitle() {
		return getEntity().getProviderName();
	}

	@Override
	protected void decorateForm(HasComponents form) {
		isPrimary = new Checkbox("Is Primary?");
		awsAccessKeyId = new TextField("Access Key ID");
		awsAccessKeyId.addValueChangeListener(event -> {
			if (isEntityBound()) {
				String value = event.getValue();
				if (value != null)
					configBuilder.setAwsAccessKeyId(value);
				else
					configBuilder.clearAwsAccessKeyId();
			}
		});
		awsSecretKey = new PasswordField("Secret Key");
		awsSecretKey.addValueChangeListener(event -> {
			if (isEntityBound()) {
				String value = event.getValue();
				if (value != null)
					configBuilder.setAwsSecretKey(value);
				else
					configBuilder.clearAwsSecretKey();
			}
		});
		awsRegion = new TextField("Region");
		awsRegion.addValueChangeListener(event -> {
			if (isEntityBound()) {
				String value = event.getValue();
				if (value != null)
					configBuilder.setAwsRegion(value);
				else
					configBuilder.clearAwsRegion();
			}
		});
		senderId = new TextField("Sender ID");
		senderId.addValueChangeListener(event -> {
			if (isEntityBound()) {
				String value = event.getValue();
				if (value != null)
					configBuilder.setSenderId(value);
				else
					configBuilder.clearSenderId();
			}
		});
		form.add(awsAccessKeyId, awsSecretKey, awsRegion, senderId, isPrimary);

		setColspan(isPrimary, 2);
	}

	@Override
	protected String dialogHeight() {
		return "400px";
	}

}
