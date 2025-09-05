package io.graphenee.core.flow.documents;

import com.vaadin.flow.component.combobox.MultiSelectComboBox;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxFolderForm extends GxAbstractEntityForm<GxFolder> {

	@Autowired
	GxFileTagRepository tagRepository;

	TextField name;
	TextArea note;
	MultiSelectComboBox<GxFileTag> fileTags;


	public GxFolderForm() {
		super(GxFolder.class);
	}

	@Override
	protected void decorateForm(HasComponents entityForm) {
		name = new TextField("Folder Name");
		note = new TextArea("Note");
		note.setHeight("7rem");

		fileTags = new MultiSelectComboBox<>("Add Tags");

		fileTags.addCustomValueSetListener(l -> {
			GxFileTag newTag = new GxFileTag();
			newTag.setTag(l.getDetail());
			newTag.setOid(null);

			// Copy current value into a mutable set
			Set<GxFileTag> updated = new HashSet<>(fileTags.getValue());
			updated.add(newTag);

			// Update items (so the combo knows this tag exists)
			List<GxFileTag> items = new ArrayList<>(fileTags.getListDataView().getItems().toList());
			items.add(newTag);
			fileTags.setItems(items);

			// Set new value
			fileTags.setValue(updated);
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
