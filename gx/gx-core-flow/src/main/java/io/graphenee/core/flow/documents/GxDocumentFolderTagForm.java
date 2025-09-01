package io.graphenee.core.flow.documents;

import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.model.entity.GxFileTag;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxDocumentFolderTagForm extends GxAbstractEntityForm<GxFileTag> {

    TextField tag;

    public GxDocumentFolderTagForm() {
        super(GxFileTag.class);
    }

    @Override
    protected void decorateForm(HasComponents entityForm) {
        tag = new TextField("Tag Name");
        entityForm.add(tag);

    }

    @Override
    protected void bindFields(Binder<GxFileTag> dataBinder) {
        dataBinder.forMemberField(tag).asRequired();
    }

    @Override
    protected String formTitleProperty() {
        return "tag";
    }

    @Override
    protected String dialogHeight() {
        return "350px";
    }

}
