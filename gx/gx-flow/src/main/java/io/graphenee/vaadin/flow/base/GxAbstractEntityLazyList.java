package io.graphenee.vaadin.flow.base;

import java.util.stream.Stream;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;

@CssImport(value = "./styles/gx-common.css", themeFor = "vaadin-grid")
public abstract class GxAbstractEntityLazyList<T> extends GxAbstractEntityList<T> {

    private static final long serialVersionUID = 1L;

    public GxAbstractEntityLazyList(Class<T> entityClass) {
        super(entityClass);
        addClassName("gx-abstract-entity-lazy-list");
    }

    @Override
    @SuppressWarnings("unchecked")
    protected DataProvider<T, T> dataProvider(Class<T> entityClass) {
        CallbackDataProvider<T, T> fromFilteringCallbacks = DataProvider.fromFilteringCallbacks(query -> getPagedData(query),
                query -> getTotalCount(query.getFilter().orElse(initializeSearchEntity())));
        return (DataProvider<T, T>) fromFilteringCallbacks.withConfigurableFilter();
    }

    protected abstract int getTotalCount(T probe);

    private Stream<T> getPagedData(Query<T, T> query) {
        int offset = query.getOffset();
        int limit = query.getLimit();
        int pageNumber = offset / limit;
        int remainder = offset % limit == 0 ? 0 : offset - (pageNumber * limit);
        int pageSize = limit;
        Stream<T> stream = getData(pageNumber, pageSize, query.getFilter().orElse(initializeSearchEntity()));
        if (remainder != 0) {
            Stream<T> nextStream = getData(pageNumber + 1, pageSize, query.getFilter().orElse(initializeSearchEntity()));
            stream = Stream.concat(stream, nextStream).skip(remainder).limit(limit);
        }
        return stream;
    }

    @Override
    final protected Stream<T> getData() {
        return null;
    }

    protected abstract Stream<T> getData(int pageNumber, int pageSize, T searchEntity);

    @Override
    protected boolean isGridFilterEnabled() {
        return true;
    }

}
