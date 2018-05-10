package com.graphenee.vaadin.converter;

import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

public class StringToLinkConverter implements Converter<String, String> {

	private LinkType linkType;

	public static enum LinkType {
		EMAIL, HTTP, HTTPS, CALL
	};

	public StringToLinkConverter() {
		this(LinkType.HTTP);
	}

	public StringToLinkConverter(LinkType linkType) {
		this.linkType = linkType;

	}

	@Override
	public Class<String> getModelType() {
		return String.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}

	@Override
	public String convertToModel(String value, Class<? extends String> targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
		return null;
	}

	@Override
	public String convertToPresentation(String value, Class<? extends String> targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value == null)
			return null;
		if (linkType == LinkType.EMAIL) {
			return "<a href=mailto:" + value + ">" + value + "</a>";
		}
		if (linkType == LinkType.HTTPS) {
			return "<a href=https://" + value + ">" + value + "</a>";
		}
		if (linkType == LinkType.CALL) {
			return "<a href=tel://" + value + ">" + value + "</a>";
		}
		return "<a href=http://" + value + ">" + value + "</a>";
	}

}
