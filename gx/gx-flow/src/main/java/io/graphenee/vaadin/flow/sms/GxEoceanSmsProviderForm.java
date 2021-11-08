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

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.graphenee.core.model.bean.GxSmsProviderBean;
import io.graphenee.sms.proto.GxSmsConfigProtos;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm;

public class GxEoceanSmsProviderForm extends GxAbstractEntityForm<GxSmsProviderBean> {

	public GxEoceanSmsProviderForm() {
		super(GxSmsProviderBean.class);
	}

	private static final long serialVersionUID = 1L;

	private static final Logger L = LoggerFactory.getLogger(GxEoceanSmsProviderForm.class);

	Label providerNameLabel;
	TextArea namespaceDescription;
	Checkbox isPrimary;

	GxSmsConfigProtos.EoceanConfig.Builder configBuilder;

	/*
	optional string baseUrl = 1;
	optional string user = 2;
	optional string password = 3;
	optional string responseType = 4;
	 */

	private TextField baseUrl;
	private TextField user;
	private PasswordField password;
	private TextField senderId;

	@Override
	protected void postBinding(GxSmsProviderBean entity) {
		providerNameLabel.setText(entity.getProviderName());
		try {
			configBuilder = GxSmsConfigProtos.EoceanConfig.parseFrom(entity.getConfigData()).toBuilder();
			baseUrl.setValue(configBuilder.getBaseUrl());
			user.setValue(configBuilder.getUser());
			password.setValue(configBuilder.getPassword());
			senderId.setValue(configBuilder.getSenderId());
		} catch (Exception e) {
			configBuilder = GxSmsConfigProtos.EoceanConfig.newBuilder();
			L.warn(e.getMessage(), e);
		}
	}

	@Override
	public void validateForm() throws ValidationException {
		getEntity().setConfigData(configBuilder.build().toByteArray());
	}

	@Override
	protected String formTitle() {
		return "Eocean SMS Configuration";
	}

	@Override
	protected void decorateForm(HasComponents form) {
		providerNameLabel = new Label("Provider");
		isPrimary = new Checkbox("Is Primary?");
		form.add(providerNameLabel, isPrimary);
		baseUrl = new TextField("Base URL");
		baseUrl.addValueChangeListener(event -> {
			if (isEntityBound()) {
				String value = event.getValue();
				if (value != null)
					configBuilder.setBaseUrl(value);
				else
					configBuilder.clearBaseUrl();
			}
		});
		user = new TextField("User");
		user.addValueChangeListener(event -> {
			if (isEntityBound()) {
				String value = event.getValue();
				if (value != null)
					configBuilder.setUser(value);
				else
					configBuilder.clearUser();
			}
		});

		password = new PasswordField("Password");
		password.addValueChangeListener(event -> {
			if (isEntityBound()) {
				String value = event.getValue();
				if (value != null)
					configBuilder.setPassword(value);
				else
					configBuilder.clearPassword();
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
		form.add(baseUrl, user, password, senderId);
	}

}
