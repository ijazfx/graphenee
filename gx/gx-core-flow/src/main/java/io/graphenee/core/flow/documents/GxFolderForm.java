package io.graphenee.core.flow.documents;

import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.model.entity.GxFolder;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxFolderForm extends GxAbstractEntityForm<GxFolder> {

	TextField name;
	TextArea note;

	public GxFolderForm() {
		super(GxFolder.class);
	}

	@Override
	protected void decorateForm(HasComponents entityForm) {
		name = new TextField("Folder Name");
		note = new TextArea("Note");
		note.setHeight("7rem");
		entityForm.add(name, note);

		entityForm.add(name, note);

		expand(name, note);
	}

	@Override
	protected void bindFields(Binder<GxFolder> dataBinder) {
		dataBinder.forMemberField(name).asRequired();
	}

	@Override
	protected String formTitleProperty() {
		return "name";
	}

	@Override
	protected String dialogHeight() {
		return "350px";
	}

}
