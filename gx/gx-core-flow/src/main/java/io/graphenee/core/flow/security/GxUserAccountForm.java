package io.graphenee.core.flow.security;

import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.flowingcode.vaadin.addons.twincolgrid.TwinColGrid;
//import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.GxDataService;
import io.graphenee.core.model.entity.GxSecurityGroup;
import io.graphenee.core.model.entity.GxSecurityPolicy;
import io.graphenee.core.model.entity.GxUserAccount;
import io.graphenee.security.GxPasswordPolicyDataService;
import io.graphenee.util.storage.FileStorage;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.GxTabItem;
import io.graphenee.vaadin.flow.component.FileUploader;

@SpringComponent
@Scope("prototype")
public class GxUserAccountForm extends GxAbstractEntityForm<GxUserAccount> {
    private static final long serialVersionUID = 1L;

    public GxUserAccountForm() {
        super(GxUserAccount.class);
    }

    private TextField username;
    private TextField firstName;
    private TextField lastName;
    private TextField email;
    private Checkbox isActive;
    private Checkbox isLocked;
    private Checkbox isPasswordChangeRequired;
    private PasswordField newPassword;
    private PasswordField confirmPassword;
    FileUploader imageUploader;
    private TwinColGrid<GxSecurityPolicy> securityPolicies;
    private TwinColGrid<GxSecurityGroup> securityGroups;

    @Autowired
    private GxAccessKeyList accessKeyList;

    @Autowired
    GxDataService dataService;

    @Autowired
    private FileStorage storage;

    @Autowired
    GxPasswordPolicyDataService passwordPolicyService;

    @Override
    protected void decorateForm(HasComponents entityForm) {
        username = new TextField("Username");

        firstName = new TextField("First Name");
        lastName = new TextField("Last Name");
        email = new TextField("Email");

        isActive = new Checkbox("Is Active?");
        isLocked = new Checkbox("Is Locked?");
        isPasswordChangeRequired = new Checkbox("Is Password Change Required?");
        securityPolicies = new TwinColGrid<GxSecurityPolicy>()
                .addFilterableColumn(GxSecurityPolicy::getSecurityPolicyName, "Policy Name", "Policy Name", true)
                .withAvailableGridCaption("Available").withSelectionGridCaption("Selected").withDragAndDropSupport();
        securityPolicies.setSizeFull();

        securityGroups = new TwinColGrid<GxSecurityGroup>()
                .addFilterableColumn(GxSecurityGroup::getSecurityGroupName, "Group Name", "Group Name", true)
                .withAvailableGridCaption("Available").withSelectionGridCaption("Selected").withDragAndDropSupport();
        securityGroups.setSizeFull();

        // Radio Buttons for gender
        // gender = new RadioButtonGroup<>();
        // gender.setLabel("Gender");
        // gender.setItems(GenderEnum.Male, GenderEnum.Female, GenderEnum.Undisclosed);
        // gender.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        // radioGroup.setValue("Option one");
        // add(radioGroup);
        // System.err.println(GenderEnum.valueOf("M"));

        newPassword = new PasswordField("New Password");
        newPassword.setAutocomplete(null);
        newPassword.setValueChangeMode(ValueChangeMode.EAGER);

        confirmPassword = new PasswordField("Confirm Password");
        confirmPassword.setAutocomplete(null);
        newPassword.addValueChangeListener(vcl -> {
            confirmPassword.clear();
            confirmPassword.setEnabled(vcl.getValue().length() > 0);
        });

        imageUploader = new FileUploader("Profile Image");
        imageUploader.setStorage(storage);
        // imageUploader.setAllowedFileTypes("image/png", "image/jpg", "image/jpeg");
        imageUploader.addValueChangeListener(event -> {
            System.err.println(event.getValue());
        });

        entityForm.add(firstName, lastName, email, isActive, isLocked, isPasswordChangeRequired, username, newPassword,
                confirmPassword, imageUploader);

        setColspan(email, 2);
        setColspan(username, 2);
        setColspan(isPasswordChangeRequired, 2);
        setColspan(imageUploader, 2);
    }

    @Override
    protected void bindFields(Binder<GxUserAccount> dataBinder) {
        dataBinder.forMemberField(username).asRequired();
        dataBinder.forMemberField(email).withValidator(new EmailValidator("Must be a valid email address"));
        dataBinder.forMemberField(newPassword).withValidator(new Validator<String>() {

            private static final long serialVersionUID = 1L;

            @Override
            public ValidationResult apply(String value, ValueContext context) {
                if (Strings.isBlank(value))
                    return ValidationResult.ok();
                try {
                    passwordPolicyService.assertPasswordPolicy(getEntity().getNamespace(), getEntity().getUsername(),
                            value);
                    return ValidationResult.ok();
                } catch (AssertionError e) {
                    return ValidationResult.error(e.getMessage());
                }
            }
        });
        dataBinder.forMemberField(confirmPassword).withValidator(new Validator<String>() {

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
        tabItems.add(GxTabItem.create(1, "Policies", securityPolicies));
        tabItems.add(GxTabItem.create(2, "Groups", securityGroups));
        tabItems.add(GxTabItem.create(3, "Access Keys", accessKeyList));
    }

    @Override
    protected void onTabChange(Integer index, Tab tab, com.vaadin.flow.component.Component component) {
        if (index == 3) {
            accessKeyList.initializeWithUserAccount(getEntity());
        }
    }

    @Override
    protected void preBinding(GxUserAccount entity) {
        newPassword.clear();
        confirmPassword.setEnabled(false);
        securityPolicies.setItems(dataService.findSecurityPolicyByNamespaceActive(entity.getNamespace()));
        securityGroups.setItems(dataService.findSecurityGroupByNamespaceActive(entity.getNamespace()));
    }

}
