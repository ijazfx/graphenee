package io.graphenee.vaadin;

import org.vaadin.viritin.fields.MPasswordField;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.data.Validator;

import io.graphenee.core.model.bean.GxChangePasswordBean;

@SuppressWarnings("serial")
public class GxChangePasswordForm extends TRAbstractForm<GxChangePasswordBean> {

	private MPasswordField currentPassword;
	private MPasswordField newPassword;
	private MPasswordField confirmNewPassword;

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected void addFieldsToForm(MVerticalLayout form, GxChangePasswordBean entity) {
		currentPassword = new MPasswordField("Current Password").withRequired(true);
		newPassword = new MPasswordField("Desired Password").withRequired(true);
		confirmNewPassword = new MPasswordField("Re-type Password").withRequired(true);

		newPassword.addValueChangeListener(event -> {
			confirmNewPassword.clear();
		});

		confirmNewPassword.addValidator(new Validator() {

			@Override
			public void validate(Object value) throws InvalidValueException {
				if (value != null && !value.equals(newPassword.getValue()))
					throw new InvalidValueException("Re-typed password does not match with desired password.");
			}
		});

		form.addComponents(currentPassword, newPassword, confirmNewPassword);
	}

	@Override
	public String getSaveCaption() {
		return "Change";
	}

	@Override
	protected String formTitle() {
		return "Change Password";
	}

	@Override
	protected boolean isPopupResizable() {
		return false;
	}

}
