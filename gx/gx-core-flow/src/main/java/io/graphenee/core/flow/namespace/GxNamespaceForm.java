package io.graphenee.core.flow.namespace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;

@SpringComponent
@Scope("prototype")
public class GxNamespaceForm extends GxAbstractEntityForm<GxNamespace> {

    TextField namespace;
    TextArea namespaceDescription;
    Checkbox isActive;

    @Autowired
    private GxNamespacePropertyList properties;

    public GxNamespaceForm() {
        super(GxNamespace.class);
    }

    @Override
    protected void decorateForm(HasComponents form) {
        namespace = new TextField("Namespace");
        namespaceDescription = new TextArea("Description");
        isActive = new Checkbox("Is Active?");
        form.add(namespace, namespaceDescription, isActive);
        expand(namespace);
        expand(namespaceDescription);

        Details propertiesDetails = new Details("Namespace Properties");
        propertiesDetails.setOpened(true);
        propertiesDetails.add(properties);
        form.add(propertiesDetails);
        expand(propertiesDetails);
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
