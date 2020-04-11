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

public class InternationalMobileNumberConverter implements Converter<String, String> {

	private static final long serialVersionUID = 1L;
	private String dialCodePrefix;

	public InternationalMobileNumberConverter(String dialCodePrefix) {
		this.dialCodePrefix = dialCodePrefix;
	}

	@Override
	public Result<String> convertToModel(String value, ValueContext context) {
		if (value != null) {
			String sanitized = value.trim().replaceAll("(-|_|\\s|\\(|\\)|\\{|\\}|,|\\.)", "");
			if (sanitized.startsWith("0")) {
				sanitized = dialCodePrefix + sanitized.substring(1);
			}
			return Result.ok(sanitized);
		}
		return Result.ok(value);
	}

	@Override
	public String convertToPresentation(String value, ValueContext context) {
		if (value != null) {
			String sanitized = value.trim().replaceAll("(-|_|\\s|\\(|\\)|\\{|\\}|,|\\.)", "");
			if (sanitized.startsWith("0")) {
				sanitized = dialCodePrefix + sanitized.substring(1);
			}
			return sanitized;
		}
		return value;
	}

}
