package io.graphenee.vaadin.flow.component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.util.StreamUtils;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.server.StreamResource;

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
        upload.setAcceptedFileTypes("image/jpeg", "image/png");

        upload.addSucceededListener(event -> {
            try {
                File tempFile = File.createTempFile("ilp", "pi");
                try (FileOutputStream os = new FileOutputStream(tempFile)) {
                    InputStream is = buffer.getInputStream();
                    StreamUtils.copy(is, os);
                    setModelValue(is.readAllBytes(), false);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.err.println(tempFile.getAbsolutePath());
            } catch (IOException e1) {
                // TODO Auto-generated catch block
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
