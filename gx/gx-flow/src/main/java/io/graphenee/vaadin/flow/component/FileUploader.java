package io.graphenee.vaadin.flow.component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import org.apache.tika.Tika;
import org.springframework.util.StreamUtils;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;
import com.vaadin.flow.server.streams.UploadHandler;

import io.graphenee.util.TRFileContentUtil;
import io.graphenee.util.storage.FileStorage;
import io.graphenee.util.storage.ResolveFailedException;
import io.graphenee.vaadin.flow.utils.IconUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * A file uploader component.
 */
@Slf4j
public class FileUploader extends CustomField<String> {
	private static final long serialVersionUID = 1L;

	private String[] allowedFileTypes = null;
	private Boolean autoUpload = true;
	private String dropFileLabel = "Drop file(s) here";

	// 5MB
	private int maxFileSize = 5048576;

	private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".pdf", ".doc", ".docx", ".xls", ".xlsx", ".csv",
			".ppt", ".pptx", ".jpg", ".jpeg", ".png", ".gif", ".txt");
	private static final Set<String> ALLOWED_MIME_TYPES = Set.of("application/pdf", "application/msword",
			"application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.ms-excel",
			"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.ms-powerpoint",
			"application/vnd.openxmlformats-officedocument.presentationml.presentation", "text/plain", "text/csv",
			"image/jpeg", "image/png", "image/gif");
	private static final int MAX_HEADER_SIZE = 16 * 1024; // 16 KB for Tika detection

	@Setter
	private String label;
	private Upload upload;
	private String uploadedFilePath;
	private String uploadedFileName;

	@Setter
	private String rootFolder;

	@Setter
	private FileStorage storage;

	private Div output;

	/**
	 * Creates a new instance of this component.
	 */
	public FileUploader() {
		this(null);
	}

	/**
	 * Creates a new instance of this component.
	 * 
	 * @param label The label of the component.
	 */
	public FileUploader(String label) {
		this.label = label;
		build();
	}

	private void build() {
		output = new Div();
		upload = new Upload(UploadHandler.toTempFile((metadata, file) -> {
			String ext = TRFileContentUtil.getExtensionFromFilename(metadata.fileName());
			String storageFileName = UUID.randomUUID().toString() + "." + ext;

			String desiredFileName = metadata.fileName();

			try (InputStream is = new FileInputStream(file)) {
				String lowerFileName = desiredFileName.toLowerCase();
				if (ALLOWED_EXTENSIONS.stream().noneMatch(lowerFileName::endsWith)) {
					throw new IllegalArgumentException("File extension not allowed: " + desiredFileName);
				}
				ByteArrayOutputStream headerOut = new ByteArrayOutputStream();
				byte[] buf = new byte[4096];
				int bytesRead, total = 0;
				while ((bytesRead = is.read(buf)) != -1 && total < MAX_HEADER_SIZE) {
					int toWrite = Math.min(bytesRead, MAX_HEADER_SIZE - total);
					headerOut.write(buf, 0, toWrite);
					total += toWrite;
					if (total >= MAX_HEADER_SIZE) {
						break;
					}
				}
				byte[] headerBuffer = headerOut.toByteArray();

				Tika tika = new Tika();
				String detectedMimeType = tika.detect(headerBuffer, desiredFileName);
				if (detectedMimeType == null || !ALLOWED_MIME_TYPES.contains(detectedMimeType)) {
					throw new IllegalArgumentException("Unsupported or suspicious file type: " + detectedMimeType);
				}

				File tempFile = File.createTempFile("uploaded", ext);
				try (OutputStream os = new FileOutputStream(tempFile)) {
					os.write(headerBuffer); // write initial header buffer
					StreamUtils.copy(is, os); // write remaining content
				}

				uploadedFilePath = tempFile.getAbsolutePath();
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
				throw new RuntimeException(e);
			}
		}));
		upload.setDropLabel(new NativeLabel(dropFileLabel));

		if (allowedFileTypes != null) {
			upload.setAcceptedFileTypes(allowedFileTypes);
		}

		upload.setMaxFileSize(maxFileSize);

		upload.addFileRejectedListener(event -> {
			Paragraph component = new Paragraph();
			showOutput(event.getErrorMessage(), component, output);
		});

		upload.getElement().addEventListener("file-remove", event -> {
			setValue(null);
			output.removeAll();
		});

		if (label != null) {
			add(new NativeLabel(label));
		}

		add(upload, output);
	}

	/**
	 * Sets the allowed file types.
	 * 
	 * @param fileTypes The allowed file types.
	 */
	public void setAllowedFileTypes(String... fileTypes) {
		this.allowedFileTypes = fileTypes;
		upload.setAcceptedFileTypes(allowedFileTypes);
	}

	/**
	 * Sets the maximum number of files.
	 * 
	 * @param maxFiles The maximum number of files.
	 */
	public void setMaxFiles(int maxFiles) {
		upload.setMaxFiles(maxFiles);
	}

	/**
	 * Sets the maximum file size.
	 * 
	 * @param maxFileSize The maximum file size.
	 */
	public void setMaxFileSize(int maxFileSize) {
		this.maxFileSize = maxFileSize;
		upload.setMaxFileSize(this.maxFileSize);
	}

	/**
	 * Sets whether to auto upload.
	 * 
	 * @param autoUpload Whether to auto upload.
	 */
	public void setAutoUpload(Boolean autoUpload) {
		this.autoUpload = autoUpload;
		upload.setAutoUpload(this.autoUpload);
	}

	/**
	 * Sets the drop file label.
	 * 
	 * @param dropFileLabel The drop file label.
	 */
	public void setDropFileLabel(String dropFileLabel) {
		this.dropFileLabel = dropFileLabel;
		upload.setDropLabel(new NativeLabel(dropFileLabel));
	}

	@Override
	protected String generateModelValue() {
		return getValue();
	}

	@Override
	protected void setPresentationValue(String newPresentationValue) {
		if (newPresentationValue != null) {
			Component component = createComponent(newPresentationValue);
			showOutput(null, component, output);
		} else {
			output.removeAll();
		}
	}

	private Component createComponent(String fileName) {
		if (fileName != null) {
			String mimeType = TRFileContentUtil.getMimeType(fileName);
			Image image = new Image();
			image.setWidth("7rem");
			image.setHeight("7rem");
			String resourcePath = storage.resourcePath(rootFolder, fileName);
			if (mimeType.startsWith("image")) {
				try {
					String fileNameOnly = extractFileNameOnly(fileName);
					image.setSrc(DownloadHandler.fromInputStream(de -> {

						try {
							return new DownloadResponse(storage.resolve(resourcePath), fileNameOnly, mimeType, -1);
						} catch (ResolveFailedException e) {
							return new DownloadResponse(new FileInputStream(getValue()), fileNameOnly, mimeType, -1);
						}
					}));
				} catch (Exception e) {
					log.error("Failed to resolve file from storage", e);
				}
			} else {
				String extension = TRFileContentUtil.getExtensionFromFilename(fileName);
				if (mimeType.startsWith("audio")) {
					image = IconUtils.fileExtensionIconResource("audio");
				} else if (mimeType.startsWith("video")) {
					image = IconUtils.fileExtensionIconResource("video");
				} else {
					image = IconUtils.fileExtensionIconResource(extension);
				}
				if (image == null) {
					image = IconUtils.fileExtensionIconResource("bin");
				}
				image.setHeight("48px");
			}
			image.addClickListener(listener -> {
				String src = getValue();
				String fileNameOnly = extractFileNameOnly(src);
				ResourcePreviewPanel resourcePreviewPanel = new ResourcePreviewPanel(fileNameOnly, () -> {
					try {
						return storage.resolve(resourcePath);
					} catch (ResolveFailedException e) {
						return null;
					}
				});
				resourcePreviewPanel.showInDialog();
			});
			return image;
		}
		return null;
	}

	private String extractFileNameOnly(String fileName) {
		int idx = fileName.lastIndexOf(File.separator);
		if (idx == -1)
			idx = 0;
		else
			idx++;
		return fileName.substring(idx);
	}

	private void showOutput(String text, Component content, HasComponents outputContainer) {
		if (text != null) {
			HtmlComponent p = new HtmlComponent(Tag.P);
			p.getElement().setText(text);
			outputContainer.add(p);
		}
		if (content != null) {
			outputContainer.removeAll();
			outputContainer.add(content);
		}
	}

}
