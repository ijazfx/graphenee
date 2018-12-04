package io.graphenee.vaadin;

import org.vaadin.viritin.fields.MPasswordField;

import com.vaadin.ui.FormLayout;

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
	protected void addFieldsToForm(FormLayout form, GxChangePasswordBean entity) {
		currentPassword = new MPasswordField("Current Password").withRequired(true);
		newPassword = new MPasswordField("New Password").withRequired(true);
		confirmNewPassword = new MPasswordField("Confirm Password").withRequired(true);
		form.addComponents(currentPassword, newPassword, confirmNewPassword);
	}

	@Override
	public String getSaveCaption() {
		return "Change";
	}

	@Override
	protected String formTitle() {
		return null;
	}

	@Override
	protected String popupHeight() {
		return "170px";
	}

	@Override
	protected String popupWidth() {
		return "400px";
	}

}
