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

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import io.graphenee.core.model.Fault;
import io.graphenee.core.util.KeyValueWrapper;

public class FaultToValueConverter<ID, T> implements Converter<T, Fault<ID, T>> {

	private static final long serialVersionUID = 1L;
	private String idProperty;

	public FaultToValueConverter() {
		this("oid");
	}

	public FaultToValueConverter(String idProperty) {
		this.idProperty = idProperty;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result<Fault<ID, T>> convertToModel(T value, ValueContext context) {
		if (value == null)
			return Result.ok(null);
		ID id = (ID) new KeyValueWrapper(value).valueForKeyPath(idProperty);
		Fault<ID, T> fault = Fault.fault(id, value);
		return Result.ok(fault);
	}

	@Override
	public T convertToPresentation(Fault<ID, T> value, ValueContext context) {
		if (value == null)
			return null;
		return value.getValue();
	}

}