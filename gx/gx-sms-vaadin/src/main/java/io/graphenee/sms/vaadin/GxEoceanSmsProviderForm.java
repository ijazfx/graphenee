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

public class GxEoceanSmsProviderForm extends TRAbstractForm<GxSmsProviderBean> {

	private static final long serialVersionUID = 1L;

	private static final Logger L = LoggerFactory.getLogger(GxEoceanSmsProviderForm.class);

	MLabel providerNameLabel;
	MTextArea namespaceDescription;
	MCheckBox isPrimary;

	GxSmsConfigProtos.EoceanConfig.Builder configBuilder;

	/*
	optional string baseUrl = 1;
	optional string user = 2;
	optional string password = 3;
	optional string responseType = 4;
	 */

	private MTextField baseUrl;
	private MTextField user;
	private MPasswordField password;

	@Override
	protected void addFieldsToForm(FormLayout form) {
		providerNameLabel = new MLabel().withCaption("Provider");
		isPrimary = new MCheckBox("Is Primary?");
		form.addComponents(providerNameLabel, isPrimary);
		baseUrl = new MTextField("Base URL");
		baseUrl.addValueChangeListener(event -> {
			if (!isBinding()) {
				Object value = event.getProperty().getValue();
				if (value != null)
					configBuilder.setBaseUrl(value.toString());
				else
					configBuilder.clearBaseUrl();
			}
		});
		user = new MTextField("User");
		user.addValueChangeListener(event -> {
			if (!isBinding()) {
				Object value = event.getProperty().getValue();
				if (value != null)
					configBuilder.setUser(value.toString());
				else
					configBuilder.clearUser();
			}
		});

		password = new MPasswordField("Password");
		password.addValueChangeListener(event -> {
			if (!isBinding()) {
				Object value = event.getProperty().getValue();
				if (value != null)
					configBuilder.setPassword(value.toString());
				else
					configBuilder.clearPassword();
			}
		});
		form.addComponents(baseUrl, user, password);
	}

	@Override
	protected void postBinding(GxSmsProviderBean entity) {
		providerNameLabel.setValue(entity.getProviderName());
		try {
			configBuilder = GxSmsConfigProtos.EoceanConfig.parseFrom(entity.getConfigData()).toBuilder();
			baseUrl.setValue(configBuilder.getBaseUrl());
			user.setValue(configBuilder.getUser());
			password.setValue(configBuilder.getPassword());
		} catch (Exception e) {
			configBuilder = GxSmsConfigProtos.EoceanConfig.newBuilder();
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
		return "250px";
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
		return "Eocean SMS Configuration";
	}

}
