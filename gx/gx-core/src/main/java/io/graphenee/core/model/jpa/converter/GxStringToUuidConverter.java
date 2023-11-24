package io.graphenee.core.model.jpa.converter;

import java.util.UUID;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class GxStringToUuidConverter implements AttributeConverter<UUID, String> {

	@Override
	public String convertToDatabaseColumn(UUID attribute) {
		if (attribute != null) {
			return attribute.toString();
		}
		return null;
	}

	@Override
	public UUID convertToEntityAttribute(String dbData) {
		if (dbData != null) {
			return UUID.fromString(dbData);
		}
		return null;
	}

}
