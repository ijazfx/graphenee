package io.graphenee.vaadin.flow.doc_mgmt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.PropertyDefinition;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.spring.annotation.SpringComponent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import io.graphenee.core.model.entity.GxDocument;
import io.graphenee.core.model.entity.GxDocumentExplorerItem;
import io.graphenee.core.model.entity.GxFolder;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.documents.GxDocumentExplorerService;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.base.GxAbstractEntityTreeList;

@SpringComponent
@Scope("prototype")
public class GxDocumentExplorer extends GxAbstractEntityTreeList<GxDocumentExplorerItem> {

    @Autowired
    GxDocumentExplorerService documentService;

    @Autowired
    GxFolderForm folderForm;

    @Autowired
    GxDocumentForm documentForm;

    private GxNamespace namespace;

    private GxDocumentExplorerItem selectedItem;

    public GxDocumentExplorer() {
        super(GxDocumentExplorerItem.class);
    }

    @Override
    protected int getChildCount(GxDocumentExplorerItem parent, GxDocumentExplorerItem probe) {
        if (parent != null) {
            return documentService.countChildren(parent).intValue();
        }
        if (selectedItem != null) {
            return documentService.countChildren(selectedItem).intValue();
        }
        return documentService.countChildren(namespace).intValue();
    }

    @Override
    protected boolean hasChildren(GxDocumentExplorerItem parent) {
        if (parent != null) {
            return documentService.countChildren(parent) > 0;
        }
        if (selectedItem != null) {
            return documentService.countChildren(selectedItem) > 0;
        }
        return documentService.countChildren(namespace) > 0;
    }

    @Override
    protected Stream<GxDocumentExplorerItem> getData(GxDocumentExplorerItem parent, GxDocumentExplorerItem probe) {
        if (parent != null) {
            return documentService.findExplorerItem(parent, "name").stream();
        }
        if (selectedItem != null) {
            return documentService.findExplorerItem(selectedItem, "name").stream();
        }
        return documentService.findExplorerItem(namespace, "name").stream();
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
                Icon icon = null;
                if (!s.isFile()) {
                    icon = VaadinIcon.FOLDER_O.create();
                } else {
                    icon = VaadinIcon.FILE_O.create();
                }
                return icon;
            });
        }
        return super.rendererForProperty(propertyName, propertyDefinition);
    }

    @Override
    protected void customizeAddMenuItem(MenuItem addMenuItem) {
        addMenuItem.setText("Create");
        addMenuItem.getSubMenu().addItem("Folder", cl -> {
            folderForm.setDelegate(folder -> {
                documentService.saveFolder(List.of(folder));
                refresh();
                folderForm.closeDialog();
            });
            GxFolder newFolder = new GxFolder();
            newFolder.setNamespace(namespace);
            if (selectedItem != null) {
                newFolder.setFolder((GxFolder) selectedItem);
            }
            if (entityGrid().getSelectedItems().size() == 1) {
                GxDocumentExplorerItem selectedContainer = entityGrid().getSelectedItems().iterator().next();
                if (!selectedContainer.isFile()) {
                    newFolder.setFolder((GxFolder) selectedContainer);
                }
            }
            folderForm.showInDialog(newFolder);
        });
        addMenuItem.getSubMenu().addItem("Document", cl -> {
            documentForm.setDelegate(document -> {
                documentService.saveDocument(List.of(document));
                refresh();
                documentForm.closeDialog();
            });
            GxDocument newDocument = new GxDocument();
            newDocument.setNamespace(namespace);
            if (selectedItem != null) {
                newDocument.setFolder((GxFolder) selectedItem);
            }
            if (entityGrid().getSelectedItems().size() == 1) {
                GxDocumentExplorerItem selectedContainer = entityGrid().getSelectedItems().iterator().next();
                if (!selectedContainer.isFile()) {
                    newDocument.setFolder((GxFolder) selectedContainer);
                }
            }
            documentForm.showInDialog(newDocument);
        });
    }

    @Override
    protected void decorateToolbarLayout(HorizontalLayout toolbarLayout) {
        MenuBar navBar = new MenuBar();
        navBar.addThemeVariants(MenuBarVariant.LUMO_ICON);
        navBar.addItem(VaadinIcon.ARROW_UP.create(), cl -> {
            initializeWithNamespace(namespace);
        });

        navBar.addItem(VaadinIcon.ARROW_LEFT.create(), cl -> {
            initializeWithDocumentExplorerItem(selectedItem != null ? selectedItem.getParent() : null);
        });

        toolbarLayout.addComponentAtIndex(0, navBar);
    }

    @Override
    protected GxAbstractEntityForm<GxDocumentExplorerItem> getEntityForm(GxDocumentExplorerItem entity) {
        return null;
    }

    @Override
    protected void onSave(GxDocumentExplorerItem entity) {
        documentService.saveExplorerItem(List.of(entity));
    }

    @Override
    protected void onDelete(Collection<GxDocumentExplorerItem> entities) {
        documentService.deleteExplorerItem(new ArrayList<>(entities));
    }

    @Override
    protected String hierarchyColumnProperty() {
        return "name";
    }

    public void initializeWithNamespace(GxNamespace namespace) {
        this.namespace = namespace;
        this.selectedItem = null;
        refresh();
    }

    public void initializeWithDocumentExplorerItem(GxDocumentExplorerItem item) {
        this.selectedItem = item;
        refresh();
    }

    @Override
    protected void onGridItemDoubleClicked(ItemDoubleClickEvent<GxDocumentExplorerItem> icl) {
      initializeWithDocumentExplorerItem(icl.getItem());
    }

}
