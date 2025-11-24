package io.graphenee.vaadin.flow.component;

import java.io.ByteArrayInputStream;

import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;
import com.vaadin.flow.server.streams.UploadHandler;

public class GxImageUploader extends CustomField<byte[]> {

    Upload imageUploader;
    Image image;
    VerticalLayout rootLayout;

    public GxImageUploader(String label) {
        this(label, 2);
    }

    public GxImageUploader(String label, int maxSizeInMegabytes) {
        setLabel(label);
        image = new Image();
        image.setMaxWidth("7rem");
        image.setMaxHeight("7rem");
        imageUploader = new Upload(UploadHandler.inMemory((metadata, fileBytes) -> {
            setValue(fileBytes);
        }));
        imageUploader.setMaxFiles(1);
        imageUploader.setMaxFileSize(maxSizeInMegabytes * 1024000);
        imageUploader.setAcceptedFileTypes("image/*");

        rootLayout = new VerticalLayout();
        rootLayout.setMargin(false);
        rootLayout.setPadding(false);
        rootLayout.add(imageUploader, image);
        add(rootLayout);
    }

    @Override
    protected byte[] generateModelValue() {
        return getValue();
    }

    @Override
    protected void setPresentationValue(byte[] fileBytes) {
        if (fileBytes != null) {
            image.setSrc(DownloadHandler.fromInputStream(de -> {
                return new DownloadResponse(new ByteArrayInputStream(fileBytes), null, null, -1);
            }));
            image.setVisible(true);
        } else {
            image.setVisible(false);
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        imageUploader.setVisible(!readOnly);
    }

}
