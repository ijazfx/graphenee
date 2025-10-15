package io.graphenee.core.flow.documents;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.model.entity.GxDocumentExplorerItem;
import io.graphenee.core.model.entity.GxFileTag;
import io.graphenee.core.model.jpa.repository.GxFileTagRepository;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;

@SpringComponent
@Scope("prototype")
public class GxDocumentExplorerItemForm extends GxAbstractEntityForm<GxDocumentExplorerItem> {

	@Autowired
	GxFileTagRepository tagRepository;

	TextField name;
	TextArea note;
	DatePicker issueDate;
	DatePicker expiryDate;
	IntegerField expiryReminderInDays;
	DatePicker reminderDate;
	MultiSelectComboBox<GxFileTag> fileTags;

	public GxDocumentExplorerItemForm() {
		super(GxDocumentExplorerItem.class);
	}

	@Override
	protected void decorateForm(HasComponents entityForm) {
		name = new TextField("Folder Name");
		note = new TextArea("Note");
		note.setHeight("100px");

		issueDate = new DatePicker("Issue Date");
		expiryDate = new DatePicker("Expiry Date");
		expiryReminderInDays = new IntegerField("Expiry Reminder (in Days)");
		expiryReminderInDays.setMin(0);

		reminderDate = new DatePicker("Reminder Date");

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

		entityForm.add(name, note, issueDate, expiryDate, expiryReminderInDays, reminderDate, fileTags);

		setColspan(fileTags, 2);

		expand(name, note);
	}

	@Override
	protected void bindFields(Binder<GxDocumentExplorerItem> dataBinder) {
		dataBinder.forMemberField(name).asRequired();
		dataBinder.forMemberField(issueDate); //.withConverter(new TimestampToDateConverter());
		dataBinder.forMemberField(expiryDate); //.withConverter(new TimestampToDateConverter());
		dataBinder.forMemberField(reminderDate); //.withConverter(new TimestampToDateConverter());
	}

	@Override
	protected void preBinding(GxDocumentExplorerItem entity) {
		fileTags.setItems(tagRepository.findAll());
	}

	@Override
	protected void postBinding(GxDocumentExplorerItem entity) {
		super.postBinding(entity);
		if (getEntity().isFile()) {
			setColspan(expiryReminderInDays, 1);
		} else {
			setColspan(expiryReminderInDays, 2);
		}
	}

	@Override
	public void setEntity(GxDocumentExplorerItem entity) {
		super.setEntity(entity);
		Boolean isFile = entity.isFile();
		issueDate.setVisible(isFile);
		expiryDate.setVisible(isFile);
		expiryReminderInDays.setVisible(true);
		reminderDate.setVisible(isFile);
	}

	@Override
	protected String formTitleProperty() {
		return "name";
	}

	@Override
	protected String dialogHeight() {
		return "550px";
	}

}
