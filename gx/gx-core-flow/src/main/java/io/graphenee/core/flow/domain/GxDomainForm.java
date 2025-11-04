package io.graphenee.core.flow.domain;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.GxDataService;
import io.graphenee.core.model.entity.GxDomain;
import io.graphenee.util.DnsUtils;
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
        Button verifyDomain = new Button("Verify Domain");
        verifyDomain.addClickListener(cl -> {
            Set<String> entries = DnsUtils.dnsTxtRecord(dns.getValue());
            Span verificationMessage = new Span();
            verificationMessage.getStyle().setColor("var(--lumo-secondary-color)");
            if (entries.contains(txtRecord.getValue())) {
                isVerified.setValue(true);
                verificationMessage.setText("Domain is verified");
                dns.setHelperComponent(verificationMessage);
            } else {
                isVerified.setValue(false);
                verificationMessage.setText("Domain could not be verified");
                dns.setHelperComponent(verificationMessage);
            }
        });

        dns.addValueChangeListener(vcl -> {
            isVerified.setValue(false);
        });

        appTitle = new TextField("Application Title");
        appLogo = new GxImageUploader("Application Logo");

        isActive = new Checkbox("Is Active?");

        HorizontalLayout verificationLayout = new HorizontalLayout();
        verificationLayout.add(txtRecord, verifyDomain, isVerified);
        verificationLayout.setAlignItems(Alignment.BASELINE);
        verificationLayout.expand(txtRecord);

        entityForm.add(dns, verificationLayout, appTitle, appLogo, isActive);
        expand(dns, verificationLayout, appTitle, appLogo, isActive);
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
        dns.setHelperComponent(null);
    }

}
