/*******************************************************************************
 * Copyright (c) 2016, 2018 Farrukh Ijaz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package io.graphenee.vaadin.component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.UUID;
import java.util.function.Function;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Image;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.util.FileTypeResolver;

import io.graphenee.core.storage.FileStorage;
import io.graphenee.core.storage.ResolveFailedException;
import io.graphenee.core.util.TRFileContentUtil;
import io.graphenee.core.util.TRImageUtil;
import io.graphenee.gx.theme.graphenee.GrapheneeTheme;
import io.graphenee.vaadin.ResourcePreviewPanel;
import server.droporchoose.UploadComponent;

public class FileChooser extends CustomField<String> {

	public static final Logger L = LoggerFactory.getLogger(FileChooser.class);

	private static final long serialVersionUID = 1L;
	private Image previewImage;
	private UploadComponent uploadComponent;
	private FileStorage storage;
	private String componentHeight;
	private String componentWidth;
	private ProgressBar progressBar;
	private String uploadedFilePath;
	private String uploadedFileName;
	private Function<String, String> fileNameTranslator;
	private String rootFolder;
	private MButton deleteButton;
	private FileDownloader fileDownloader;

	private MHorizontalLayout imageLayout;

	public String getUploadedFilePath() {
		return uploadedFilePath;
	}

	public String getUploadedFileName() {
		return uploadedFileName;
	}

	public FileChooser() {
		this(null);
	}

	public FileChooser(String caption) {
		this(caption, (fileName) -> {
			return UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(fileName);
		});
	}

	public FileChooser(String caption, Function<String, String> fileNameTranslator) {
		setCaption(caption);
		this.fileNameTranslator = fileNameTranslator;
		componentHeight = "-1px";
		componentWidth = "-1px";
	}

	public void setStorage(FileStorage storage) {
		this.storage = storage;
	}

	public void setFileNameTranslator(Function<String, String> fileNameTranslator) {
		this.fileNameTranslator = fileNameTranslator;
	}

	public String getRootFolder() {
		return rootFolder;
	}

	public void setRootFolder(String rootFolder) {
		this.rootFolder = rootFolder;
		fireValueChange(true);
	}

	private void preview(String filePath) {
		String mimeType = TRFileContentUtil.getMimeType(filePath);
		String extension = TRFileContentUtil.getExtensionFromFilename(filePath);
		if (mimeType.startsWith("image/") || extension.matches("(pdf)")) {
			File file = new File(filePath);
			Resource resource = null;
			if (file.exists()) {
				resource = new FileResource(file);
			} else {
				try {
					String resourcePath = storage.resourcePath(getRootFolder(), filePath);
					InputStream inputStream = storage.resolve(resourcePath);
					InputStreamSource source = new InputStreamSource(inputStream);
					resource = new StreamResource(source, UUID.randomUUID().toString() + "." + TRFileContentUtil.getExtensionFromFilename(filePath)) {
						@Override
						public String getMIMEType() {
							String mimeType = FileTypeResolver.getMIMEType(filePath);
							if (mimeType != null) {
								return mimeType;
							}
							return super.getMIMEType();
						}
					};
				} catch (ResolveFailedException e) {
					resource = null;
				}
			}
			if (fileDownloader == null) {
				ResourcePreviewPanel previewPanel = new ResourcePreviewPanel();
				previewPanel.build();
				previewPanel.preview(resource);
			}
		}
	}

	private void uploadReceived(String inputFileName, Path inputFilePath) {
		String fileName = fileNameTranslator != null ? fileNameTranslator.apply(inputFileName) : inputFileName;
		File targetFile = new File(inputFilePath.getParent().toFile(), fileName);
		if (targetFile.exists()) {
			targetFile.delete();
		}
		inputFilePath.toFile().renameTo(targetFile);
		this.uploadedFilePath = targetFile.getAbsolutePath();
		this.uploadedFileName = inputFileName;
		setValue(uploadedFilePath);

		File compressedFile = null;

		// resize file..
		String mimeType = TRFileContentUtil.getMimeType(uploadedFilePath);
		if (mimeType != null && mimeType.startsWith("image/")) {
			try {
				compressedFile = File.createTempFile(inputFileName, "resized");
				if (!TRImageUtil.resizeImage(new File(uploadedFilePath), compressedFile)) {
					compressedFile = null;
				} else {
					compressedFile.renameTo(targetFile);
				}
			} catch (Exception ex) {
				L.warn("Resize failed so using original file", ex);
			}
		}

		UI.getCurrent().access(() -> {
			String extension = TRFileContentUtil.getExtensionFromFilename(uploadedFilePath);
			if (extension != null)
				extension = extension.toLowerCase();
			Resource resource = null;
			if (!mimeType.startsWith("image/")) {
				previewImage.setHeight("32px");
				previewImage.setWidth("32px");
				resource = GrapheneeTheme.fileExtensionIconResource(extension);
				if (resource == null)
					resource = GrapheneeTheme.fileExtensionIconResource("bin");
			} else {
				previewImage.setWidth("100px");
				previewImage.setHeightUndefined();
				try {
					InputStream inputStream = new FileInputStream(targetFile);
					StreamSource source = new InputStreamSource(inputStream);
					resource = new StreamResource(source, UUID.randomUUID().toString());
				} catch (FileNotFoundException e) {
					resource = null;
				}
			}
			previewImage.setSource(resource);
			previewImage.markAsDirty();
			progressBar.setVisible(false);
			previewImage.setVisible(true);
			UI.getCurrent().push();
		});
	}

	private void uploadStarted(String fileName) {
		UI.getCurrent().access(() -> {
			uploadComponent.setVisible(false);
			imageLayout.setVisible(true);
			previewImage.setVisible(false);
			progressBar.setValue(0F);
			progressBar.setVisible(true);
			UI.getCurrent().push();
		});
	}

	private void uploadProgress(String fileName, long readBytes, long contentLength) {
		UI.getCurrent().access(() -> {
			float value = (float) readBytes / contentLength;
			progressBar.setValue(value);
			UI.getCurrent().push();
		});
	}

	private void uploadFailed(String fileName, Path file) {
		UI.getCurrent().access(() -> {
			uploadComponent.setVisible(true);
			imageLayout.setVisible(false);
			progressBar.setVisible(false);
			previewImage.setVisible(true);
			UI.getCurrent().push();
		});
	}

	public void setComponentHeight(String height) {
		componentHeight = height;
	}

	public void setComponentWidth(String width) {
		componentWidth = width;
		if (uploadComponent != null)
			uploadComponent.setWidth(width);
	}

	@Override
	protected Component initContent() {
		MHorizontalLayout layout = new MHorizontalLayout().withDefaultComponentAlignment(Alignment.TOP_LEFT).withMargin(false).withSpacing(true).withWidthUndefined();
		imageLayout = new MHorizontalLayout().withDefaultComponentAlignment(Alignment.MIDDLE_CENTER).withSpacing(false);
		imageLayout.setHeight(componentHeight);
		progressBar = new ProgressBar();
		progressBar.setIndeterminate(false);
		previewImage = new Image();
		// previewImage.setHeight("32px");
		previewImage.setSource(GrapheneeTheme.UPLOAD_ICON);
		previewImage.addClickListener(event -> {
			String value = getValue();
			if (value != null) {
				preview(value);
			}
		});
		imageLayout.addComponent(progressBar);
		// MVerticalLayout imageContainer1 = new
		// MVerticalLayout().withMargin(false).withSpacing(false).withDefaultComponentAlignment(Alignment.MIDDLE_CENTER)
		// .withWidth(componentHeight).withHeight(componentHeight);
		// imageContainer.addComponent(previewImage);
		// imageLayout.addComponent(imageContainer);
		imageLayout.addComponent(previewImage);

		progressBar.setVisible(false);
		uploadComponent = new UploadComponent();
		uploadComponent.getChoose().setButtonCaption("Choose a file");
		uploadComponent.setWidth(componentWidth);
		uploadComponent.setHeight(componentHeight);
		uploadComponent.setStyleName("dropBoxLayout");

		uploadComponent.setReceivedCallback(this::uploadReceived);
		uploadComponent.setStartedCallback(this::uploadStarted);
		uploadComponent.setProgressCallback(this::uploadProgress);
		uploadComponent.setFailedCallback(this::uploadFailed);

		deleteButton = new MButton(FontAwesome.CLOSE).withListener(event -> {
			setValue(null);
		}).withStyleName(ValoTheme.BUTTON_SMALL, ValoTheme.BUTTON_ICON_ONLY, ValoTheme.BUTTON_QUIET);

		imageLayout.addComponent(deleteButton);
		imageLayout.setComponentAlignment(deleteButton, Alignment.BOTTOM_RIGHT);
		imageLayout.setExpandRatio(deleteButton, 1);

		addValueChangeListener(event -> {
			renderComponent();
		});

		layout.addComponents(uploadComponent, imageLayout);

		uploadComponent.setVisible(false);
		imageLayout.setVisible(false);

		// fireValueChange(true);
		renderComponent();

		return layout;
	}

	private void renderComponent() {
		if (previewImage != null) {
			if (fileDownloader != null) {
				previewImage.removeExtension(fileDownloader);
				fileDownloader = null;
			}
			previewImage.markAsDirtyRecursive();
			String fileName = getValue();
			Resource resource = null;
			if (fileName != null) {
				String mimeType = TRFileContentUtil.getMimeType(fileName);
				String extension = TRFileContentUtil.getExtensionFromFilename(fileName);
				if (!mimeType.startsWith("image/")) {
					resource = GrapheneeTheme.fileExtensionIconResource(extension);
					if (resource == null)
						resource = GrapheneeTheme.fileExtensionIconResource("bin");
					previewImage.setHeight("32px");
					previewImage.setWidth("32px");
					previewImage.setSource(resource);
				} else {
					File file = new File(fileName);
					if (file.isFile() && file.exists()) {
						resource = new FileResource(file);
					} else {
						try {
							String resourcePath = storage.resourcePath(getRootFolder(), fileName);
							InputStream inputStream = storage.resolve(resourcePath);
							InputStreamSource isr = new InputStreamSource(inputStream);
							resource = new StreamResource(isr, fileName);
						} catch (ResolveFailedException e1) {
							resource = GrapheneeTheme.IMAGE_NOT_AVAILBLE;
						}
					}
					previewImage.setWidth("100px");
					previewImage.setHeightUndefined();
					previewImage.setSource(resource);
				}

				if (!mimeType.startsWith("image/") && !extension.matches("(pdf)")) {
					try {
						File file = new File(fileName);
						if (file.isFile() && file.exists()) {
							Resource downloadResource = new FileResource(file);
							if (fileDownloader == null) {
								fileDownloader = new FileDownloader(downloadResource);
								fileDownloader.extend(previewImage);
							}
						} else {
							String resourcePath = storage.resourcePath(getRootFolder(), fileName);
							InputStream inputStream = storage.resolve(resourcePath);
							InputStreamSource isr = new InputStreamSource(inputStream);
							Resource downloadResource = new StreamResource(isr, fileName);
							if (fileDownloader == null) {
								fileDownloader = new FileDownloader(downloadResource);
								fileDownloader.extend(previewImage);
							}
						}
					} catch (ResolveFailedException e1) {
						resource = GrapheneeTheme.IMAGE_NOT_AVAILBLE;
					}
				}

				uploadComponent.setVisible(false);
				imageLayout.setVisible(true);
			} else {
				imageLayout.setVisible(false);
				uploadComponent.setVisible(true);
			}
		}
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		if (deleteButton != null)
			deleteButton.setVisible(!readOnly);
	}

	@Override
	public Class<? extends String> getType() {
		return String.class;
	}

}
