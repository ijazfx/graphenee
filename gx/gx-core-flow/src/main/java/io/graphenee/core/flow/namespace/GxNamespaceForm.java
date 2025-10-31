package io.graphenee.core.flow.namespace;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.server.streams.DownloadEvent;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;
import com.vaadin.flow.server.streams.InMemoryUploadCallback;
import com.vaadin.flow.server.streams.InputStreamDownloadCallback;
import com.vaadin.flow.server.streams.UploadHandler;
import com.vaadin.flow.server.streams.UploadMetadata;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxNamespaceForm extends GxAbstractEntityForm<GxNamespace> {

    TextField namespace;
    TextArea namespaceDescription;

    TextField appTitle;
    Image appLogo;

    Upload imageUploader;

    Checkbox isActive;

    @Autowired
    private GxNamespacePropertyList properties;

    public GxNamespaceForm() {
        super(GxNamespace.class);
    }

    @Override
    protected void decorateForm(HasComponents form) {
        namespace = new TextField("Namespace");
        namespaceDescription = new TextArea("Description");

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
                        appLogo.setVisible(true);
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

        form.add(namespace, namespaceDescription, appTitle, appLogoDetails,
                isActive);
        expand(namespace, namespaceDescription, appTitle, appLogoDetails, isActive);

        Details propertiesDetails = new Details("Namespace Properties");
        propertiesDetails.setOpened(true);
        propertiesDetails.add(properties);
        form.add(propertiesDetails);
        expand(propertiesDetails);
    }

    @Override
    protected void bindFields(Binder<GxNamespace> dataBinder) {
        dataBinder.forMemberField(namespace).asRequired("Device token is required");
    }

    @Override
    protected void postBinding(GxNamespace entity) {
        properties.initializeWithNamespace(entity);
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

    @Override
    protected String formTitle() {
        return "Namespace";
    }

    @Override
    protected String dialogHeight() {
        return "50rem";
    }

}
