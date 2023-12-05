package io.graphenee.core.model.jpa.converter;

import org.json.JSONObject;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class GxStringToJsonConverter implements AttributeConverter<JSONObject, String> {

	@Override
	public String convertToDatabaseColumn(JSONObject attribute) {
		if (attribute != null) {
			return attribute.toString();
		}
		return null;
	}

	@Override
	public JSONObject convertToEntityAttribute(String dbData) {
		if (dbData != null) {
			return new JSONObject(dbData);
		}
		return new JSONObject();
	}

}
