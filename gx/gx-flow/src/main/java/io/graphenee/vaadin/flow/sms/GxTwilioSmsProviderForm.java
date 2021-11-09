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
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.graphenee.core.model.bean.GxSmsProviderBean;
import io.graphenee.sms.proto.GxSmsConfigProtos;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm;

public class GxTwilioSmsProviderForm extends GxAbstractEntityForm<GxSmsProviderBean> {

	public GxTwilioSmsProviderForm() {
		super(GxSmsProviderBean.class);
	}

	private static final long serialVersionUID = 1L;

	private static final Logger L = LoggerFactory.getLogger(GxTwilioSmsProviderForm.class);

	TextArea namespaceDescription;
	Checkbox isPrimary;

	GxSmsConfigProtos.TwilioConfig.Builder configBuilder;

	private TextField senderId;
	private TextField accountSid;
	private PasswordField authToken;

	@Override
	protected void postBinding(GxSmsProviderBean entity) {
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
	public void validateForm() throws ValidationException {
		getEntity().setConfigData(configBuilder.build().toByteArray());
	}

	@Override
	protected String formTitle() {
		return getEntity().getProviderName();
	}

	@Override
	protected void decorateForm(HasComponents form) {
		isPrimary = new Checkbox("Is Primary?");
		accountSid = new TextField("Account SID");
		accountSid.addValueChangeListener(event -> {
			if (isEntityBound()) {
				String value = event.getValue();
				if (value != null)
					configBuilder.setAccountSid(value);
				else
					configBuilder.clearAccountSid();
			}
		});
		authToken = new PasswordField("Auth Token");
		authToken.addValueChangeListener(event -> {
			if (isEntityBound()) {
				String value = event.getValue();
				if (value != null)
					configBuilder.setAuthToken(value);
				else
					configBuilder.clearAuthToken();
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
		form.add(accountSid, authToken, senderId, isPrimary);
		
		setColspan(isPrimary, 2);
	}

	@Override
	protected String dialogHeight() {
		return "400px";
	}

}
