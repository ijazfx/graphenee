package io.graphenee.security.vaadin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.fields.MCheckBox;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.label.MLabel;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.core.model.bean.GxPasswordPolicyBean;
import io.graphenee.security.api.GxPasswordPolicyDataService;
import io.graphenee.vaadin.TRAbstractForm;

@SpringComponent
@Scope("prototype")
public class GxDefaultPasswordPolicyForm extends TRAbstractForm<GxPasswordPolicyBean> {

	MTextField maxHistory;
	MTextField maxAge;
	MTextField minLength;
	MCheckBox isUserUsernameAllowed, isActive;
	MTextField maxAllowedMatchingUserName;
	MTextField minUppercase;
	MTextField minLowercase;
	MTextField minNumbers;
	MTextField minSpecialCharacters;

	MTextField testUsernameTextField;
	MTextField testPolicyTextField;
	MLabel testPolicyResultLabel;

	@Autowired
	GxPasswordPolicyDataService passwordPolicyDataService;

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected void addFieldsToForm(FormLayout form) {
		maxHistory = new MTextField("Max History");
		maxAge = new MTextField("Max Days");
		minLength = new MTextField("Min Length");
		isUserUsernameAllowed = new MCheckBox("Is Username Allowed?");
		maxAllowedMatchingUserName = new MTextField("Max Match Username");
		minUppercase = new MTextField("Min Upper Case");
		minLowercase = new MTextField("Min Lower Case");
		minNumbers = new MTextField("Min Numbers");
		minSpecialCharacters = new MTextField("Min Special Chars");
		isActive = new MCheckBox("Is Active?");

		isUserUsernameAllowed.addValueChangeListener(event -> {
			if (!isBinding()) {
				Boolean v = (Boolean) event.getProperty().getValue();
				maxAllowedMatchingUserName.setEnabled(v);
				maxAllowedMatchingUserName.setRequired(v);
			}

		});

		testUsernameTextField = new MTextField().withCaption("Test Username:").withInputPrompt("Type in test username e.g. admin");

		testPolicyTextField = new MTextField().withCaption("Test Password:").withInputPrompt("Type in the password to test this policy...");
		testPolicyTextField.addValueChangeListener(event -> {
			try {
				passwordPolicyDataService.assertPasswordPolicy(getEntity(), testUsernameTextField.getValue(), (String) event.getProperty().getValue());
				testPolicyResultLabel.setValue("Passed");
				testPolicyResultLabel.removeStyleName(ValoTheme.LABEL_FAILURE);
				testPolicyResultLabel.setStyleName(ValoTheme.LABEL_SUCCESS);
			} catch (AssertionError err) {
				testPolicyResultLabel.setValue(err.getMessage());
				testPolicyResultLabel.removeStyleName(ValoTheme.LABEL_SUCCESS);
				testPolicyResultLabel.setStyleName(ValoTheme.LABEL_FAILURE);
			}
		});

		testPolicyTextField.addFocusListener(event -> {
			testPolicyResultLabel.setValue(null);
			testPolicyResultLabel.removeStyleName(ValoTheme.LABEL_FAILURE);
			testPolicyResultLabel.removeStyleName(ValoTheme.LABEL_SUCCESS);
		});

		testPolicyResultLabel = new MLabel();

		form.addComponents(maxHistory, maxAge, minLength, isUserUsernameAllowed, maxAllowedMatchingUserName, minUppercase, minLowercase, minNumbers, minSpecialCharacters, isActive,
				testUsernameTextField, testPolicyTextField, testPolicyResultLabel);
	}

	@Override
	protected void postBinding(GxPasswordPolicyBean entity) {
		super.postBinding(entity);
		Boolean v = entity.getOid() == null ? false : entity.getIsUserUsernameAllowed();
		maxAllowedMatchingUserName.setEnabled(v);
		maxAllowedMatchingUserName.setRequired(v);
	}

	@Override
	protected String formTitle() {
		return "Password Policy";
	}

	@Override
	protected boolean shouldShowDismissButton() {
		return false;
	}

}
