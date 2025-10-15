package io.graphenee.core.flow.security;

import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.model.entity.GxAccessKey;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;

@SpringComponent
@Scope("prototype")
public class GxAccessKeyForm extends GxAbstractEntityForm<GxAccessKey> {

	TextField accessKey;
	PasswordField secret;
	Checkbox isActive;

	public GxAccessKeyForm() {
		super(GxAccessKey.class);
	}

	@Override
	protected void decorateForm(HasComponents entityForm) {
		accessKey = new TextField("Access Key");
		secret = new PasswordField("Secret");
		isActive = new Checkbox("Is Active?", true);
		
		entityForm.add(accessKey, secret, isActive);
	}
	
	@Override
	protected void postBinding(GxAccessKey entity) {
		accessKey.setReadOnly(true);
		secret.setReadOnly(true);
	}

}
