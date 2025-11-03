package io.graphenee.core.flow.documents;

import java.security.Principal;
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
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.GxDataService;
import io.graphenee.core.model.entity.GxDocumentExplorerItem;
import io.graphenee.core.model.entity.GxTag;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.data.TimestampToDateConverter;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxDocumentExplorerItemForm extends GxAbstractEntityForm<GxDocumentExplorerItem> {

	@Autowired
	GxDataService dataService;

	TextField name;
	TextArea note;
	PasswordField uniqueId;
	DatePicker issueDate;
	DatePicker expiryDate;
	IntegerField expiryReminderInDays;
	DatePicker reminderDate;
	MultiSelectComboBox<GxTag> tags;
	MultiSelectComboBox<Principal> grants;

	public GxDocumentExplorerItemForm() {
		super(GxDocumentExplorerItem.class);
	}

	@Override
	protected void decorateForm(HasComponents entityForm) {
		name = new TextField("Folder Name");
		note = new TextArea("Note");
		note.setHeight("100px");

		uniqueId = new PasswordField("Document ID");

		issueDate = new DatePicker("Issue Date");
		expiryDate = new DatePicker("Expiry Date");
		expiryReminderInDays = new IntegerField("Expiry Reminder (in Days)");
		expiryReminderInDays.setMin(0);
		reminderDate = new DatePicker("Reminder Date");
		tags = new MultiSelectComboBox<>("Add Tags");
		tags.addCustomValueSetListener(l -> {
			GxTag newTag = new GxTag();
			newTag.setTag(l.getDetail());
			newTag.setOid(null);

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
		grants = new MultiSelectComboBox<>("Grant Access (User/Group)");
		grants.setItemLabelGenerator(i -> i.getName());

		entityForm.add(name, uniqueId, note, issueDate, expiryDate, expiryReminderInDays, reminderDate, tags, grants);

		expand(note, tags, grants);
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
		tags.clear();
		grants.clear();
		tags.setItems(dataService.findTagByNamespace(entity.getNamespace()));
		grants.setItems(dataService.findPrincipalActiveByNamespace(entity.getNamespace()));
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
		return "50rem";
	}

	@Override
	protected String dialogWidth() {
		return "50rem";
	}

}
