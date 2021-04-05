package io.graphenee.vaadin.flow.component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.server.StreamResource;

import org.apache.commons.io.IOUtils;
import org.springframework.util.StreamUtils;

import io.graphenee.core.util.TRFileContentUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
public class FileUploader extends CustomField<String> {
    private static final long serialVersionUID = 1L;

    private String[] allowedFileTypes = null;

    @Setter
    @Getter
    private String label;

    private Upload upload;

    @Getter
    private String uploadedFilePath;

    @Getter
    private String uploadedFileName;

    @Getter
    private String uploadedFileMimeType;

    public FileUploader(String label) {
        this.label = label;
        build();
    }

    public FileUploader() {
        build();
    }

    private void build() {
        Div output = new Div();
        MemoryBuffer buffer = new MemoryBuffer();
        upload = new Upload(buffer);
        upload.setDropLabel(new Label("Drop files here"));

        if (allowedFileTypes != null) {
            upload.setAcceptedFileTypes(allowedFileTypes);
        }

        upload.addSucceededListener(event -> {
            Component component = createComponent(event.getMIMEType(), event.getFileName(), buffer.getInputStream());
            showOutput(null, component, output);

            String ext = TRFileContentUtil.getExtensionFromFilename(event.getFileName());
            String storageFileName = UUID.randomUUID().toString() + "." + ext;

            String desiredFileName = event.getFileName();

            try {
                File tempFile = File.createTempFile("uploaded", ext);
                uploadedFilePath = tempFile.getAbsolutePath();
                uploadedFileName = event.getFileName();
                uploadedFileMimeType = event.getMIMEType();

                try (FileOutputStream os = new FileOutputStream(tempFile)) {
                    InputStream is = buffer.getInputStream();
                    StreamUtils.copy(is, os);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                File receivedFile = new File(uploadedFilePath);
                File newFile = new File(receivedFile.getParent(), storageFileName);
                receivedFile.renameTo(newFile);
                receivedFile = newFile;
                uploadedFilePath = receivedFile.getAbsolutePath();
                uploadedFileName = desiredFileName;

                uploadedFileName = new File(uploadedFilePath).getName();
                String uploadedFileExtension = TRFileContentUtil.getExtensionFromFilename(uploadedFileName);
                if (!desiredFileName.endsWith(uploadedFileExtension)) {
                    uploadedFileName = desiredFileName + "." + uploadedFileExtension;
                } else {
                    uploadedFileName = desiredFileName;
                }

                setValue(uploadedFilePath);

            } catch (IOException e) {
                log.warn(e.getMessage());
            }

        });

        upload.addFileRejectedListener(event -> {
            Paragraph component = new Paragraph();
            showOutput(event.getErrorMessage(), component, output);
        });
        if (label != null) {
            add(new Label(label));
        }

        add(upload, output);
    }

    public void setAllowedFileTypes(String... fileTypes) {
        this.allowedFileTypes = fileTypes;
        upload.setAcceptedFileTypes(allowedFileTypes);
    }

    @Override
    protected String generateModelValue() {
        return null;
    }

    @Override
    protected void setPresentationValue(String newPresentationValue) {

    }

    private Component createComponent(String mimeType, String fileName, InputStream stream) {
        if (mimeType.startsWith("image")) {
            Image image = new Image();
            try {
                byte[] bytes = IOUtils.toByteArray(stream);
                image.getElement().setAttribute("src", new StreamResource(fileName, () -> new ByteArrayInputStream(bytes)));
                try (ImageInputStream in = ImageIO.createImageInputStream(new ByteArrayInputStream(bytes))) {
                    final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
                    if (readers.hasNext()) {
                        ImageReader reader = readers.next();
                        try {
                            reader.setInput(in);
                            image.setWidth("100px");
                            image.setHeight("100px");
                        } finally {
                            reader.dispose();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }
        return null;
    }

    private void showOutput(String text, Component content, HasComponents outputContainer) {
        if (text != null) {
            HtmlComponent p = new HtmlComponent(Tag.P);
            p.getElement().setText(text);
            outputContainer.add(p);
        }
        if (content != null) {
            outputContainer.add(content);
        }
    }

}
