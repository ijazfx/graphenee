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

public class InternationalMobileNumberConverter implements Converter<String, String> {

	private String dialCodePrefix;

	public InternationalMobileNumberConverter(String dialCodePrefix) {
		this.dialCodePrefix = dialCodePrefix;
	}

	@Override
	public String convertToModel(String value, Class<? extends String> targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value != null) {
			String sanitized = value.trim().replaceAll("(-|_|\\s|\\(|\\)|\\{|\\}|,|\\.)", "");
			if (sanitized.startsWith("0")) {
				sanitized = dialCodePrefix + sanitized.substring(1);
			}
			return sanitized;
		}
		return value;
	}

	@Override
	public String convertToPresentation(String value, Class<? extends String> targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value != null) {
			String sanitized = value.trim().replaceAll("(-|_|\\s|\\(|\\)|\\{|\\}|,|\\.)", "");
			if (sanitized.startsWith("0")) {
				sanitized = dialCodePrefix + sanitized.substring(1);
			}
			return sanitized;
		}
		return value;
	}

	@Override
	public Class<String> getModelType() {
		return String.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}

}
