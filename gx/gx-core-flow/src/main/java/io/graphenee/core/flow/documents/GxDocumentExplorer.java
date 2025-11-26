package io.graphenee.core.flow.documents;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu.GridContextMenuItemClickEvent;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.grid.dnd.GridDragStartEvent;
import com.vaadin.flow.component.grid.dnd.GridDropEvent;
import com.vaadin.flow.component.grid.dnd.GridDropLocation;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.PropertyDefinition;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.GxDataService;
import io.graphenee.core.model.entity.GxDocument;
import io.graphenee.core.model.entity.GxDocumentExplorerItem;
import io.graphenee.core.model.entity.GxDocumentFilter;
import io.graphenee.core.model.entity.GxFolder;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxTag;
import io.graphenee.core.model.entity.GxUserAccount;
import io.graphenee.documents.GxDocumentExplorerService;
import io.graphenee.util.TRFileContentUtil;
import io.graphenee.util.callback.TRParamCallback;
import io.graphenee.util.storage.FileStorage;
import io.graphenee.util.storage.FileStorage.FileMetaData;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.GxAbstractEntityForm.EntityFormDelegate;
import io.graphenee.vaadin.flow.GxAbstractEntityTreeList;
import io.graphenee.vaadin.flow.component.DialogFactory;
import io.graphenee.vaadin.flow.component.GxCopyToClipboardButton;
import io.graphenee.vaadin.flow.component.GxDownloadButton;
import io.graphenee.vaadin.flow.component.GxFormLayout;
import io.graphenee.vaadin.flow.component.GxNotification;
import io.graphenee.vaadin.flow.component.ResourcePreviewPanel;
import io.graphenee.vaadin.flow.event.TRDelayMenuClickListener;
import io.graphenee.vaadin.flow.utils.IconUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringComponent
@Scope("prototype")
public class GxDocumentExplorer extends GxAbstractEntityTreeList<GxDocumentExplorerItem> {

	@Autowired
	GxDocumentExplorerService documentService;

	@Autowired
	GxDataService gxDataService;

	@Autowired
	GxDocumentExplorerItemForm form;

	@Autowired
	GxFolderForm folderForm;

	@Autowired
	GxDocumentForm documentForm;

	@Autowired
	GxFileUploadForm uploadForm;

	@Autowired
	GxFileUploadForm quickUploadForm;

	@Autowired
	GxFileUploadNewVersionForm uploadNewVersionForm;

	@Autowired
	GxDocumentVersionList versionList;

	FileStorage storage;

	GxUserAccount loggedInUser;

	private GxNamespace namespace;

	private GxFolder topFolder, selectedFolder;

	private FlexLayout breadcrumbLayout;

	private GxDownloadButton downloadButton;

	private List<GxDocumentExplorerItem> draggedItems;

	private GxDocumentFilter filter;

	private SplitLayout splitLayout = null;

	public GxDocumentExplorer() {
		super(GxDocumentExplorerItem.class);
	}

	@Override
	protected int getChildCount(GxDocumentExplorerItem parent) {
		if (parent != null) {
			return documentService.countChildren(loggedInUser(), parent, getSearchEntity(), filter).intValue();
		}
		return documentService.countChildren(loggedInUser(), selectedFolder, getSearchEntity(), filter).intValue();
	}

	@Override
	protected boolean hasChildren(GxDocumentExplorerItem parent) {
		if (parent != null) {
			return documentService.countChildren(loggedInUser, parent, getSearchEntity(), filter) > 0;
		}
		return documentService.countChildren(loggedInUser, selectedFolder, getSearchEntity(), filter) > 0;
	}

	@Override
	protected Stream<GxDocumentExplorerItem> getData(int pageNumber, int pageSize, GxDocumentExplorerItem parent) {
		if (parent != null) {
			return documentService.findExplorerItem(loggedInUser, parent, getSearchEntity(), filter, "name").stream();
		}
		return documentService.findExplorerItem(loggedInUser, selectedFolder, getSearchEntity(), filter, "name")
				.stream();
	}

	private void generateBreadcrumb(GxDocumentExplorerItem parent) {
		if (breadcrumbLayout == null) {
			breadcrumbLayout = new FlexLayout();
			breadcrumbLayout.addClassName("gx-dms-breadcrumb-layout");
			breadcrumbLayout.setAlignItems(Alignment.CENTER);
		}
		breadcrumbLayout.removeAll();
		GxDocumentExplorerItem current = parent != null ? parent : selectedFolder;
		LinkedList<GxDocumentExplorerItem> list = new LinkedList<>();
		while (current != null) {
			list.addFirst(current);
			if (current.equals(topFolder))
				break;
			current = current.getParent();
		}
		for (int i = 0; i < list.size(); i++) {
			GxDocumentExplorerItem f = list.get(i);
			if (i > 0) {
				Icon icon = VaadinIcon.CHEVRON_RIGHT_SMALL.create();
				icon.setColor("var(--lumo-primary-text-color)");
				icon.setSize("1rem");
				breadcrumbLayout.add(icon);
			}
			Button button;
			if (f.getParent() == null || f.equals(topFolder)) {
				button = new Button(VaadinIcon.HOME.create());
				button.addThemeVariants(ButtonVariant.LUMO_ICON);
			} else {
				button = new Button(f.getName());
				button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE, ButtonVariant.LUMO_SMALL);
			}
			button.addClickListener(cl -> {
				this.selectedFolder = f.isFile() ? ((GxDocument) f).getFolder() : (GxFolder) f;
				this.namespace = topFolder.getNamespace();
				generateBreadcrumb(f);
				refresh();
			});
			breadcrumbLayout.add(button);
		}
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "extension", "name", "version", "ownerName", "tagsJoined", "size", "isReadOnly",
				"expiryDate",
				"expiryReminderInDays", "updatedOn" };
	}

	@Override
	protected void preEdit(GxDocumentExplorerItem entity) {
		setEditable(canEdit(entity));
	}

	public Boolean canEdit(GxDocumentExplorerItem entity) {
		GxUserAccount owner = entity.getOwner();
		if (owner != null && owner.equals(loggedInUser)) {
			return true;
		}
		return entity.getIsReadOnly().equals(Boolean.FALSE);
	}

	@Override
	protected void decorateColumn(String propertyName, Column<GxDocumentExplorerItem> column) {
		if (propertyName.matches("(extension)")) {
			column.setHeader("Type");
			column.setWidth("3rem");
			column.setTextAlign(ColumnTextAlign.CENTER);
		}
		if (propertyName.matches("(tagsJoined)")) {
			column.setHeader("Tags");
			column.setWidth("13rem");
		}
		if (propertyName.matches("(ownerName)")) {
			column.setHeader("Owner");
			column.setWidth("13rem");
		}
		if (propertyName.matches("(updatedAt)")) {
			column.setAutoWidth(true);
		}
	}

	@Override
	protected Renderer<GxDocumentExplorerItem> rendererForProperty(String propertyName,
			PropertyDefinition<GxDocumentExplorerItem, ?> propertyDefinition) {
		if (propertyName.equalsIgnoreCase("extension")) {
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
						previewDocument(s);
					});
				}
				image.setHeight("24px");
				return image;
			});
		}
		return super.rendererForProperty(propertyName, propertyDefinition);
	}

	@Override
	protected AbstractField<?, ?> columnFilterForProperty(String propertyName,
			PropertyDefinition<GxDocumentExplorerItem, Object> propertyDefinition, AbstractField<?, ?> defaultFilter) {
		if (propertyName.equalsIgnoreCase("tagsJoined")) {
			MultiSelectComboBox<GxTag> box = new MultiSelectComboBox<>();
			box.setItems(gxDataService.findTagByNamespace(namespace));
			box.setClearButtonVisible(true);
			box.addValueChangeListener(l -> {
				getSearchEntity().setTags(l.getValue());
				refresh();
			});
			return box;
		}
		return super.columnFilterForProperty(propertyName, propertyDefinition, defaultFilter);
	}

	public void previewDocument(GxDocumentExplorerItem s) {
		GxDocument document = (GxDocument) s;
		String mimeType = s.getMimeType();
		String extension = s.getExtension();
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
				ResourcePreviewPanel resourcePreviewPanel = new ResourcePreviewPanel(s.getName(),
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
	}

	@Override
	protected void decorateGrid(Grid<GxDocumentExplorerItem> dataGrid) {
		super.decorateGrid(dataGrid);
		dataGrid.addComponentColumn(item -> {
			if (!item.isFile()) {
				return null;
			}
			GxCopyToClipboardButton copyButton = new GxCopyToClipboardButton(() -> {
				try {
					GxDocument document = (GxDocument) item;
					String docID = document.getDocumentId().toString();
					String shareKey = document.getShareKey().toString();
					String shareLink = getBaseUrl() + "/api/gx/dms/v1/shared-document?documentId=" + docID
							+ "&shareKey=" + shareKey;
					log.info("File link: {}", shareLink);

					GxNotification.success("Link Copied!");
					return shareLink;

				} catch (Exception e) {
					log.error("Error while getting file link: {}", e.getMessage());
					GxNotification.error(String.format("Error while getting file link: {}", e.getMessage()));
				}
				return "";
			});
			return copyButton;
		}).setFrozenToEnd(true).setAutoWidth(true).setFlexGrow(0);
	}

	public String getBaseUrl() {
		VaadinServletRequest vaadinRequest = VaadinServletRequest.getCurrent();
		if (vaadinRequest == null) {
			throw new IllegalStateException("No Vaadin request available");
		}

		HttpServletRequest request = vaadinRequest.getHttpServletRequest();
		String scheme = request.getScheme(); // http or https
		String serverName = request.getServerName();
		int port = request.getServerPort();

		String baseUrl = scheme + "://" + serverName;

		// Include port only if it's non-standard
		if ((scheme.equals("http") && port != 80) || (scheme.equals("https") && port != 443)) {
			baseUrl += ":" + port;
		}

		return baseUrl;
	}

	@Override
	protected void customizeAddMenuItem(MenuItem addMenuItem) {
		addMenuItem.getSubMenu().addItem("Create Folder", cl -> {
			GxUserAccount user = (loggedInUser() instanceof GxUserAccount) ? ((GxUserAccount) loggedInUser()) : null;
			GxFolder newFolder = new GxFolder();
			newFolder.setNamespace(namespace);
			newFolder.setOwner(user);
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
		setRowDraggable(true);
		folderForm.setDelegate(folder -> {
			documentService.saveFolder(selectedFolder, List.of(folder));
			refresh();
			folderForm.dismiss();
		});

		uploadForm.initializeWithFileUploadHandlerAndUser((parentFolder, uploadedFiles) -> {
			GxUserAccount user = (loggedInUser() instanceof GxUserAccount) ? ((GxUserAccount) loggedInUser()) : null;
			uploadedFiles.forEach(uploadedFile -> {
				try {
					GxDocument d = new GxDocument();
					File file = uploadedFile.getFile();
					FileInputStream fis = new FileInputStream(file);
					String ext = TRFileContentUtil.getExtensionFromFilename(uploadedFile.getFileName());
					Future<FileMetaData> savedFile = storage.save("documents", d.getDocumentId() + "." + ext, fis);
					FileMetaData metaData = savedFile.get();
					d.setOwner(user);
					d.setFolder(parentFolder);
					d.setSize((long) metaData.getFileSize());
					d.setNamespace(parentFolder.getNamespace());
					d.setName(uploadedFile.getFileName());
					d.setPath(metaData.getFileName());
					d.setMimeType(uploadedFile.getMimeType());
					if (uploadedFile.getTags() != null) {
						d.getTags().addAll(uploadedFile.getTags());
					}
					documentService.saveDocument(parentFolder, List.of(d));
				} catch (Exception ex) {
					log.error(ex.getMessage(), ex);
				}
				refresh();
			});
			refresh();
		}, loggedInUser);

		uploadNewVersionForm.initializeWithFileUploadHandlerAndUser((parentDocument, uploadedFile) -> {
			GxUserAccount user = (loggedInUser() instanceof GxUserAccount) ? ((GxUserAccount) loggedInUser()) : null;
			try {
				GxDocument d = new GxDocument();
				File file = uploadedFile.getFile();
				FileInputStream fis = new FileInputStream(file);
				String ext = TRFileContentUtil.getExtensionFromFilename(uploadedFile.getFileName());
				Future<FileMetaData> savedFile = storage.save("documents", d.getDocumentId() + "." + ext, fis);
				FileMetaData metaData = savedFile.get();
				d.setOwner(user);
				d.setSize((long) metaData.getFileSize());
				d.setNamespace(parentDocument.getNamespace());
				d.setName(uploadedFile.getFileName());
				d.setPath(metaData.getFileName());
				d.setMimeType(uploadedFile.getMimeType());
				documentService.createDocumentVersion(parentDocument, d);
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
			}
			refresh();
		}, loggedInUser);
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
		archiveMultiple(entities);
	}

	public void delete(GxDocumentExplorerItem entity) {
		archiveMultiple(List.of(entity));
	}

	public void archiveMultiple(Collection<GxDocumentExplorerItem> entities) {
		int count = 0;
		for (GxDocumentExplorerItem e : entities) {
			Boolean canEdit = canEdit(e);
			if (canEdit) {
				try {
					documentService.archiveExplorerItem(List.of(e), loggedInUser);
				} catch (DataIntegrityViolationException ex) {
					count++;
				}
			} else {
				GxNotification.error("Cannot trash this document due to limited access: " + e.getName());
			}

		}
		if (count > 0) {
			GxNotification.error("Some document(s) are in use hence cannot be moved to trash.");
		}
	}

	@Override
	protected String hierarchyColumnProperty() {
		return "name";
	}

	public void initializeWithNamespaceAndStorageAndSearchListAndLayoutAndUser(GxNamespace namespace,
			FileStorage storage,
			GxDocumentSearchList sList, SplitLayout sLayout, GxUserAccount currentUser) {
		this.topFolder = documentService.findOrCreateNamespaceFolder(namespace);
		this.selectedFolder = this.topFolder;
		this.namespace = namespace;
		this.storage = storage;
		this.splitLayout = sLayout;
		this.loggedInUser = currentUser;
		generateBreadcrumb(this.selectedFolder);
		refresh();
	}

	public void initializeWithFolderAndStorage(GxDocumentExplorerItem parent, FileStorage storage) {
		this.topFolder = parent.isFile() ? ((GxDocument) parent).getFolder() : (GxFolder) parent;
		this.selectedFolder = this.topFolder;
		this.namespace = topFolder.getNamespace();
		this.storage = storage;
		generateBreadcrumb(parent);
		refresh();
	}

	@Override
	protected GxDocumentExplorerItem initializeSearchEntity() {
		GxUserAccount user = (loggedInUser() instanceof GxUserAccount) ? ((GxUserAccount) loggedInUser()) : null;
		GxDocument document = new GxDocument();
		document.setGrants(Set.of(user));
		return document;
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
			versionList.showInDialog("Manage Versions", () -> {
				refresh();
			});
		} else {
			select(icl.getItem());
		}
	}

	public void select(GxDocumentExplorerItem item) {
		this.selectedFolder = (item instanceof GxDocument) ? (GxFolder) item.getParent() : (GxFolder) item;
		generateBreadcrumb(item);
		refresh();
	}

	@Override
	protected boolean shouldShowContextMenu() {
		return true;
	}

	@Override
	protected void decorateContextMenu(GridContextMenu<GxDocumentExplorerItem> contextMenu) {
		super.decorateContextMenu(contextMenu);
		GridMenuItem<GxDocumentExplorerItem> renameItem = contextMenu
				.addItem(new Span(VaadinIcon.EDIT.create(), new Text("Rename")));

		renameItem.addMenuItemClickListener(
				new TRDelayMenuClickListener<GxDocumentExplorerItem, GridMenuItem<GxDocumentExplorerItem>>() {

					@Override
					public void onClick(GridContextMenuItemClickEvent<GxDocumentExplorerItem> event) {
						try {
							GxDocumentExplorerItem item = event.getItem().get();
							form.showInDialog(item);
							form.setDelegate(new EntityFormDelegate<GxDocumentExplorerItem>() {

								@Override
								public void onSave(GxDocumentExplorerItem entity) {
									documentService.saveExplorerItem(selectedFolder, List.of(entity));
									refresh();
								}

							});
						} catch (Throwable e) {
							log.warn(e.getMessage(), e);
							Notification.show(e.getMessage(), 10000, Position.BOTTOM_CENTER)
									.addThemeVariants(NotificationVariant.LUMO_ERROR);
						}
					}

				});
	}

	@Override
	protected void decorateSearchForm(GxFormLayout searchForm, Binder<GxDocumentExplorerItem> searchBinder) {
		searchForm.add(breadcrumbLayout, 10);
	}

	@Override
	protected void decorateToolbarLayout(HorizontalLayout toolbarLayout) {
		downloadButton = new GxDownloadButton("Download");
		downloadButton.getStyle().set("margin", "0");
		downloadButton.setInputStreamFactory(this::downloadFile);
		downloadButton.setEnabled(false);

		Button searchButton = new Button(VaadinIcon.SEARCH.create());
		searchButton.addClickListener(l -> {
			splitLayout.setSplitterPosition(0);
		});
		toolbarLayout.add(downloadButton, searchButton);
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
	protected void onGridItemSelect(SelectionEvent<Grid<GxDocumentExplorerItem>, GxDocumentExplorerItem> event) {
		if (!event.getAllSelectedItems().isEmpty() && event.getAllSelectedItems().size() == 1) {
			GxDocumentExplorerItem d = entityGrid().getSelectedItems().stream().iterator().next();
			downloadButton.setEnabled(d.isFile());
		} else {
			downloadButton.setEnabled(false);
		}
	}

	@Override
	protected void onDrop(GridDropEvent<GxDocumentExplorerItem> event) {
		try {
			if (event.getDropLocation() != GridDropLocation.EMPTY) {
				GxDocumentExplorerItem targetItem = event.getDropTargetItem().orElse(null);
				if (event.getDropLocation() == GridDropLocation.ON_TOP) {
					changeParent(draggedItems, targetItem);
				} else if (event.getDropLocation() == GridDropLocation.ABOVE) {
					positionBefore(draggedItems, targetItem);
				} else if (event.getDropLocation() == GridDropLocation.BELOW) {
					positionAfter(draggedItems, targetItem);
				}
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			draggedItems = null;
		}
	}

	private void positionBefore(List<GxDocumentExplorerItem> items, GxDocumentExplorerItem targetItem) {
		documentService.positionBefore(items, targetItem);
		refresh();
	}

	private void positionAfter(List<GxDocumentExplorerItem> items, GxDocumentExplorerItem targetItem) {
		documentService.positionAfter(items, targetItem);
		refresh();
	}

	private void changeParent(List<GxDocumentExplorerItem> items, GxDocumentExplorerItem targetItem) {
		documentService.changeParent(items, targetItem);
		refresh();
	}

	@Override
	protected void onDragStart(GridDragStartEvent<GxDocumentExplorerItem> event) {
		draggedItems = event.getDraggedItems();
	}

	@Override
	public boolean isDragAndDropEnabled() {
		return true;
	}

	public void chooseSingle(GxDocumentFilter filter, TRParamCallback<GxDocument> onChoose) {
		entityGrid().deselectAll();
		this.filter = filter;
		VerticalLayout fl = new VerticalLayout();
		fl.setPadding(false);
		fl.setSpacing(false);
		fl.setMargin(false);
		fl.setHeight("600px");
		fl.add(this);
		setWidthFull();
		setHeight("600px");
		ConfirmDialog dlg = DialogFactory.customDialog("Choose a Document", this, "Choose", "Dismiss", cb -> {
			Optional<GxDocumentExplorerItem> document = entityGrid().getSelectedItems().stream().findFirst();
			GxDocumentExplorerItem item = document.orElse(null);
			if (item == null || item.isFile()) {
				onChoose.execute((GxDocument) item);
			}
		});
		dlg.setWidth("900px");
		dlg.open();
	}

	public void uploadSingle(GxDocumentFilter filter, TRParamCallback<GxDocument> onChoose) {
		quickUploadForm.initializeWithFileUploadHandlerAndUser((parentFolder, uploadedFiles) -> {
			uploadedFiles.forEach(uploadedFile -> {
				try {
					GxDocument d = new GxDocument();
					File file = uploadedFile.getFile();
					FileInputStream fis = new FileInputStream(file);
					String ext = TRFileContentUtil.getExtensionFromFilename(uploadedFile.getFileName());
					Future<FileMetaData> savedFile = storage.save("documents", d.getDocumentId() + "." + ext, fis);
					FileMetaData metaData = savedFile.get();
					d.setFolder(parentFolder);
					d.setSize((long) metaData.getFileSize());
					d.setNamespace(parentFolder.getNamespace());
					d.setName(uploadedFile.getFileName());
					d.setPath(metaData.getFileName());
					d.setMimeType(uploadedFile.getMimeType());
					documentService.saveDocument(parentFolder, List.of(d));
					if (onChoose != null) {
						onChoose.execute(d);
					}
				} catch (Exception ex) {
					log.error(ex.getMessage(), ex);
				}
			});
		}, loggedInUser);
		quickUploadForm.showInDialog(selectedFolder);
	}

}
