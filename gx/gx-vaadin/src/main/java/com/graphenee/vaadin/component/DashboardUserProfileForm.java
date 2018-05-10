package com.graphenee.vaadin.component;

import java.util.Arrays;

import org.vaadin.viritin.fields.MPasswordField;
import org.vaadin.viritin.fields.MTextField;

import com.graphenee.vaadin.TRAbstractForm;
import com.graphenee.vaadin.domain.DashboardUser;
import com.graphenee.vaadin.domain.DashboardUser.GenderEnum;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;

public class DashboardUserProfileForm extends TRAbstractForm<DashboardUser> {

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
