package io.graphenee.vaadin.flow.documents;

import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.model.entity.GxDocumentExplorerItem;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.converter.TimestampToDateTimeConverter;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxDocumentExplorerItemForm extends GxAbstractEntityForm<GxDocumentExplorerItem> {

    TextField name;
    TextArea note;
    DateTimePicker issueDate;
    DateTimePicker expiryDate;
    IntegerField expiryReminderInDays;
    DateTimePicker reminderDate;

    public GxDocumentExplorerItemForm() {
        super(GxDocumentExplorerItem.class);
    }

    @Override
    protected void decorateForm(HasComponents entityForm) {
        name = new TextField("Folder Name");
        note = new TextArea("Note");
        note.setHeight("100px");

        issueDate = new DateTimePicker("Issue Date");
        expiryDate = new DateTimePicker("Expiry Date");
        expiryReminderInDays = new IntegerField("Expiry Reminder (in Days)");
        expiryReminderInDays.setMin(0);

        reminderDate = new DateTimePicker("Reminder Date");

        entityForm.add(name, note, issueDate, expiryDate, expiryReminderInDays, reminderDate);

        setColspan(name, 2);
        setColspan(note, 2);
    }

    @Override
    protected void bindFields(Binder<GxDocumentExplorerItem> dataBinder) {
        dataBinder.forMemberField(issueDate).withConverter(new TimestampToDateTimeConverter());
        dataBinder.forMemberField(expiryDate).withConverter(new TimestampToDateTimeConverter());
        dataBinder.forMemberField(reminderDate).withConverter(new TimestampToDateTimeConverter());
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
        return "450px";
    }

}
