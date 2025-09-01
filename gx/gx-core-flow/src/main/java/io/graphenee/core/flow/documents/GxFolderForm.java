package io.graphenee.core.flow.documents;

import com.flowingcode.vaadin.addons.chipfield.ChipField;
import io.graphenee.core.model.entity.GxFileTag;
import io.graphenee.core.model.jpa.repository.GxFileTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.HasComponents;
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

	@Autowired
	GxFileTagRepository tagRepository;

	TextField name;
	TextArea note;
	ChipField<GxFileTag> fileTags;


	public GxFolderForm() {
		super(GxFolder.class);
	}

	@Override
	protected void decorateForm(HasComponents entityForm) {
		name = new TextField("Folder Name");
		note = new TextArea("Note");
		note.setHeight("7rem");

		fileTags = new ChipField<>("Add Tags");

		fileTags.setNewItemHandler(label -> {
			GxFileTag newTag = new GxFileTag();
			newTag.setTag(label);
			newTag.setOid(null);
			return newTag;
		});

		entityForm.add(name, note, fileTags);

		setColspan(fileTags, 2);

		expand(name, note);
	}

	@Override
	protected void preBinding(GxFolder entity) {
		fileTags.setItems(tagRepository.findAll());
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
		return "400px";
	}

}
