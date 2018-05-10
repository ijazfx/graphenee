package com.graphenee.vaadin.converter;

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
