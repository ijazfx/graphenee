package io.graphenee.vaadin.flow.documents;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.PropertyDefinition;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import io.graphenee.core.model.entity.GxDocument;
import io.graphenee.core.model.entity.GxDocumentExplorerItem;
import io.graphenee.core.model.entity.GxFolder;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.documents.GxDocumentExplorerService;
import io.graphenee.util.storage.FileStorage;
import io.graphenee.util.storage.FileStorage.FileMetaData;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.base.GxAbstractEntityTreeList;
import io.graphenee.vaadin.flow.component.GxNotification;
import io.graphenee.vaadin.flow.component.ResourcePreviewPanel;
import io.graphenee.vaadin.flow.utils.IconUtils;

@SpringComponent
@Scope("prototype")
public class GxDocumentExplorer extends GxAbstractEntityTreeList<GxDocumentExplorerItem> {

	@Autowired
	GxDocumentExplorerService documentService;

	@Autowired
	GxDocumentExplorerItemForm form;

	@Autowired
	GxFolderForm folderForm;

	@Autowired
	GxDocumentForm documentForm;

	@Autowired
	GxFileUploadForm uploadForm;

	@Autowired
	GxFileUploadNewVersionForm uploadNewVersionForm;

	@Autowired
	GxDocumentVersionList versionList;

	FileStorage storage;

	private GxNamespace namespace;

	private GxFolder selectedFolder;

	private HorizontalLayout breadcrumbLayout;

	public GxDocumentExplorer() {
		super(GxDocumentExplorerItem.class);
	}

	@Override
	protected int getChildCount(GxDocumentExplorerItem parent, GxDocumentExplorerItem probe) {
		if (parent != null) {
			return documentService.countChildren(parent).intValue();
		}
		return documentService.countChildren(selectedFolder).intValue();
	}

	@Override
	protected boolean hasChildren(GxDocumentExplorerItem parent) {
		if (parent != null) {
			return documentService.countChildren(parent) > 0;
		}
		return documentService.countChildren(selectedFolder) > 0;
	}

	@Override
	protected Stream<GxDocumentExplorerItem> getData(int pageNumber, int pageSize, GxDocumentExplorerItem parent, GxDocumentExplorerItem probe) {
		if (parent != null) {
			return documentService.findExplorerItem(parent, "name").stream();
		}
		return documentService.findExplorerItem(selectedFolder, "name").stream();
	}

	private void generateBreadcrumb(GxDocumentExplorerItem parent) {
		if (breadcrumbLayout == null) {
			breadcrumbLayout = new HorizontalLayout();
		}
		breadcrumbLayout.removeAll();
		GxDocumentExplorerItem current = parent != null ? parent : selectedFolder;
		LinkedList<GxDocumentExplorerItem> list = new LinkedList<>();
		while (current != null) {
			list.addFirst(current);
			current = current.getParent();
		}
		for (int i = 0; i < list.size(); i++) {
			GxDocumentExplorerItem f = list.get(i);
			if (i > 0) {
				breadcrumbLayout.add(VaadinIcon.ANGLE_RIGHT.create());
			}
			Button button;
			if (f.getParent() == null) {
				button = new Button(VaadinIcon.HOME.create());
				button.addThemeVariants(ButtonVariant.LUMO_ICON);
			} else {
				button = new Button(f.getName());
				button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE, ButtonVariant.LUMO_SMALL);
			}
			button.addClickListener(cl -> {
				initializeWithFolderAndStorage((GxFolder) f, storage);
			});
			breadcrumbLayout.add(button);
		}
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "extension", "name", "size", "version" };
	}

	@Override
	protected void decorateColumn(String propertyName, Column<GxDocumentExplorerItem> column) {
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
	protected Renderer<GxDocumentExplorerItem> rendererForProperty(String propertyName, PropertyDefinition<GxDocumentExplorerItem, ?> propertyDefinition) {
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
						if (mimeType.startsWith("image") || extension.equals("pdf") || mimeType.startsWith("audio") || mimeType.startsWith("video")) {
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
								StreamResource resource = new StreamResource(src, () -> new ByteArrayInputStream(bytes));
								ResourcePreviewPanel resourcePreviewPanel = new ResourcePreviewPanel(src, resource);
								resourcePreviewPanel.showInDialog("80%", "80%");
							} catch (Exception e) {
								e.printStackTrace();
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
	protected void customizeAddMenuItem(MenuItem addMenuItem) {
		addMenuItem.getSubMenu().addItem("Create Folder", cl -> {
			GxFolder newFolder = new GxFolder();
			newFolder.setNamespace(namespace);
			if (entityGrid().getSelectedItems().size() == 1) {
				GxDocumentExplorerItem selectedContainer = entityGrid().getSelectedItems().iterator().next();
				if (!selectedContainer.isFile()) {
					newFolder.setFolder((GxFolder) selectedContainer);
				}
			} else {
				newFolder.setFolder(selectedFolder);
			}
			folderForm.showInDialog(newFolder);
		});
		addMenuItem.getSubMenu().addItem("Upload Document", cl -> {
			GxDocumentExplorerItem selectedContainer = selectedFolder;
			if (entityGrid().getSelectedItems().size() == 1) {
				selectedContainer = entityGrid().getSelectedItems().iterator().next();
			}
			if (!selectedContainer.isFile()) {
				uploadForm.showInDialog((GxFolder) selectedContainer);
			}
		});
		addMenuItem.getSubMenu().addItem("Upload New Version", cl -> {
			GxDocumentExplorerItem selectedContainer = null;
			if (entityGrid().getSelectedItems().size() == 1) {
				selectedContainer = entityGrid().getSelectedItems().iterator().next();
				if (selectedContainer.isFile()) {
					uploadNewVersionForm.showInDialog((GxDocument) selectedContainer);
				}
			} else {
				GxNotification.error("Please select at least one document.");
			}
		});
	}

	@Override
	protected void postBuild() {
		folderForm.setDelegate(folder -> {
			documentService.saveFolder(selectedFolder, List.of(folder));
			refresh();
			folderForm.closeDialog();
		});

		uploadForm.initializeWithFileUploadHandler((parentFolder, uploadedFiles) -> {
			uploadedFiles.forEach(uploadedFile -> {
				try {
					File file = uploadedFile.getFile();
					Future<FileMetaData> savedFile = storage.save("documents", file.getAbsolutePath());
					FileMetaData metaData = savedFile.get();
					GxDocument d = new GxDocument();
					d.setFolder(parentFolder);
					d.setSize((long) metaData.getFileSize());
					d.setNamespace(namespace);
					d.setName(uploadedFile.getFileName());
					d.setPath(metaData.getFileName());
					d.setMimeType(uploadedFile.getMimeType());
					documentService.saveDocument(parentFolder, List.of(d));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				refresh();
			});
		});

		uploadNewVersionForm.initializeWithFileUploadHandler((parentDocument, uploadedFile) -> {
			try {
				File file = uploadedFile.getFile();
				Future<FileMetaData> savedFile = storage.save("documents", file.getAbsolutePath());
				FileMetaData metaData = savedFile.get();
				GxDocument d = new GxDocument();
				d.setSize((long) metaData.getFileSize());
				d.setNamespace(namespace);
				d.setName(uploadedFile.getFileName());
				d.setPath(metaData.getFileName());
				d.setMimeType(uploadedFile.getMimeType());
				documentService.createDocumentVersion(parentDocument, d);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			refresh();
		});
	}

	@Override
	protected GxAbstractEntityForm<GxDocumentExplorerItem> getEntityForm(GxDocumentExplorerItem entity) {
		return form;
	}

	@Override
	protected void onSave(GxDocumentExplorerItem entity) {
		documentService.saveExplorerItem(selectedFolder, List.of(entity));
	}

	@Override
	protected void onDelete(Collection<GxDocumentExplorerItem> entities) {
		documentService.deleteExplorerItem(new ArrayList<>(entities));
	}

	@Override
	protected String hierarchyColumnProperty() {
		return "name";
	}

	public void initializeWithNamespaceAndStorage(GxNamespace namespace, FileStorage storage) {
		this.namespace = namespace;
		this.selectedFolder = documentService.findOrCreateNamespaceFolder(namespace);
		this.storage = storage;
		generateBreadcrumb(this.selectedFolder);
		refresh();
	}

	public void initializeWithFolderAndStorage(GxDocumentExplorerItem parent, FileStorage storage) {
		this.selectedFolder = parent.isFile() ? ((GxDocument) parent).getFolder() : (GxFolder) parent;
		this.storage = storage;
		generateBreadcrumb(parent);
		refresh();
	}

	@Override
	protected void onGridItemDoubleClicked(ItemDoubleClickEvent<GxDocumentExplorerItem> icl) {
		GxDocumentExplorerItem item = icl.getItem();
		if (item.isFile()) {
			GxDocument doc = (GxDocument) item;
			while (doc.getDocument() != null) {
				doc = doc.getDocument();
			}
			versionList.initializeWithDocumentAndStorage(doc, storage);
			versionList.showInDialog(() -> {
				refresh();
			});
		} else {
			initializeWithFolderAndStorage(icl.getItem(), storage);
		}
	}

	@Override
	protected void decorateSearchForm(FormLayout searchForm, Binder<GxDocumentExplorerItem> searchBinder) {
		breadcrumbLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		searchForm.add(breadcrumbLayout, 10);
	}

}
