package io.graphenee.core.flow.namespace;

import java.util.Collection;
import java.util.stream.Stream;

import org.springframework.context.annotation.Scope;

import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxNamespaceProperty;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.GxAbstractEntityList;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxNamespacePropertyList extends GxAbstractEntityList<GxNamespaceProperty> {

    private GxNamespace namespace;

    public GxNamespacePropertyList() {
        super(GxNamespaceProperty.class);
    }

    @Override
    protected Stream<GxNamespaceProperty> getData() {
        return namespace.getProperties().stream()
                .sorted((a, b) -> a.getPropertyKey().compareToIgnoreCase(b.getPropertyKey()));
    }

    @Override
    protected String[] visibleProperties() {
        return new String[] { "namespace", "namespaceDescription", "isActive", "isProtected" };
    }

    @Override
    protected GxAbstractEntityForm<GxNamespaceProperty> getEntityForm(GxNamespaceProperty entity) {
        return null;
    }

    @Override
    protected void onSave(GxNamespaceProperty entity) {
        namespace.addToProperties(entity);
    }

    @Override
    protected void onDelete(Collection<GxNamespaceProperty> entities) {
        entities.forEach(e -> namespace.removeFromProperty(e));
    }

    @Override
    protected boolean isGridInlineEditingEnabled() {
        return true;
    }

    @Override
    protected void preEdit(GxNamespaceProperty entity) {
        if(entity.getOid() == null) {
            entity.setNamespace(namespace);
        }
    }

    @Override
    protected boolean shouldShowExportDataMenu() {
        return false;
    }

    public void initializeWithNamespace(GxNamespace namespace) {
        this.namespace = namespace;
    }

}
