package io.graphenee.core.flow.documents;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vaadin.flow.component.notification.Notification;
import org.apache.tika.Tika;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.upload.Receiver;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.model.entity.GxFolder;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxFileUploadForm extends GxAbstractEntityForm<GxFolder> {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".pdf", ".doc", ".docx", ".xls", ".xlsx", ".csv", ".ppt", ".pptx", ".jpg", ".jpeg", ".png", ".gif", ".txt");
    private static final Set<String> ALLOWED_MIME_TYPES = Set.of("application/pdf", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", "text/plain", "text/csv", "image/jpeg", "image/png", "image/gif");

	Upload upload;

	List<GxUploadedFile> uploadedFiles = new ArrayList<>();

	public GxFileUploadForm() {
		super(GxFolder.class);
	}

	@Override
	protected void decorateForm(HasComponents entityForm) {
		upload = new Upload(new Receiver() {

			@Override
			public OutputStream receiveUpload(String fileName, String mimeType) {
				try {
                    String lowerName = fileName.toLowerCase();
                    if (ALLOWED_EXTENSIONS.stream().noneMatch(lowerName::endsWith)) {
                        throw new IOException("File extension not allowed: " + fileName);
                    }

                    return new OutputStream() {
                        private static final int MAX_HEADER_SIZE = 16 * 1024; // read first 16 KB
                        private final ByteArrayOutputStream headerBuffer = new ByteArrayOutputStream();
                        private OutputStream diskOut;
                        private boolean checked = false;
                        private File tempFile;

                        @Override
                        public void write(int b) throws IOException {
                            write(new byte[]{(byte) b}, 0, 1);
                        }

                        @Override
                        public void write(@NotNull byte[] b, int off, int len) throws IOException {
                            if (!checked) {
                                int remaining = MAX_HEADER_SIZE - headerBuffer.size();
                                int toBuffer = Math.min(remaining, len);
                                headerBuffer.write(b, off, toBuffer);

                                // When enough bytes are buffered, perform MIME check
                                if (headerBuffer.size() >= MAX_HEADER_SIZE) {
                                    validateAndStartWriting();
                                }

                                // If already validated and there’s leftover data
                                if (checked && len > toBuffer) {
                                    diskOut.write(b, off + toBuffer, len - toBuffer);
                                }
                            } else {
                                // Already validated — write directly
                                diskOut.write(b, off, len);
                            }
                        }

                        @Override
                        public void close() throws IOException {
                            if (!checked) {
                                validateAndStartWriting(); // check small files
                            }
                            if (diskOut != null) diskOut.close();
                        }

                        private void validateAndStartWriting() throws IOException {
                            checked = true;
                            Tika tika = new Tika();
                            String detectedMimeType = tika.detect(headerBuffer.toByteArray(), fileName);

                            if (detectedMimeType == null || !ALLOWED_MIME_TYPES.contains(detectedMimeType)) {
                                throw new IOException("Unsupported or suspicious file type: " + detectedMimeType);
                            }

                            // Passed check — create temp file and start writing
                            tempFile = File.createTempFile("uploaded", fileName);
                            GxUploadedFile uploadedFile = new GxUploadedFile();
                            uploadedFile.setFile(tempFile);
                            uploadedFile.setFileName(fileName);
					        uploadedFile.setMimeType(mimeType);
                            uploadedFiles.add(uploadedFile);
                            diskOut = new FileOutputStream(tempFile);
                            headerBuffer.writeTo(diskOut);
						}
					};
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}

		});
		upload.setMaxFiles(10);
		upload.setMaxFileSize(1024000000);

        upload.addSucceededListener(event -> Notification.show("Upload OK: " + event.getFileName()));
        upload.addFailedListener(event -> Notification.show("Upload failed: " + event.getReason().getMessage()));
		entityForm.add(upload);
	}

	public void initializeWithFileUploadHandler(GxFileUploadHandler handler) {
		setDelegate(dlg -> {
			handler.onSave(dlg, uploadedFiles);
		});
	}

	@Override
	protected void preBinding(GxFolder entity) {
		uploadedFiles.clear();
	}

	@Override
	protected String dialogHeight() {
		return "100%";
	}

	@Override
	protected String dialogWidth() {
		return "100%";
	}

	@Override
	protected String formTitleProperty() {
		return "name";
	}

	public static interface GxFileUploadHandler {
		void onSave(GxFolder parentFolder, List<GxUploadedFile> uploadedFiles);
	}

}
