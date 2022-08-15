package io.graphenee.vaadin.flow.component;

import java.util.stream.Stream;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;

import lombok.Setter;

public class GxLazyComboBox<T> extends ComboBox<T> {
	private static final long serialVersionUID = 1L;

	@Setter
	private ComboBoxFetchCallback<T> fetchCallback;

	public GxLazyComboBox(String label) {
		this();
		setLabel(label);
	}

	public GxLazyComboBox() {
		CallbackDataProvider.FetchCallback<T, String> findCallback = new CallbackDataProvider.FetchCallback<T, String>() {

			@Override
			public Stream<T> fetch(Query<T, String> query) {
				return getPagedData(query);
			}
		};

		CallbackDataProvider.CountCallback<T, String> countCallback = new CallbackDataProvider.CountCallback<T, String>() {

			@Override
			public int count(Query<T, String> query) {
				String searchString = query.getFilter().orElse("%");
				if (searchString == null || searchString.trim().length() < 5) {
					return 0;
				}
				if (fetchCallback != null) {
					return fetchCallback.count(searchString);
				}
				return 0;
			}
		};

		CallbackDataProvider<T, String> dataProvider = DataProvider.fromFilteringCallbacks(findCallback, countCallback);

		setDataProvider(dataProvider);
	}

	private Stream<T> getPagedData(Query<T, String> query) {
		int offset = query.getOffset();
		int limit = query.getLimit();
		int pageNumber = offset / limit;
		int remainder = offset % limit == 0 ? 0 : offset - (pageNumber * limit);
		int pageSize = limit;
		Stream<T> stream = getData(pageNumber, pageSize, query.getFilter().orElse("%"));
		if (remainder != 0) {
			Stream<T> nextStream = getData(pageNumber + 1, pageSize, query.getFilter().orElse("%"));
			stream = Stream.concat(stream, nextStream).skip(remainder).limit(limit);
		}
		return stream;
	}

	private Stream<T> getData(int pageNumber, int pageSize, String searchString) {
		if (searchString == null || searchString.trim().length() < 5) {
			return Stream.empty();
		}
		if (fetchCallback != null) {
			return fetchCallback.fetch(pageNumber, pageSize, searchString);
		}
		return Stream.empty();
	}

	public interface ComboBoxFetchCallback<T> {
		Stream<T> fetch(int pageNumber, int pageSize, String searchString);

		int count(String searchString);
	}
}
