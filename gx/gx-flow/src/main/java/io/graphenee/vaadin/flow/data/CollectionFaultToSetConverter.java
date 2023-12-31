package io.graphenee.vaadin.flow.data;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import io.graphenee.core.model.CollectionFault;

public class CollectionFaultToSetConverter<T> implements Converter<Set<T>, CollectionFault<T>> {

	private static final long serialVersionUID = 1L;

	@Override
	public Result<CollectionFault<T>> convertToModel(Set<T> value, ValueContext context) {
		CollectionFault<T> from = CollectionFault.from(value);
		from.markAsModified();
		return Result.ok(from);
	}

	@Override
	public Set<T> convertToPresentation(CollectionFault<T> value, ValueContext context) {
		if (value == null) {
			return Collections.emptySet();
		}
		return new HashSet<>(value.getCollection());
	}

}
