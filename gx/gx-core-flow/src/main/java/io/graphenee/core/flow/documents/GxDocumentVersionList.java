package io.graphenee.core.flow.documents;

import java.io.ByteArrayInputStream;
import java.io.File;
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
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.binder.PropertyDefinition;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.model.entity.GxDocument;
import io.graphenee.documents.GxDocumentExplorerService;
import io.graphenee.util.storage.FileStorage;
import io.graphenee.util.storage.FileStorage.FileMetaData;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.GxAbstractEntityList;
import io.graphenee.vaadin.flow.component.ResourcePreviewPanel;

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
				Icon icon = null;
				if (!s.isFile()) {
					icon = VaadinIcon.FOLDER_O.create();
				} else {
					icon = VaadinIcon.FILE_O.create();
					icon.addClickListener(cl -> {
						GxDocument document = (GxDocument) s;
						String extension = s.getExtension();
						String mimeType = s.getMimeType();
						if (mimeType.startsWith("image") || extension.equals("pdf") || mimeType.startsWith("audio")
								|| mimeType.startsWith("video")) {
							try {
								InputStream stream = null;
								String src = document.getPath();
								String resourcePath = storage.resourcePath("documents", src);
								try {
									stream = storage.resolve(resourcePath);
								} catch (Exception e) {
									e.printStackTrace();
								}
								byte[] bytes = IOUtils.toByteArray(stream);
								StreamResource resource = new StreamResource(document.getName(),
										() -> new ByteArrayInputStream(bytes));
								ResourcePreviewPanel resourcePreviewPanel = new ResourcePreviewPanel(document.getName(),
										resource);
								resourcePreviewPanel.showInDialog();
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					});
				}
				return icon;
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
		uploadNewVersionForm.initializeWithFileUploadHandler((parentDocument, uploadedFile) -> {
			try {
				File file = uploadedFile.getFile();
				Future<FileMetaData> savedFile = storage.save("documents", file.getAbsolutePath());
				FileMetaData metaData = savedFile.get();
				GxDocument d = new GxDocument();
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
		});
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
		documentService.deleteDocument(documents);
	}

	public void initializeWithDocumentAndStorage(GxDocument document, FileStorage storage) {
		this.selectedDocument = document;
		this.storage = storage;
		refresh();
	}

}
