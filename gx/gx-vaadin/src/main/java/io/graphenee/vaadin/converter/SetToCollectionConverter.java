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
