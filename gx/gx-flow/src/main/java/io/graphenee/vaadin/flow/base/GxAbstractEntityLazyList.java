package io.graphenee.vaadin.flow.base;

import java.util.stream.Stream;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.data.provider.DataProvider;

@CssImport("./styles/gx-entity-lazy-list.css")
public abstract class GxAbstractEntityLazyList<T> extends GxAbstractEntityList<T> {

    private static final long serialVersionUID = 1L;

    public GxAbstractEntityLazyList(Class<T> entityClass) {
        super(entityClass);
    }

    @Override
    protected DataProvider<T, ?> dataProvider(Class<T> entityClass) {
        return DataProvider.fromCallbacks(query -> getPagedData(query.getOffset(), query.getLimit()), query -> getTotalCount());
    }

    protected abstract int getTotalCount();

    private Stream<T> getPagedData(int offset, int limit) {
        int pageNumber = offset / limit;
        int remainder = offset % limit == 0 ? 0 : offset - (pageNumber * limit);
        int pageSize = limit;
        Stream<T> stream = getData(pageNumber, pageSize);
        if (remainder != 0) {
            Stream<T> nextStream = getData(pageNumber + 1, pageSize);
            stream = Stream.concat(stream, nextStream).skip(remainder).limit(limit);
        }
        return stream;
    }

    @Override
    final protected Stream<T> getData() {
        return null;
    }

    protected abstract Stream<T> getData(int pageNumber, int pageSize);

}
