package io.graphenee.vaadin.converter;

import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

public class DoubleToPercentConverter implements Converter<String, Double> {

	private static final long serialVersionUID = 1L;

	@Override
	public Double convertToModel(String value, Class<? extends Double> targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
		return null;
	}

	@Override
	public String convertToPresentation(Double value, Class<? extends String> targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
		return String.format("%,.2f%%", value == null ? 0 : value);
	}

	@Override
	public Class<Double> getModelType() {
		return Double.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}

}
