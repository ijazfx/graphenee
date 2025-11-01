package io.graphenee.core.flow.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.GxDataService;
import io.graphenee.core.model.entity.GxDomain;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.component.GxImageUploader;

@SpringComponent
@Scope("prototype")
public class GxDomainForm extends GxAbstractEntityForm<GxDomain> {
    private static final long serialVersionUID = 1L;

    private TextField dns;
    private Checkbox isVerified;
    private TextField txtRecord;

    private TextField appTitle;
    private GxImageUploader appLogo;

    private Checkbox isActive;

    @Autowired
    GxDataService dataService;

    public GxDomainForm() {
        super(GxDomain.class);
    }

    @Override
    protected void decorateForm(HasComponents entityForm) {
        dns = new TextField("DNS (Domain Name)");
        dns.setPlaceholder("e.g. myapp.mydomain.com");
        txtRecord = new TextField("TXT Record");
        txtRecord.setHelperText("Create a TXT Record with this value for verification");
        isVerified = new Checkbox("Is Verified?");

        appTitle = new TextField("Application Title");
        appLogo = new GxImageUploader("Application Logo");

        isActive = new Checkbox("Is Active?");

        entityForm.add(dns, txtRecord, isVerified, appTitle, appLogo, isActive);
        expand(dns, txtRecord, isVerified, appTitle, appLogo, isActive);
    }

    @Override
    protected void bindFields(Binder<GxDomain> dataBinder) {
        dataBinder.forMemberField(dns).withValidator(new Validator<String>() {

            @Override
            public ValidationResult apply(String value, ValueContext context) {
                if (GxDomain.isDnsValid(value)) {
                    return ValidationResult.ok();
                }
                return ValidationResult.error("Not a valid DNS");
            }
        }).asRequired();
    }

    @Override
    protected void postBinding(GxDomain entity) {
        txtRecord.setReadOnly(true);
        isVerified.setReadOnly(true);
    }

}
