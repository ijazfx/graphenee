package io.graphenee.vaadin.flow.security;

import java.util.List;

import com.flowingcode.vaadin.addons.twincolgrid.TwinColGrid;
//import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.validator.EmailValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxSecurityGroupBean;
import io.graphenee.core.model.bean.GxSecurityPolicyBean;
import io.graphenee.core.model.bean.GxUserAccountBean;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.base.GxTabItem;
import io.graphenee.vaadin.flow.component.FileUploader;
import io.graphenee.vaadin.flow.converter.BeanCollectionFaultToSetConverter;

@Component
@Scope("prototype")
public class GxUserAccountForm extends GxAbstractEntityForm<GxUserAccountBean> {
	private static final long serialVersionUID = 1L;

	public GxUserAccountForm() {
		super(GxUserAccountBean.class);
	}

	private TextField username;
	private TextField firstName;
	private TextField lastName;
	private TextField email;
	private Checkbox isActive;
	private Checkbox isLocked;
	private Checkbox isPasswordChangeRequired;
	private PasswordField newPassword;
	private PasswordField password;
	FileUploader imageUploader;
	private TwinColGrid<GxSecurityPolicyBean> securityPolicyCollectionFault;
	private TwinColGrid<GxSecurityGroupBean> securityGroupCollectionFault;

	@Autowired
	GxDataService gxDataService;

	@Override
	protected void decorateForm(HasComponents entityForm) {
		username = new TextField("Username");

		firstName = new TextField("First Name");
		lastName = new TextField("Last Name");
		email = new TextField("Email");

		isActive = new Checkbox("Is Active?");
		isLocked = new Checkbox("Is Locked?");
		isPasswordChangeRequired = new Checkbox("Is Password Change Required?");
		securityPolicyCollectionFault = new TwinColGrid<GxSecurityPolicyBean>().addFilterableColumn(GxSecurityPolicyBean::getSecurityPolicyName, "Policy Name", "Policy Name", true)
				.withLeftColumnCaption("Available").withRightColumnCaption("Selected");
		securityPolicyCollectionFault.setSizeFull();

		securityGroupCollectionFault = new TwinColGrid<GxSecurityGroupBean>().addFilterableColumn(GxSecurityGroupBean::getSecurityGroupName, "Group Name", "Group Name", true)
				.withLeftColumnCaption("Available").withRightColumnCaption("Selected");
		securityGroupCollectionFault.setSizeFull();

		// Radio Buttons for gender
		// gender = new RadioButtonGroup<>();
		// gender.setLabel("Gender");
		// gender.setItems(GenderEnum.Male, GenderEnum.Female, GenderEnum.Undisclosed);
		// gender.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
		// radioGroup.setValue("Option one");
		// add(radioGroup);
		// System.err.println(GenderEnum.valueOf("M"));

		newPassword = new PasswordField("New Password");
		password = new PasswordField("Confirm Password");

		newPassword.addValueChangeListener(vcl -> {
			password.clear();
			password.setEnabled(vcl.getValue().length() > 1);
		});

		imageUploader = new FileUploader("Profile Image");
		imageUploader.setAllowedFileTypes("image/png", "image/jpg", "image/jpeg");
		imageUploader.addValueChangeListener(event -> {
			System.err.println(event.getValue());
		});

		entityForm.add(username, firstName, lastName, email, isActive, isLocked, isPasswordChangeRequired, newPassword, password, imageUploader);

		setColspan(isPasswordChangeRequired, 2);
	}

	@Override
	protected void bindFields(Binder<GxUserAccountBean> dataBinder) {
		dataBinder.forMemberField(username).asRequired();
		dataBinder.forMemberField(email).withValidator(new EmailValidator("Must be a valid email address"));
		dataBinder.forMemberField(securityPolicyCollectionFault).withConverter(new BeanCollectionFaultToSetConverter<GxSecurityPolicyBean>());
		dataBinder.forMemberField(securityGroupCollectionFault).withConverter(new BeanCollectionFaultToSetConverter<GxSecurityGroupBean>());
		dataBinder.forMemberField(password).withValidator(new Validator<String>() {

			private static final long serialVersionUID = 1L;

			@Override
			public ValidationResult apply(String value, ValueContext context) {
				if (newPassword.getValue().equals(value)) {
					return ValidationResult.ok();
				}
				return ValidationResult.error("Confirm password does not match with new password!");
			}

		});
	}

	@Override
	protected void addTabsToForm(List<GxTabItem> tabItems) {
		tabItems.add(GxTabItem.create(1, "Policies", securityPolicyCollectionFault));
		tabItems.add(GxTabItem.create(2, "Groups", securityGroupCollectionFault));
	}

	@Override
	protected void preBinding(GxUserAccountBean entity) {
		newPassword.clear();
		password.setEnabled(false);
		securityPolicyCollectionFault.setItems(gxDataService.findSecurityPolicyByNamespaceActive(entity.getNamespaceFault().getBean()));
		securityGroupCollectionFault.setItems(gxDataService.findSecurityGroupByNamespaceActive(entity.getNamespaceFault().getBean()));
	}

}
