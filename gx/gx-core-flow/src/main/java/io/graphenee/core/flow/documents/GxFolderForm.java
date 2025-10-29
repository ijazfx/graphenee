package io.graphenee.core.flow.documents;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.GxDataService;
import io.graphenee.core.model.entity.GxFolder;
import io.graphenee.core.model.entity.GxTag;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxFolderForm extends GxAbstractEntityForm<GxFolder> {

	@Autowired
	GxDataService dataService;

	TextField name;
	TextArea note;
	MultiSelectComboBox<GxTag> tags;
	MultiSelectComboBox<Principal> grants;

	public GxFolderForm() {
		super(GxFolder.class);
	}

	@Override
	protected void decorateForm(HasComponents entityForm) {
		name = new TextField("Folder Name");
		note = new TextArea("Note");
		note.setHeight("7rem");
		tags = new MultiSelectComboBox<>("Add Tags");
		tags.addCustomValueSetListener(l -> {
			GxTag newTag = new GxTag();
			newTag.setTag(l.getDetail());
			newTag.setNamespace(getEntity().getNamespace());
			tags.getListDataView().addItem(newTag);
			Set<GxTag> updated = new HashSet<>(tags.getValue());
			updated.add(newTag);
			tags.setValue(updated);
			tags.setOpened(false);
		});
		grants = new MultiSelectComboBox<>("Grant Access (User/Group)");
		grants.setItemLabelGenerator(i -> i.getName());

		entityForm.add(name, note, tags, grants);
		expand(name, note, tags, grants);
	}

	@Override
	protected void preBinding(GxFolder entity) {
		tags.clear();
		grants.clear();
		tags.setItems(dataService.findTagByNamespace(entity.getNamespace()));
		grants.setItems(dataService.findPrincipalActiveByNamespace(entity.getNamespace()));
	}

	@Override
	protected void bindFields(Binder<GxFolder> dataBinder) {
		dataBinder.forMemberField(name).asRequired();
	}

	@Override
	protected String dialogHeight() {
		return "37.5rem";
	}

	@Override
	protected String dialogWidth() {
		return "50rem";
	}

	@Override
	protected String formTitleProperty() {
		return "name";
	}

}
