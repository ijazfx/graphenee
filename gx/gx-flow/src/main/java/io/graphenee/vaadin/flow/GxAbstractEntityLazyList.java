package io.graphenee.vaadin.flow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;

import io.reactivex.rxjava3.core.ObservableEmitter;

@CssImport(value = "./styles/graphenee.css", themeFor = "vaadin-grid")
public abstract class GxAbstractEntityLazyList<T> extends GxAbstractEntityList<T> {

	private static final long serialVersionUID = 1L;

	private List<QuerySortOrder> sortOrders = new ArrayList<>();

	public GxAbstractEntityLazyList(Class<T> entityClass) {
		super(entityClass);
		addClassName("gx-entity-lazy-list");
	}

	@Override
	@SuppressWarnings("unchecked")
	protected DataProvider<T, T> dataProvider(Class<T> entityClass) {
		CallbackDataProvider<T, T> fromFilteringCallbacks = DataProvider.fromFilteringCallbacks(query -> {
			sortOrders.clear();
			sortOrders.addAll(query.getSortOrders());
			return getPagedData(query, sortOrders);
		}, query ->

		{
			int count = getTotalCount(query.getFilter().orElse(getSearchEntity()));
			updateTotalCountFooter(count);
			return count;
		});
		return (DataProvider<T, T>) fromFilteringCallbacks.withConfigurableFilter();
	}

	protected abstract int getTotalCount(T searchEntity);

	private Stream<T> getPagedData(Query<T, T> query, List<QuerySortOrder> sortOrders) {
		int offset = query.getOffset();
		int limit = query.getLimit();
		int pageNumber = offset / limit;
		int remainder = offset % limit == 0 ? 0 : offset - (pageNumber * limit);
		int pageSize = limit;
		Stream<T> stream = getData(pageNumber, pageSize, query.getFilter().orElse(getSearchEntity()), sortOrders);
		if (remainder != 0) {
			Stream<T> nextStream = getData(pageNumber + 1, pageSize, query.getFilter().orElse(getSearchEntity()), sortOrders);
			stream = Stream.concat(stream, nextStream).skip(remainder).limit(limit);
		}
		return stream;
	}

	@Override
	final protected Stream<T> getData() {
		return null;
	}

	protected Stream<T> getData(int pageNumber, int pageSize, T searchEntity, List<QuerySortOrder> sortOrders) {
		return getData(pageNumber, pageSize, searchEntity);
	}

	/**
	 * @deprecated use {@link #getData(int, int, Object, List)} instead.
	 */
	@Deprecated
	protected Stream<T> getData(int pageNumber, int pageSize, T searchEntity) {
		return null;
	}

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
					data = getData(i, 100, getSearchEntity(), sortOrders);
					data.forEach(d -> {
						if (emitter.isDisposed()) {
							return;
						}
						emitter.onNext(d);
					});
				}
				int remaining = count - (pages * 100);
				if (remaining > 0) {
					data = getData(pages, 100, getSearchEntity(), sortOrders);
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

	/**
	 * @deprecated use {@link #createSort(List, Sort)} or {@link #createSort(List, Sort, Map)} instead.
	 */
	@Deprecated
	protected List<Order> sortOrdersToSpringOrders(List<QuerySortOrder> sortOrders) {
		return sortOrders.stream().map(so -> {
			return new Order(so.getDirection() == SortDirection.ASCENDING ? Direction.ASC : Direction.DESC, so.getSorted());
		}).collect(Collectors.toList());
	}

	protected Sort createSort(List<QuerySortOrder> sortOrders, Sort defaultSort) {
		return createSort(sortOrders, defaultSort, Collections.emptyMap());
	}

	protected Sort createSort(List<QuerySortOrder> sortOrders, Sort defaultSort, Map<String, String> keyPropertyMap) {
		List<Order> orders = sortOrders.stream().map(so -> {
			String propertyName = keyPropertyMap.getOrDefault(so.getSorted(), so.getSorted());
			return new Order(so.getDirection() == SortDirection.ASCENDING ? Direction.ASC : Direction.DESC, propertyName);
		}).collect(Collectors.toList());
		if (orders.isEmpty() && defaultSort != null) {
			return defaultSort;
		}
		return Sort.by(orders);
	}

	@Override
	protected boolean isGridFilterEnabled() {
		return true;
	}

}
