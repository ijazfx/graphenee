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

import java.sql.Timestamp;
import java.util.Date;
import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

public class DateToTimestampConverter implements Converter<Date, Timestamp> {

	@Override
	public Timestamp convertToModel(Date value, Class<? extends Timestamp> targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value == null) {
			return null;
		}
		return new Timestamp(value.getTime());
	}

	@Override
	public Date convertToPresentation(Timestamp value, Class<? extends Date> targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value == null) {
			return null;
		}
		return new Date(value.getTime());
	}

	@Override
	public Class<Timestamp> getModelType() {
		return Timestamp.class;
	}

	@Override
	public Class<Date> getPresentationType() {
		return Date.class;
	}

}
