package io.graphenee.core.flow.documents;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.model.entity.GxDocument;
import io.graphenee.core.model.entity.GxTag;
import io.graphenee.core.model.jpa.repository.GxTagRepository;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.data.TimestampToDateTimeConverter;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxDocumentForm extends GxAbstractEntityForm<GxDocument> {

	TextField name;
	TextArea note;
	PasswordField uniqueId;
	DateTimePicker issueDate;
	DateTimePicker expiryDate;
	IntegerField expiryReminderInDays;
	DateTimePicker reminderDate;
	MultiSelectComboBox<GxTag> tags;
		Checkbox isReadOnly;


	public GxDocumentForm() {
		super(GxDocument.class);
	}

	@Autowired
	GxTagRepository tagRepository;

	@Override
	protected void decorateForm(HasComponents entityForm) {
		name = new TextField("Name");

		note = new TextArea("Note");
		note.setHeight("7rem");

		uniqueId = new PasswordField("Document ID");

		issueDate = new DateTimePicker("Issue Date");
		expiryDate = new DateTimePicker("Expiry Date");
		expiryReminderInDays = new IntegerField("Expiry Reminder (in Days)");
		expiryReminderInDays.setMin(0);

		reminderDate = new DateTimePicker("Reminder Date");

		tags = new MultiSelectComboBox<>("Add Tags");

		tags.addCustomValueSetListener(l -> {
			GxTag newTag = new GxTag();
			newTag.setTag(l.getDetail());
			newTag.setOid(null);
			newTag.setNamespace(getEntity().getNamespace());

			// Copy current value into a mutable set
			Set<GxTag> updated = new HashSet<>(tags.getValue());
			updated.add(newTag);

			// Update items (so the combo knows this tag exists)
			List<GxTag> items = new ArrayList<>(tags.getListDataView().getItems().toList());
			items.add(newTag);
			tags.setItems(items);

			// Set new value
			tags.setValue(updated);
		});

				isReadOnly = new Checkbox("Is Read Only ?");


		entityForm.add(name, uniqueId, isReadOnly, note, issueDate, expiryDate, expiryReminderInDays, reminderDate, tags);

		setColspan(tags, 2);
		setColspan(isReadOnly, 2);
		setColspan(note, 2);
	}

	@Override
	protected void preBinding(GxDocument entity) {
		tags.setItems(tagRepository.findAll());
	}

	@Override
	protected void bindFields(Binder<GxDocument> dataBinder) {
		dataBinder.forMemberField(name).asRequired();
		dataBinder.forMemberField(issueDate).withConverter(new TimestampToDateTimeConverter());
		dataBinder.forMemberField(expiryDate).withConverter(new TimestampToDateTimeConverter());
		dataBinder.forMemberField(reminderDate).withConverter(new TimestampToDateTimeConverter());
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
