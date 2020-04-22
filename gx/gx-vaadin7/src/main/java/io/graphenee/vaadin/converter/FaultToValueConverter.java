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

import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

import io.graphenee.core.model.Fault;
import io.graphenee.core.util.KeyValueWrapper;

public class FaultToValueConverter<ID, T> implements Converter<T, Fault<ID, T>> {

	private static final long serialVersionUID = 1L;
	private String idProperty;
	private Class<T> typeOfT;

	public FaultToValueConverter(Class<T> typeOfT) {
		this(typeOfT, "oid");
	}

	public FaultToValueConverter(Class<T> typeOfT, String idProperty) {
		this.typeOfT = typeOfT;
		this.idProperty = idProperty;
	}

	@Override
	public Fault<ID, T> convertToModel(T value, Class<? extends Fault<ID, T>> targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value == null)
			return null;
		ID id = (ID) new KeyValueWrapper(value).valueForKeyPath(idProperty);
		Fault<ID, T> fault = Fault.fault(id, value);
		return fault;
	}

	@Override
	public T convertToPresentation(Fault<ID, T> value, Class<? extends T> targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value == null)
			return null;
		return value.getValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<Fault<ID, T>> getModelType() {
		return (Class<Fault<ID, T>>) Fault.nullFault().getClass();
	}

	@Override
	public Class<T> getPresentationType() {
		return typeOfT;
	}

}