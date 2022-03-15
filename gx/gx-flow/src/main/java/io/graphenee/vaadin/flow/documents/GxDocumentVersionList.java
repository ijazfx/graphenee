package io.graphenee.vaadin.flow.documents;

import java.util.Collection;
import java.util.stream.Stream;

import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.binder.PropertyDefinition;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.spring.annotation.SpringComponent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import io.graphenee.core.model.entity.GxDocument;
import io.graphenee.documents.GxDocumentExplorerService;
import io.graphenee.util.storage.FileStorage;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.base.GxAbstractEntityList;

@SpringComponent
@Scope("prototype")
public class GxDocumentVersionList extends GxAbstractEntityList<GxDocument> {

    @Autowired
    GxDocumentExplorerService documentService;

    @Autowired
    GxFileUploadNewVersionForm uploadNewVersionForm;

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
    protected Renderer<GxDocument> rendererForProperty(String propertyName, PropertyDefinition<GxDocument, ?> propertyDefinition) {
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
    protected GxAbstractEntityForm<GxDocument> getEntityForm(GxDocument entity) {
        return null;
    }

    @Override
    protected void onSave(GxDocument entity) {

    }

    @Override
    protected void onDelete(Collection<GxDocument> entities) {

    }

    public void initializeWithDocument(GxDocument document) {
        this.selectedDocument = document;
        refresh();
    }

}
