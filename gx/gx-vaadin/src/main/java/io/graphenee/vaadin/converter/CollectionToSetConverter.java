package io.graphenee.vaadin.converter;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.vaadin.data.util.converter.Converter;

public class CollectionToSetConverter<T> implements Converter<Set<T>, Collection<T>> {

	private static final long serialVersionUID = 1L;

	@Override
	public Collection<T> convertToModel(Set<T> value, Class<? extends Collection<T>> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		return value;
	}

	@Override
	public Set<T> convertToPresentation(Collection<T> value, Class<? extends Set<T>> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value == null) {
			return Collections.emptySet();
		}
		Set<T> valueAsSet = new HashSet<>();
		valueAsSet.addAll(value);
		return valueAsSet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<Collection<T>> getModelType() {
		return (Class<Collection<T>>) Collections.emptyList().getClass().getSuperclass();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<Set<T>> getPresentationType() {
		return (Class<Set<T>>) Collections.emptySet().getClass();
	}

}