package io.graphenee.vaadin.flow.base;

import java.util.stream.Stream;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;

import io.reactivex.rxjava3.core.ObservableEmitter;

@CssImport(value = "./styles/gx-common.css", themeFor = "vaadin-grid")
public abstract class GxAbstractEntityLazyList<T> extends GxAbstractEntityList<T> {

    private static final long serialVersionUID = 1L;

    public GxAbstractEntityLazyList(Class<T> entityClass) {
        super(entityClass);
        addClassName("gx-entity-lazy-list");
    }

    @Override
    @SuppressWarnings("unchecked")
    protected DataProvider<T, T> dataProvider(Class<T> entityClass) {
        CallbackDataProvider<T, T> fromFilteringCallbacks = DataProvider.fromFilteringCallbacks(query -> getPagedData(query), query -> {
            int count = getTotalCount(query.getFilter().orElse(getSearchEntity()));
            updateTotalCountFooter(count);
            return count;
        });
        return (DataProvider<T, T>) fromFilteringCallbacks.withConfigurableFilter();
    }

    protected abstract int getTotalCount(T searchEntity);

    private Stream<T> getPagedData(Query<T, T> query) {
        int offset = query.getOffset();
        int limit = query.getLimit();
        int pageNumber = offset / limit;
        int remainder = offset % limit == 0 ? 0 : offset - (pageNumber * limit);
        int pageSize = limit;
        Stream<T> stream = getData(pageNumber, pageSize, query.getFilter().orElse(getSearchEntity()));
        if (remainder != 0) {
            Stream<T> nextStream = getData(pageNumber + 1, pageSize, query.getFilter().orElse(getSearchEntity()));
            stream = Stream.concat(stream, nextStream).skip(remainder).limit(limit);
        }
        return stream;
    }

    @Override
    final protected Stream<T> getData() {
        return null;
    }

    protected abstract Stream<T> getData(int pageNumber, int pageSize, T searchEntity);

    protected void exportData(ObservableEmitter<T> emitter) {
        Stream<T> data = null;
        if (!entityGrid().getSelectedItems().isEmpty()) {
            data = entityGrid().getSelectedItems().stream();
            data.forEach(d -> {
                if (emitter.isDisposed()) {
                    return;
                }
                emitter.onNext(d);
            });
            emitter.onComplete();
        } else {
            int count = getTotalCount(getSearchEntity());
            if (count > 0) {
                int pages = count / 100;
                for (int i = 0; i < pages; i++) {
                    data = getData(i, 100, getSearchEntity());
                    data.forEach(d -> {
                        if (emitter.isDisposed()) {
                            return;
                        }
                        emitter.onNext(d);
                    });
                }
                int remaining = count - (pages * 100);
                if (remaining > 0) {
                    data = getData(pages, 100, getSearchEntity());
                    data.forEach(d -> {
                        if (emitter.isDisposed()) {
                            return;
                        }
                        emitter.onNext(d);
                    });
                }
                emitter.onComplete();
            }
        }
    }

    @Override
    protected boolean isGridFilterEnabled() {
        return true;
    }

}
