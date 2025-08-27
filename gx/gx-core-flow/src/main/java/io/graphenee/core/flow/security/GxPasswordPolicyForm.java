package io.graphenee.core.flow.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.model.entity.GxPasswordPolicy;
import io.graphenee.security.GxPasswordPolicyDataService;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxPasswordPolicyForm extends GxAbstractEntityForm<GxPasswordPolicy> {

	@Autowired
	private GxPasswordPolicyDataService passwordPolicyDataService;

	TextField passwordPolicyName;
	IntegerField maxHistory;
	IntegerField maxAge;
	IntegerField minLength;
	Checkbox isUserUsernameAllowed;
	IntegerField maxAllowedMatchingUserName;
	IntegerField minUppercase;
	IntegerField minLowercase;
	IntegerField minNumbers;
	IntegerField minSpecialCharacters;
	Checkbox isActive;

	public GxPasswordPolicyForm() {
		super(GxPasswordPolicy.class);
	}

	@Override
	protected void decorateForm(HasComponents entityForm) {
		passwordPolicyName = new TextField("Password Policy Name");
		maxHistory = new IntegerField("Password History");
		maxAge = new IntegerField("Maximum Age");
		minLength = new IntegerField("Minimum Length");
		maxAllowedMatchingUserName = new IntegerField("Maximum Username Characters Allowed");
		minUppercase = new IntegerField("Minimum Uppercase Characters");
		minLowercase = new IntegerField("Minimum Lowercase Characters");
		minNumbers = new IntegerField("Minimum Numbers");
		minSpecialCharacters = new IntegerField("Minimum Special Characters");
		isUserUsernameAllowed = new Checkbox("Username allowed in Password?", true);
		isActive = new Checkbox("Is Active?", true);

		TextField username = new TextField("Test Username");
		TextField testPolicy = new TextField("Test Password");
		testPolicy.addValueChangeListener(vcl -> {
			try {
				passwordPolicyDataService.assertPasswordPolicy(getEntity(), username.getValue(), vcl.getValue());
			} catch (AssertionError error) {
				testPolicy.setErrorMessage(error.getMessage());
			}
		});

		entityForm.add(group("group0", maxHistory, maxAge,
				minLength, maxAllowedMatchingUserName), group("group1", minUppercase, minLowercase,
				minNumbers, minSpecialCharacters), group("group2", isUserUsernameAllowed, isActive), new Hr(), group("group3", username, testPolicy));
		
		expand(username, testPolicy);
		expand("group0", "group1", "group2", "group3");
	}

}
