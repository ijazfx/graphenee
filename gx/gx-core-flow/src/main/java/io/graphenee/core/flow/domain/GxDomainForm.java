package io.graphenee.core.flow.domain;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.server.streams.DownloadEvent;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;
import com.vaadin.flow.server.streams.InMemoryUploadCallback;
import com.vaadin.flow.server.streams.InputStreamDownloadCallback;
import com.vaadin.flow.server.streams.UploadHandler;
import com.vaadin.flow.server.streams.UploadMetadata;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.GxDataService;
import io.graphenee.core.model.entity.GxDomain;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.component.GxCopyToClipboardWrapper;

@SpringComponent
@Scope("prototype")
public class GxDomainForm extends GxAbstractEntityForm<GxDomain> {
    private static final long serialVersionUID = 1L;

    private TextField dns;
    private Checkbox isVerified;
    private TextField txtRecord;

    TextField appTitle;
    Image appLogo;

    Upload imageUploader;

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
        appLogo = new Image();

        HorizontalLayout appLogoContainer = new HorizontalLayout();
        appLogoContainer.setWidth("7rem");
        appLogoContainer.setHeight("7rem");

        appLogoContainer.add(appLogo);

        imageUploader = new Upload(UploadHandler.inMemory(new InMemoryUploadCallback() {

            @Override
            public void complete(UploadMetadata metadata, byte[] data) throws IOException {
                getEntity().setAppLogo(data);
                appLogo.setSrc(DownloadHandler.fromInputStream(new InputStreamDownloadCallback() {

                    @Override
                    public DownloadResponse complete(DownloadEvent downloadEvent) throws IOException {
                        if (getEntity().getAppLogo() == null)
                            return DownloadResponse.error(404);
                        return new DownloadResponse(new ByteArrayInputStream(getEntity().getAppLogo()), null, null,
                                getEntity().getAppLogo().length);
                    }

                }));
            }

        }));

        imageUploader.setAcceptedFileTypes(".png");
        imageUploader.setMaxFiles(1);

        isActive = new Checkbox("Is Active?");

        Details appLogoDetails = new Details("Application Logo");
        appLogoDetails.setOpened(true);
        appLogoDetails.add(imageUploader, appLogoContainer);

        entityForm.add(dns, txtRecord, isVerified, appTitle, appLogoDetails, isActive);
        expand(dns, txtRecord, isVerified, appTitle, appLogoDetails, isActive);
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

        appLogo.setSrc(DownloadHandler.fromInputStream(new InputStreamDownloadCallback() {

            @Override
            public DownloadResponse complete(DownloadEvent downloadEvent) throws IOException {
                if (entity.getAppLogo() == null)
                    return DownloadResponse.error(404);
                appLogo.setVisible(true);
                return new DownloadResponse(new ByteArrayInputStream(entity.getAppLogo()), null, null,
                        entity.getAppLogo().length);
            }

        }));
        appLogo.setVisible(entity.getAppLogo() != null);
    }

}
