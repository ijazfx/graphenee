package io.graphenee.security.vaadin;

import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.fields.MCheckBox;
import org.vaadin.viritin.fields.MTextField;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.FormLayout;

import io.graphenee.core.model.bean.GxPasswordPolicyBean;
import io.graphenee.vaadin.TRAbstractForm;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxPasswordPolicyForm extends TRAbstractForm<GxPasswordPolicyBean> {

	MTextField passwordPolicyName;
	MTextField maxHistory;
	MTextField maxAge;
	MTextField minLength;
	MCheckBox isUserUsernameAllowed, isActive;
	MTextField maxAllowedMatchingUserName;
	MTextField minUppercase;
	MTextField minLowercase;
	MTextField minNumbers;
	MTextField minSpecialCharacters;

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected void addFieldsToForm(FormLayout form) {
		passwordPolicyName = new MTextField("Policy Name").withRequired(true);
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
				if (!v)
					maxAllowedMatchingUserName.clear();
			}

		});
		form.addComponents(passwordPolicyName, maxHistory, maxAge, minLength, isUserUsernameAllowed, maxAllowedMatchingUserName, minUppercase, minLowercase, minNumbers,
				minSpecialCharacters, isActive);
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

}
