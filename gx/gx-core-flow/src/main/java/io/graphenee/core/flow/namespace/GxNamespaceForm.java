package io.graphenee.core.flow.namespace;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;

@SuppressWarnings("serial")
@Component
@Scope("prototype")
public class GxNamespaceForm extends GxAbstractEntityForm<GxNamespace> {

	TextField namespace;
	TextArea namespaceDescription;
	Checkbox isActive;

	public GxNamespaceForm() {
		super(GxNamespace.class);
	}

	@Override
	protected void decorateForm(HasComponents form) {
		namespace = new TextField("Namespace");
		namespaceDescription = new TextArea("Description");
		isActive = new Checkbox("Is Active?");
		form.add(namespace, namespaceDescription, isActive);
		setColspan(namespace, 2);
		setColspan(namespaceDescription, 2);
	}

	@Override
	protected void bindFields(Binder<GxNamespace> dataBinder) {
		dataBinder.forMemberField(namespace).asRequired("Device token is required");
	}

	@Override
	protected String formTitle() {
		return "Namespace";
	}

	@Override
	protected String dialogHeight() {
		return "400px";
	}

}
