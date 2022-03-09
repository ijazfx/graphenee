package io.graphenee.vaadin.flow.documents;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;

import org.springframework.context.annotation.Scope;

import io.graphenee.core.model.entity.GxFolder;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm;

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
		note.setHeight("100px");
		entityForm.add(name, note);

		setColspan(name, 2);
		setColspan(note, 2);
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
