package io.graphenee.vaadin.flow.email_template;

import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.model.entity.GxEmailTemplate;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm;

@SuppressWarnings("serial")
@Scope("prototype")
@SpringComponent
public class GxEmailTemplateForm extends GxAbstractEntityForm<GxEmailTemplate> {

	private TextField templateName;
	private TextField templateCode;
	private TextField subject;
	private TextField senderEmailAddress;
	private TextArea body;
	private TextArea smsBody;
	private TextArea ccList;
	private TextArea bccList;
	private Checkbox isActive;

	private Tabs tabs;
	private Tab emailTab;
	private Tab messageTab;
	private NativeLabel messageCount;
	private VerticalLayout content;

	final int perSMSMaxLength = 160;

	public GxEmailTemplateForm() {
		super(GxEmailTemplate.class);
	}

	@Override
	protected void decorateForm(HasComponents entityForm) {
		templateName = new TextField("Name");
		templateCode = new TextField("Code");
		isActive = new Checkbox("Is Active?");

		subject = new TextField("Subject");
		subject.setWidthFull();

		senderEmailAddress = new TextField("Sender");
		senderEmailAddress.setWidthFull();

		messageCount = new NativeLabel();
		messageCount.setWidthFull();

		body = new TextArea("Body");
		body.setPlaceholder("Dear #{lastName},\nThis is a test message");
		body.setClearButtonVisible(true);
		body.setMinHeight("300px");
		body.setMaxHeight("350px");
		body.setWidthFull();

		smsBody = new TextArea("Body");
		smsBody.setPlaceholder("Dear #{lastName},\nThis is a test message");
		smsBody.setClearButtonVisible(true);
		smsBody.setMinHeight("300px");
		smsBody.setMaxHeight("350px");
		smsBody.setWidthFull();
		smsBody.setValueChangeMode(ValueChangeMode.EAGER);

		smsBody.addValueChangeListener(event -> {
			Integer result = event.getValue().length() / perSMSMaxLength;
			if (event.getValue().length() > perSMSMaxLength) {
				if (event.getValue().length() % perSMSMaxLength == 0) {
					messageCount.setText("The message will consume " + (result) + " no. of sms.");
				} else {
					messageCount.setText("The message will consume " + (result + 1) + " no. of sms.");
				}

			} else {
				messageCount.setText(null);
			}
		});

		ccList = new TextArea("CC To");
		ccList.setPlaceholder("Separate email addresses with , or;");
		ccList.setClearButtonVisible(true);
		ccList.setMinHeight("75px");
		ccList.setMaxHeight("150px");
		ccList.setWidthFull();

		bccList = new TextArea("BCC To");
		bccList.setPlaceholder("Separate email addresses with , or;");
		bccList.setClearButtonVisible(true);
		bccList.setMinHeight("75px");
		bccList.setMaxHeight("150px");
		bccList.setWidthFull();

		emailTab = new Tab("Email Message");
		messageTab = new Tab("SMS Message");
		tabs = new Tabs(emailTab, messageTab);

		tabs.addSelectedChangeListener(event -> {
			buildTabBody(event.getSelectedTab());
		});

		tabs.setSelectedTab(emailTab);

		content = new VerticalLayout();
		content.setWidthFull();
		content.setSpacing(false);
		content.setPadding(false);
		buildTabBody(tabs.getSelectedTab());

		setColspan(tabs, 2);
		setColspan(content, 2);
		entityForm.add(templateName, templateCode, isActive, tabs, content);
	}

	private void buildTabBody(Tab tab) {
		content.removeAll();
		if (tab.equals(emailTab)) {
			content.add(subject, body, ccList, bccList, senderEmailAddress);
		} else {
			content.add(smsBody);
			content.add(messageCount);
		}
	}

	@Override
	protected void bindFields(Binder<GxEmailTemplate> dataBinder) {
		dataBinder.forMemberField(templateName).asRequired("Template Name is required.");
		dataBinder.forMemberField(templateCode).asRequired("Template Code is required.");
		dataBinder.forMemberField(subject).asRequired("Subject is required.");
		dataBinder.forMemberField(body).asRequired("Body is required.");
		dataBinder.forMemberField(smsBody).asRequired("SMS Body is required.");
	}

	@Override
	protected void preBinding(GxEmailTemplate entity) {

	}

	@Override
	protected String formTitle() {
		return "Message Template";
	}

	@Override
	protected String dialogHeight() {
		return "1024px";
	}

	@Override
	protected String dialogWidth() {
		return "1024px";
	}
}
