package io.graphenee.vaadin.flow.component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.server.StreamResource;

import org.springframework.util.StreamUtils;

@Tag("gx-image-uploader")
public class ImageUploader extends AbstractField<ImageUploader, byte[]> {

    private static final long serialVersionUID = 1L;

    private Image preview;
    private Upload upload;

    public ImageUploader() {
        super(new byte[] {});
        VerticalLayout verticalLayout = new VerticalLayout();
        preview = new Image();
        MemoryBuffer buffer = new MemoryBuffer();
        upload = new Upload(buffer);
        upload.setDropLabel(new Label("Drop files here"));
        upload.setAcceptedFileTypes("image/jpeg", "image/png");

        upload.addSucceededListener(event -> {
            try {
                File tempFile = File.createTempFile("ilp", "pi");
                try (FileOutputStream os = new FileOutputStream(tempFile)) {
                    InputStream is = buffer.getInputStream();
                    StreamUtils.copy(is, os);
                    setModelValue(is.readAllBytes(), false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.err.println(tempFile.getAbsolutePath());
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        });
        verticalLayout.add(upload, preview);
    }

    @Override
    protected void setPresentationValue(byte[] newPresentationValue) {
        StreamResource resource = new StreamResource("profileImage.jpg", () -> new ByteArrayInputStream(newPresentationValue));
        preview.setSrc(resource);
    }

}
