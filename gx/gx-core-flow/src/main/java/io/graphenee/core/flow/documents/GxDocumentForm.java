package io.graphenee.core.flow.documents;

import com.flowingcode.vaadin.addons.chipfield.ChipField;
import io.graphenee.core.model.entity.GxFileTag;
import io.graphenee.core.model.jpa.repository.GxFileTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.model.entity.GxDocument;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.data.TimestampToDateTimeConverter;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxDocumentForm extends GxAbstractEntityForm<GxDocument> {

	TextField name;
	TextArea note;
	DateTimePicker issueDate;
	DateTimePicker expiryDate;
	IntegerField expiryReminderInDays;
	DateTimePicker reminderDate;
	ChipField<GxFileTag> fileTags;

	public GxDocumentForm() {
		super(GxDocument.class);
	}

	@Autowired
	GxFileTagRepository tagRepository;

	@Override
	protected void decorateForm(HasComponents entityForm) {
		name = new TextField("Name");

		note = new TextArea("Note");
		note.setHeight("7rem");

		issueDate = new DateTimePicker("Issue Date");
		expiryDate = new DateTimePicker("Expiry Date");
		expiryReminderInDays = new IntegerField("Expiry Reminder (in Days)");
		expiryReminderInDays.setMin(0);

		reminderDate = new DateTimePicker("Reminder Date");

		fileTags = new ChipField<>("Add Tags");

		fileTags.setNewItemHandler(label -> {
			GxFileTag newTag = new GxFileTag();
			newTag.setTag(label);
			newTag.setOid(null);
			return newTag;
		});

		entityForm.add(name, note, issueDate, expiryDate, expiryReminderInDays, reminderDate, fileTags);

		setColspan(fileTags, 2);
		setColspan(name, 2);
		setColspan(note, 2);
	}

	@Override
	protected void preBinding(GxDocument entity) {
		fileTags.setItems(tagRepository.findAll());
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
