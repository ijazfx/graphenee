package io.graphenee.vaadin.flow.base;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.hierarchy.AbstractBackEndHierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery;

public abstract class GxAbstractEntityTreeList<T> extends GxAbstractEntityList<T> {

    private static final long serialVersionUID = 1L;

    public GxAbstractEntityTreeList(Class<T> entityClass) {
        super(entityClass);
        addClassName("gx-abstract-entity-tree-list");
    }

    @Override
    protected DataProvider<T, ?> dataProvider(Class<T> entityClass) {
        DataProvider<T, Void> dataProvider = new AbstractBackEndHierarchicalDataProvider<T, Void>() {
            private static final long serialVersionUID = 1L;

            @Override
            public int getChildCount(HierarchicalQuery<T, Void> query) {
                return GxAbstractEntityTreeList.this.getChildCount(query.getParent());
            }

            @Override
            public boolean hasChildren(T item) {
                return GxAbstractEntityTreeList.this.hasChildren(item);
            }

            @Override
            protected Stream<T> fetchChildrenFromBackEnd(HierarchicalQuery<T, Void> query) {
                return getData(query.getParent());
            }
        };

        return dataProvider;
    }

    protected abstract int getChildCount(T parent);

    protected abstract boolean hasChildren(T parent);

    protected abstract Stream<T> getData(T parent);

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
        treeGrid.setHierarchyColumn(visibleProperties()[0]);
        return treeGrid;
    }

}
