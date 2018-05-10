package com.graphenee.vaadin.converter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.vaadin.data.util.converter.Converter;

@SuppressWarnings({ "serial", "rawtypes" })
public class SetToCollectionConverter implements Converter<Set, Collection> {

	@Override
	public Collection convertToModel(Set value, Class<? extends Collection> targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
		return value;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public Set convertToPresentation(Collection value, Class<? extends Set> targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value == null)
			return null;
		HashSet hashSet = new HashSet<>();
		value.iterator().forEachRemaining(item -> {
			hashSet.add(item);
		});
		return hashSet;
	}

	@Override
	public Class<Collection> getModelType() {
		return Collection.class;
	}

	@Override
	public Class<Set> getPresentationType() {
		return Set.class;
	}

}
