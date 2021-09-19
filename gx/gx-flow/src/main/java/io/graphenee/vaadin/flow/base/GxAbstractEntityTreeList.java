package io.graphenee.vaadin.flow.base;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.hierarchy.AbstractBackEndHierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery;

@CssImport(value = "./styles/gx-common.css", themeFor = "vaadin-grid")
public abstract class GxAbstractEntityTreeList<T> extends GxAbstractEntityList<T> {

    private static final long serialVersionUID = 1L;

    public GxAbstractEntityTreeList(Class<T> entityClass) {
        super(entityClass);
        addClassName("gx-abstract-entity-tree-list");
    }

    @Override
    @SuppressWarnings("unchecked")
    protected DataProvider<T, T> dataProvider(Class<T> entityClass) {
        DataProvider<T, T> dataProvider = new AbstractBackEndHierarchicalDataProvider<T, T>() {
            private static final long serialVersionUID = 1L;

            @Override
            public int getChildCount(HierarchicalQuery<T, T> query) {
                return GxAbstractEntityTreeList.this.getChildCount(query.getParent(), query.getFilter().orElse(initializeSearchEntity()));
            }

            @Override
            public boolean hasChildren(T item) {
                return GxAbstractEntityTreeList.this.hasChildren(item);
            }

            @Override
            protected Stream<T> fetchChildrenFromBackEnd(HierarchicalQuery<T, T> query) {
                return getData(query.getParent(), query.getFilter().orElse(initializeSearchEntity()));
            }
        };
        return (DataProvider<T, T>) dataProvider.withConfigurableFilter();
    }

    protected abstract int getChildCount(T parent, T probe);

    protected abstract boolean hasChildren(T parent);

    protected abstract Stream<T> getData(T parent, T probe);

    @Override
    final protected Stream<T> getData() {
        return null;
    }

    @Override
    protected Grid<T> dataGrid(Class<T> entityClass) {
        GxTreeGrid<T> treeGrid = new GxTreeGrid<>(entityClass);
        List<Column<T>> removeList = new ArrayList<>(treeGrid.getColumns());
        List<Column<T>> keepList = new ArrayList<>();
        for (String key : visibleProperties()) {
            Column<T> column = treeGrid.getColumnByKey(key);
            if (column != null) {
                column.setVisible(true);
                removeList.remove(column);
                keepList.add(column);
            }
        }
        removeList.forEach(c -> treeGrid.removeColumn(c));
        treeGrid.setColumnOrder(keepList);
        treeGrid.setHierarchyColumn(hierarchyColumnProperty());
        return treeGrid;
    }

    protected String hierarchyColumnProperty() {
        return visibleProperties()[0];
    }

}
