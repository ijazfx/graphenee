/*******************************************************************************
 * Copyright (c) 2016, 2018 Farrukh Ijaz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package io.graphenee.vaadin.converter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.vaadin.data.util.converter.Converter;

import io.graphenee.core.model.CollectionFault;

public class CollectionFaultToSetConverter<T> implements Converter<Set<T>, CollectionFault<T>> {

	private static final long serialVersionUID = 1L;

	@Override
	public CollectionFault<T> convertToModel(Set<T> value, Class<? extends CollectionFault<T>> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		CollectionFault<T> from = CollectionFault.from(value);
		from.markAsModified();
		return from;
	}

	@Override
	public Set<T> convertToPresentation(CollectionFault<T> value, Class<? extends Set<T>> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value == null) {
			return Collections.emptySet();
		}
		Set<T> valueAsSet = new HashSet<>();
		valueAsSet.addAll(value.getCollection());
		return valueAsSet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<CollectionFault<T>> getModelType() {
		return (Class<CollectionFault<T>>) CollectionFault.emptyCollectionFault().getClass();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<Set<T>> getPresentationType() {
		return (Class<Set<T>>) Collections.emptySet().getClass();
	}

}