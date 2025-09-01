package io.graphenee.core.flow.documents;

import com.flowingcode.vaadin.addons.chipfield.ChipField;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import io.graphenee.core.model.entity.GxFileTag;
import io.graphenee.core.model.jpa.repository.GxFileTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.model.entity.GxDocumentExplorerItem;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.data.TimestampToDateConverter;

@SuppressWarnings("serial")
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
	ChipField<GxFileTag> fileTags;

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

		fileTags = new ChipField<>("Add Tags");

		fileTags.setNewItemHandler(label -> {
			GxFileTag newTag = new GxFileTag();
			newTag.setTag(label);
			newTag.setOid(null);
			return newTag;
		});

		entityForm.add(name, note, issueDate, expiryDate, expiryReminderInDays, reminderDate, fileTags);

		setColspan(fileTags, 2);

		expand(name, note);
	}

	@Override
	protected void bindFields(Binder<GxDocumentExplorerItem> dataBinder) {
		dataBinder.forMemberField(name).asRequired();
		dataBinder.forMemberField(issueDate).withConverter(new TimestampToDateConverter());
		dataBinder.forMemberField(expiryDate).withConverter(new TimestampToDateConverter());
		dataBinder.forMemberField(reminderDate).withConverter(new TimestampToDateConverter());
	}

	@Override
	protected void preBinding(GxDocumentExplorerItem entity) {
		fileTags.setItems(tagRepository.findAll());
	}

	@Override
	protected void postBinding(GxDocumentExplorerItem entity) {
		super.postBinding(entity);
		if (getEntity().isFile()) {
			setColspan(fileTags, 2);
		} else {
			setColspan(fileTags, 1);
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
