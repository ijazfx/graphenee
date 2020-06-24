package io.graphenee.vaadin;

import org.vaadin.viritin.fields.MTextField;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;

import io.graphenee.core.enums.GenderEnum;
import io.graphenee.core.model.GxAuthenticatedUser;
import io.graphenee.vaadin.component.FileChooser;

@SuppressWarnings("serial")
public class BaseProfileForm extends TRAbstractForm<GxAuthenticatedUser> {

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
	protected void addFieldsToForm(FormLayout form) {
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

	@Override
	protected String popupWidth() {
		return "500px";
	}

	@Override
	protected String popupHeight() {
		return "400px";
	}

}
