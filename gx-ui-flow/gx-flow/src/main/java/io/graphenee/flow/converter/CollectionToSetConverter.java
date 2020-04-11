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
package io.graphenee.flow.converter;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class CollectionToSetConverter<T> implements Converter<Set<T>, Collection<T>> {

	private static final long serialVersionUID = 1L;

	@Override
	public Result<Collection<T>> convertToModel(Set<T> value, ValueContext context) {
		return Result.ok(value);
	}

	@Override
	public Set<T> convertToPresentation(Collection<T> value, ValueContext context) {
		if (value == null) {
			return Collections.emptySet();
		}
		Set<T> valueAsSet = new HashSet<>();
		valueAsSet.addAll(value);
		return valueAsSet;
	}

}