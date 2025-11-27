package io.graphenee.core.flow.documents;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.PropertyDefinition;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.model.entity.GxDocument;
import io.graphenee.core.model.entity.GxDocumentExplorerItem;
import io.graphenee.core.model.entity.GxUserAccount;
import io.graphenee.documents.GxDocumentExplorerService;
import io.graphenee.util.TRFileContentUtil;
import io.graphenee.util.storage.FileStorage;
import io.graphenee.util.storage.FileStorage.FileMetaData;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.GxAbstractEntityList;
import io.graphenee.vaadin.flow.component.GxDownloadButton;
import io.graphenee.vaadin.flow.component.ResourcePreviewPanel;
import io.graphenee.vaadin.flow.utils.IconUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxDocumentVersionList extends GxAbstractEntityList<GxDocument> {

	@Autowired
	GxDocumentExplorerService documentService;

	@Autowired
	GxFileUploadNewVersionForm uploadNewVersionForm;

	@Autowired
	GxDocumentForm form;

	@Autowired
	GxFileUploadNewVersionForm uploadForm;

	FileStorage storage;

	private GxDocument selectedDocument;

	private GxDownloadButton downloadButton;

	GxUserAccount loggedInUser;

	public GxDocumentVersionList() {
		super(GxDocument.class);
	}

	@Override
	protected Stream<GxDocument> getData() {
		return documentService.findDocumentVersion(selectedDocument).stream();
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "extension", "name", "size", "version" };
	}

	@Override
	protected void decorateColumn(String propertyName, Column<GxDocument> column) {
		if (propertyName.equals("extension")) {
			column.setHeader("");
			column.setAutoWidth(false);
			column.setWidth("50px");
			column.setTextAlign(ColumnTextAlign.CENTER);
		}
		if (propertyName.equals("name")) {
			column.setAutoWidth(false);
			column.setWidth("350px");
		}
	}

	@Override
	protected void decorateToolbarLayout(HorizontalLayout toolbarLayout) {
		downloadButton = new GxDownloadButton("Download");
		downloadButton.getStyle().set("margin", "0");
		downloadButton.setInputStreamFactory(this::downloadFile);
		downloadButton.setEnabled(false);
		toolbarLayout.add(downloadButton);
		super.decorateToolbarLayout(toolbarLayout);
	}

	public InputStream downloadFile() {
		if (!entityGrid().getSelectedItems().isEmpty()) {
			GxDocumentExplorerItem d = entityGrid().getSelectedItems().stream().iterator().next();
			if (d.isFile()) {
				GxDocument document = (GxDocument) d;
				downloadButton.setDefaultFileName(d.getName());
				try {
					InputStream stream = null;
					String src = document.getPath();
					String resourcePath = storage.resourcePath("documents", src);
					stream = storage.resolve(resourcePath);
					byte[] bytes = IOUtils.toByteArray(stream);
					document.audit(loggedInUser, "DOWNLOADED");
					documentService.saveDocument(document);
					return new ByteArrayInputStream(bytes);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	@Override
	protected Renderer<GxDocument> rendererForProperty(String propertyName,
			PropertyDefinition<GxDocument, ?> propertyDefinition) {
		if (propertyName.equals("extension")) {
			return new ComponentRenderer<>(s -> {
				Image image = null;
				String extension = s.getExtension();
				String mimeType = s.getMimeType();
				if (!s.isFile()) {
					image = IconUtils.fileExtensionIconResource("folder");
				} else {
					if (mimeType.startsWith("image")) {
						image = IconUtils.fileExtensionIconResource("image");
					} else if (mimeType.startsWith("audio")) {
						image = IconUtils.fileExtensionIconResource("audio");
					} else if (mimeType.startsWith("video")) {
						image = IconUtils.fileExtensionIconResource("video");
					} else {
						image = IconUtils.fileExtensionIconResource(extension);
					}
					if (image == null) {
						image = IconUtils.fileExtensionIconResource("bin");
					}

					image.addClickListener(cl -> {
						GxDocument document = (GxDocument) s;
						if (mimeType.startsWith("image") || extension.equals("pdf")
								|| extension.startsWith(
										"xls")
								|| extension.startsWith(
										"doc")
								|| mimeType.startsWith("audio")
								|| mimeType.startsWith("video")) {
							try {
								String src = document.getPath();
								String resourcePath = storage.resourcePath("documents", src);
								ResourcePreviewPanel resourcePreviewPanel = new ResourcePreviewPanel(document.getName(),
										() -> {
											try {
												return storage.resolve(resourcePath);
											} catch (Exception ex) {
												return null;
											}
										});
								resourcePreviewPanel.showInDialog();
							} catch (Exception e) {
								log.error("Failed to resolve file from storage", e);
							}

						}
					});
				}
				image.setHeight("24px");
				return image;
			});
		}
		return super.rendererForProperty(propertyName, propertyDefinition);
	}

	@Override
	protected GxAbstractEntityForm<GxDocument> getEntityForm(GxDocument entity) {
		return form;
	}

	@Override
	protected void customizeAddMenuItem(MenuItem addMenuItem) {
		addMenuItem.getSubMenu().addItem("Upload New Version", cl -> {
			uploadNewVersionForm.showInDialog(selectedDocument);
		});
	}

	@Override
	protected void postBuild() {
		uploadNewVersionForm.initializeWithFileUploadHandlerAndUser((parentDocument, uploadedFile) -> {
			try {
				GxDocument d = new GxDocument();
				File file = uploadedFile.getFile();
				FileInputStream fis = new FileInputStream(file);
				String ext = TRFileContentUtil.getExtensionFromFilename(uploadedFile.getFileName());
				Future<FileMetaData> savedFile = storage.save("documents", d.getDocumentId() + "." + ext, fis);
				FileMetaData metaData = savedFile.get();
				d.setSize((long) metaData.getFileSize());
				d.setNamespace(parentDocument.getNamespace());
				d.setName(uploadedFile.getFileName());
				d.setPath(metaData.getResourcePath());
				d.setMimeType(uploadedFile.getMimeType());
				documentService.createDocumentVersion(parentDocument, d);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			refresh();
		}, loggedInUser);
	}

	@Override
	protected void onGridItemSelect(SelectionEvent<Grid<GxDocument>, GxDocument> event) {
		downloadButton.setEnabled(!event.getAllSelectedItems().isEmpty() && event.getAllSelectedItems().size() == 1);
	}

	@Override
	protected void onSave(GxDocument entity) {

	}

	@Override
	protected void onDelete(Collection<GxDocument> entities) {
		List<GxDocument> documents = new ArrayList<>();
		entities.forEach(e -> {
			if (e.getDocument() != null) {
				documents.add(e);
			}
		});
		documentService.archiveDocumentVersion(documents, loggedInUser);
	}

	public void initializeWithDocumentAndStorageAndUser(GxDocument document, FileStorage storage, GxUserAccount user) {
		this.selectedDocument = document;
		this.storage = storage;
		this.loggedInUser = user;
		refresh();
	}

}
