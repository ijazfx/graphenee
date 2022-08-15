package io.graphenee.vaadin;

import com.vaadin.ui.ComboBox;

import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MVerticalLayout;

import io.graphenee.core.model.GxAuthenticatedUser;
import io.graphenee.util.enums.GenderEnum;
import io.graphenee.vaadin.component.FileChooser;

@SuppressWarnings("serial")
public class BaseProfileForm<T extends GxAuthenticatedUser> extends TRAbstractForm<T> {

	MTextField firstName;

	MTextField lastName;

	MTextField username;

	ComboBox gender;

	MTextField email;

	MTextField mobileNumber;

	FileChooser profilePhoto;

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected String formTitle() {
		return "Profile";
	}

	@Override
	protected void addFieldsToForm(MVerticalLayout form) {
		firstName = new MTextField("First Name");
		lastName = new MTextField("Last Name");
		username = new MTextField("Username");
		email = new MTextField("Email");
		mobileNumber = new MTextField("Mobile Number");
		gender = new ComboBox("Sex");
		gender.addItems(GenderEnum.Male, GenderEnum.Female);

		profilePhoto = new FileChooser("Photo");

		form.addComponents(firstName, lastName, gender, username, email, mobileNumber, profilePhoto);
	}

}
