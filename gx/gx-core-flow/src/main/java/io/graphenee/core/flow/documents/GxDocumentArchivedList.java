package io.graphenee.core.flow.documents;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.data.binder.PropertyDefinition;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.GxDataService;
import io.graphenee.core.model.entity.GxDocument;
import io.graphenee.core.model.entity.GxDocumentExplorerItem;
import io.graphenee.core.model.entity.GxFolder;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxTag;
import io.graphenee.core.model.entity.GxUserAccount;
import io.graphenee.core.model.jpa.repository.GxTagRepository;
import io.graphenee.documents.GxDocumentExplorerService;
import io.graphenee.documents.scheduler.DocumentSchedulerService;
import io.graphenee.util.storage.FileStorage;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.GxAbstractEntityLazyList;
import io.graphenee.vaadin.flow.component.DialogFactory;
import io.graphenee.vaadin.flow.component.GxNotification;
import io.graphenee.vaadin.flow.component.ResourcePreviewPanel;
import io.graphenee.vaadin.flow.utils.IconUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxDocumentArchivedList extends GxAbstractEntityLazyList<GxDocumentExplorerItem> {

    @Autowired
    GxDocumentExplorerService documentService;

    @Autowired
    GxDataService dataService;

    @Autowired
    GxFolderDetailList folderDetailList;

    @Autowired
    GxTagRepository tagRepository;

    @Autowired
    DocumentSchedulerService schedulerService;

    FileStorage storage;

    GxUserAccount loggedInUser;

    private GxNamespace namespace;

    private MenuItem restoreMenuItem;

    private GxFolder topFolder;

    public GxDocumentArchivedList() {
        super(GxDocumentExplorerItem.class);
    }

    @Override
    protected int getTotalCount(GxDocumentExplorerItem searchEntity) {
        return documentService.countAllArchivedItems(loggedInUser, searchEntity, topFolder, namespace);
    }

    @Override
    protected Stream<GxDocumentExplorerItem> getData(int pageNumber, int pageSize, GxDocumentExplorerItem searchEntity,
            List<QuerySortOrder> sortOrders) {
        return documentService.findAllArchivedItems(loggedInUser, searchEntity, topFolder, namespace).stream();
    }

    @Override
    protected String[] visibleProperties() {
        return new String[] { "extension", "name", "version", "ownerName", "tagsJoined", "size",
                "expiryDate", "updatedAt" };
    }

    @Override
    protected GxAbstractEntityForm<GxDocumentExplorerItem> getEntityForm(GxDocumentExplorerItem entity) {
        return null;
    }

    @Override
    protected void onSave(GxDocumentExplorerItem entity) {

    }

    @Override
    protected void onDelete(Collection<GxDocumentExplorerItem> entities) {
        deleteMultiple(entities);
    }

    @Override
    protected void customizeDeleteMenuItem(MenuItem deleteMenuItem) {
        deleteMenuItem.addClickListener(l -> {
            Set<GxDocumentExplorerItem> selectedItems = entityGrid().getSelectedItems();
            if (!selectedItems.isEmpty()) {
                DialogFactory.questionDialog("Confirmation", "Are you sure to delete selected record(s) permanently?",
                        dlg -> {
                            try {
                                deleteMultiple(selectedItems);
                                refresh();
                            } catch (Exception e) {
                                log.warn(e.getMessage(), e);
                                Notification.show(e.getMessage(), 10000, Position.BOTTOM_CENTER)
                                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                            }
                        }).open();
            }
        });
    }

    public void deleteMultiple(Collection<GxDocumentExplorerItem> entities) {
        for (GxDocumentExplorerItem e : entities) {
            try {
                documentService.deleteExplorerItem(List.of(e), loggedInUser);
            } catch (Exception ex) {
                GxNotification.error("Error while deleting: " + e.getName());
            }
        }
    }

    @Override
    protected boolean isGridFilterEnabled() {
        return true;
    }

    @Override
    protected void onGridItemDoubleClicked(ItemDoubleClickEvent<GxDocumentExplorerItem> icl) {
        if (!icl.getItem().isFile()) {
            GxFolder folder = (GxFolder) icl.getItem();
            folderDetailList.initializeWithFolder(folder);
            folderDetailList.setEditable(false);
            folderDetailList.showInDialog("Folder Content", () -> {
                refresh();
            });
        }
    }

    @Override
    protected AbstractField<?, ?> columnFilterForProperty(String propertyName,
            PropertyDefinition<GxDocumentExplorerItem, Object> propertyDefinition, AbstractField<?, ?> defaultFilter) {
        if (propertyName.equalsIgnoreCase("tagsJoined")) {
            MultiSelectComboBox<GxTag> box = new MultiSelectComboBox<>();
            box.setItems(dataService.findTagByNamespace(namespace));
            box.setClearButtonVisible(true);
            box.addValueChangeListener(l -> {
                getSearchEntity().setTags(l.getValue());
                refresh();
            });
            return box;
        }
        return super.columnFilterForProperty(propertyName, propertyDefinition, defaultFilter);
    }

    @Override
    protected GxDocumentExplorerItem initializeSearchEntity() {
        GxUserAccount user = (loggedInUser() instanceof GxUserAccount) ? ((GxUserAccount) loggedInUser()) : null;
        GxDocument document = new GxDocument();
        document.setGrants(Set.of(user));
        return document;
    }

    @Override
    protected void decorateColumn(String propertyName, Grid.Column<GxDocumentExplorerItem> column) {
        if (propertyName.matches("(extension)")) {
            column.setHeader("Type");
            column.setWidth("4.5rem");
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
    protected void decorateGrid(Grid<GxDocumentExplorerItem> dataGrid) {
        super.decorateGrid(dataGrid);
        dataGrid.addColumn(new ValueProvider<GxDocumentExplorerItem, Object>() {
            @Override
            public Object apply(GxDocumentExplorerItem item) {
                String path = item.getRelativePath().replace(item.getName(), "");
                return path;
            };
        }).setHeader("Relative Path");
        dataGrid.setEmptyStateText("No results found.");
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

    public void previewDocument(GxDocumentExplorerItem s) {
        GxDocument document = (GxDocument) s;
        String mimeType = s.getMimeType();
        String extension = s.getExtension();
        if (mimeType.startsWith("image") || extension.equals("pdf") || mimeType.startsWith("audio")
                || mimeType.startsWith("video")) {
            try {
                String src = document.getPath();
                String resourcePath = storage.resourcePath("documents", src);
                ResourcePreviewPanel resourcePreviewPanel = new ResourcePreviewPanel(s.getName(), () -> {
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
    protected void decorateMenuBar(MenuBar menuBar) {
        restoreMenuItem = menuBar.addItem(VaadinIcon.REFRESH.create(), l -> {
            List<GxDocumentExplorerItem> items = entityGrid().getSelectedItems().stream().toList();

            if (!items.isEmpty()) {
                DialogFactory.questionDialog("Confirmation", "Are you sure to restore selected record(s)?", dlg -> {
                    try {
                        documentService.restoreExplorerItem(items, loggedInUser);
                        refresh();
                    } catch (Exception e) {
                        log.warn(e.getMessage(), e);
                        Notification.show(e.getMessage(), 10000, Position.BOTTOM_CENTER)
                                .addThemeVariants(NotificationVariant.LUMO_ERROR);
                    }
                }).open();
            }

        });
        restoreMenuItem.setEnabled(false);
    }

    @Override
    protected void onGridItemSelect(SelectionEvent<Grid<GxDocumentExplorerItem>, GxDocumentExplorerItem> event) {
        restoreMenuItem.setEnabled(!event.getAllSelectedItems().isEmpty());
    }

    @Override
    protected boolean shouldShowImportDataMenu() {
        return false;
    }

    @Override
    protected void customizeAddMenuItem(MenuItem addMenuItem) {
        addMenuItem.setVisible(false);
    }

    public void initializeByNamespace(GxNamespace namespace) {
        this.getSearchEntity().setNamespace(namespace);
    }

    public void initializeWithNamespaceAndStorageAndSearchListAndLayoutAndUser(GxNamespace namespace,
            FileStorage storage, GxUserAccount currentUser) {
        this.topFolder = documentService.findOrCreateNamespaceFolder(namespace);
        this.namespace = namespace;
        this.storage = storage;
        this.loggedInUser = currentUser;
        refresh();
    }

}
