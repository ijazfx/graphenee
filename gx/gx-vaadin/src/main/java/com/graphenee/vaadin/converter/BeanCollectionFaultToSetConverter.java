package com.graphenee.vaadin.converter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.graphenee.core.model.BeanCollectionFault;
import com.vaadin.data.util.converter.Converter;

public class BeanCollectionFaultToSetConverter<T> implements Converter<Set<T>, BeanCollectionFault<T>> {

	private static final long serialVersionUID = 1L;

	@Override
	public BeanCollectionFault<T> convertToModel(Set<T> value, Class<? extends BeanCollectionFault<T>> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		BeanCollectionFault<T> from = BeanCollectionFault.from(value);
		from.markAsModified();
		return from;
	}

	@Override
	public Set<T> convertToPresentation(BeanCollectionFault<T> value, Class<? extends Set<T>> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value == null) {
			return Collections.emptySet();
		}
		Set<T> valueAsSet = new HashSet<>();
		valueAsSet.addAll(value.getBeans());
		return valueAsSet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<BeanCollectionFault<T>> getModelType() {
		return (Class<BeanCollectionFault<T>>) BeanCollectionFault.emptyCollectionFault().getClass();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<Set<T>> getPresentationType() {
		return (Class<Set<T>>) Collections.emptySet().getClass();
	}

}