package io.graphenee.core.flow.documents;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.PropertyDefinition;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;

import ch.qos.logback.core.util.StringUtil;
import io.graphenee.core.GxDataService;
import io.graphenee.core.model.entity.GxDocument;
import io.graphenee.core.model.entity.GxDocumentExplorerItem;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxTag;
import io.graphenee.documents.GxDocumentExplorerService;
import io.graphenee.util.storage.FileStorage;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.GxAbstractEntityLazyList;
import io.graphenee.vaadin.flow.component.ResourcePreviewPanel;
import io.graphenee.vaadin.flow.utils.IconUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxDocumentSearchList extends GxAbstractEntityLazyList<GxDocumentExplorerItem> {

    @Autowired
    GxDocumentExplorerService documentService;

    @Autowired
    GxDataService dataService;

    @Setter
    private SplitLayout splitLayout = null;

    @Setter
    FileStorage storage = null;

    @Setter
    GxDocumentExplorer explorer = null;

    private MultiSelectComboBox<GxTag> tagBox = new MultiSelectComboBox<>("Search by Tags");

    public GxDocumentSearchList() {
        super(GxDocumentExplorerItem.class);
    }

    @Override
    protected int getTotalCount(GxDocumentExplorerItem searchEntity) {
        if (StringUtil.isNullOrEmpty(searchEntity.getName()) && searchEntity.getTags().isEmpty()) {
            return 0;
        }
        return documentService.countAll(searchEntity).intValue();
    }

    @Override
    protected Stream<GxDocumentExplorerItem> getData(int pageNumber, int pageSize, GxDocumentExplorerItem searchEntity,
            List<QuerySortOrder> sortOrders) {
        if (StringUtil.isNullOrEmpty(searchEntity.getName()) && searchEntity.getTags().isEmpty()) {
            return Stream.empty();
        }
        return documentService.findAll(searchEntity).stream();
    }

    @Override
    protected String[] visibleProperties() {
        return new String[] { "extension", "name", "tagsJoined" };
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

    }

    @Override
    protected boolean isGridFilterEnabled() {
        return false;
    }

    @Override
    protected GxDocumentExplorerItem initializeSearchEntity() {
        return new GxDocument();
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

    @Override
    protected void decorateToolbarLayout(HorizontalLayout toolbarLayout) {
        TextField searchBar = new TextField("Search by Name");
        searchBar.setClearButtonVisible(true);
        searchBar.addValueChangeListener(l -> {
            getSearchEntity().setName(l.getValue());
            refresh();
        });

        tagBox.addValueChangeListener(l -> {
            getSearchEntity().setTags(l.getValue());
            refresh();
        });

        Button searchButton = new Button("Search");
        searchButton.addClickListener(l -> {
            refresh();
        });
        Button closeButton = new Button("Close");
        closeButton.addClickListener(l -> {
            splitLayout.setSplitterPosition(100);
        });
        toolbarLayout.add(searchBar, tagBox, searchButton, closeButton);
        toolbarLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
    }

    @Override
    protected void onGridItemClicked(ItemClickEvent<GxDocumentExplorerItem> icl) {
        super.onGridItemClicked(icl);
        GxDocumentExplorerItem item = icl.getItem();
        explorer.select(item);
        // explorer.refresh();
        splitLayout.setSplitterPosition(100);
    }

    public void previewDocument(GxDocumentExplorerItem s) {
        // TODO Auto-generated method stub
        GxDocument document = (GxDocument) s;
        String mimeType = s.getMimeType();
        String extension = s.getExtension();
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
                StreamResource resource = new StreamResource(document.getName(), () -> new ByteArrayInputStream(bytes));
                ResourcePreviewPanel resourcePreviewPanel = new ResourcePreviewPanel(s.getName(), resource);
                resourcePreviewPanel.showInDialog();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void initializeByNamespace(GxNamespace namespace) {
        this.getSearchEntity().setNamespace(namespace);
        tagBox.setItems(dataService.findTagByNamespace(namespace));
    }

}
