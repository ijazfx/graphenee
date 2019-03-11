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
package io.graphenee.vaadin.component;

import java.util.Arrays;

import org.vaadin.viritin.fields.MPasswordField;
import org.vaadin.viritin.fields.MTextField;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;

import io.graphenee.core.enums.GenderEnum;
import io.graphenee.core.model.GxAuthenticatedUser;
import io.graphenee.vaadin.TRAbstractForm;

public class DashboardUserProfileForm extends TRAbstractForm<GxAuthenticatedUser> {

	MTextField firstName;
	MTextField lastName;
	MTextField username;
	MPasswordField password;
	ComboBox gender;

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected String formTitle() {
		return "User Profile";
	}

	@Override
	protected void addFieldsToForm(FormLayout form) {
		firstName = new MTextField("First Name");
		firstName.setEnabled(false);
		lastName = new MTextField("Last Name");
		lastName.setEnabled(false);
		username = new MTextField("Username");
		username.setEnabled(false);
		gender = new ComboBox("Gender");
		gender.addItems(Arrays.asList(GenderEnum.values()));
		password = new MPasswordField("Password");
		form.addComponents(firstName, lastName, gender, username, password);
	}

}
