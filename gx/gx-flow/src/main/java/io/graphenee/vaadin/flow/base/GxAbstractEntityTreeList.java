package io.graphenee.vaadin.flow.base;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.data.binder.Binder;
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
                return getPagedData(query);
            }

            @Override
            public boolean isInMemory() {
                return true;
            }
        };
        return (DataProvider<T, T>) dataProvider.withConfigurableFilter();
    }

    private Stream<T> getPagedData(HierarchicalQuery<T, T> query) {
        int offset = query.getOffset();
        int limit = query.getLimit();
        int pageNumber = offset / limit;
        int remainder = offset % limit == 0 ? 0 : offset - (pageNumber * limit);
        int pageSize = limit;
        Stream<T> stream = getData(pageNumber, pageSize, query.getParent(), query.getFilter().orElse(initializeSearchEntity()));
        if (remainder != 0) {
            Stream<T> nextStream = getData(pageNumber + 1, pageSize, query.getParent(), query.getFilter().orElse(initializeSearchEntity()));
            stream = Stream.concat(stream, nextStream).skip(remainder).limit(limit);
        }
        return stream;
    }

    protected abstract int getChildCount(T parent, T searchEntity);

    protected abstract boolean hasChildren(T parent);

    protected abstract Stream<T> getData(int pageNumber, int pageSize, T parent, T searchEntity);

    @Override
    final protected Stream<T> getData() {
        return null;
    }

    @Override
    protected Grid<T> dataGrid(Class<T> entityClass) {
        GxTreeGrid<T> treeGrid = new GxTreeGrid<>(entityClass);
        if (isGridInlineEditingEnabled()) {
            Binder<T> editBinder = new Binder<>(entityClass);
            treeGrid.getEditor().setBinder(editBinder);
        }
        List<Column<T>> removeList = new ArrayList<>(treeGrid.getColumns());
        for (String key : availableProperties()) {
            Column<T> column = treeGrid.getColumnByKey(key);
            if (column != null) {
                column.setVisible(true);
                removeList.remove(column);
            }
        }
        removeList.forEach(c -> treeGrid.removeColumn(c));
        treeGrid.setHierarchyColumn(hierarchyColumnProperty());
        return treeGrid;
    }

    protected String hierarchyColumnProperty() {
        return visibleProperties()[0];
    }

}
