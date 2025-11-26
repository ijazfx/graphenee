package io.graphenee.core.flow.documents;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.data.binder.PropertyDefinition;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.model.entity.GxDocument;
import io.graphenee.core.model.entity.GxUserAccount;
import io.graphenee.documents.GxDocumentExplorerService;
import io.graphenee.util.TRFileContentUtil;
import io.graphenee.util.storage.FileStorage;
import io.graphenee.util.storage.FileStorage.FileMetaData;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.GxAbstractEntityList;
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
								|| mimeType.equals(
										"application/vnd.openxmlformats-officedocument.wordprocessingml.document")
								|| mimeType.equals(
										"application/vnd.ms-excel")
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
		GxUserAccount user = (loggedInUser() instanceof GxUserAccount) ? ((GxUserAccount) loggedInUser()) : null;

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
		}, user);
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
		GxUserAccount user = (loggedInUser() instanceof GxUserAccount) ? ((GxUserAccount) loggedInUser()) : null;
		documentService.archiveDocumentVersion(documents, user);
	}

	public void initializeWithDocumentAndStorage(GxDocument document, FileStorage storage) {
		this.selectedDocument = document;
		this.storage = storage;
		refresh();
	}

}
