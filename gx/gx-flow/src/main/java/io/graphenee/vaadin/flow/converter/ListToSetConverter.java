package io.graphenee.vaadin.flow.converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

@SuppressWarnings("serial")
public class ListToSetConverter<T> implements Converter<Set<T>, List<T>> {

	@Override
	public Result<List<T>> convertToModel(Set<T> value, ValueContext context) {
		return Result.ok(new ArrayList<>(value));
	}

	@Override
	public Set<T> convertToPresentation(List<T> value, ValueContext context) {
		return value == null ? Collections.emptySet() : new HashSet<>(value);
	}

}