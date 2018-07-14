package io.graphenee.core.gson;

import java.sql.Timestamp;
import java.text.ParseException;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import io.graphenee.core.util.TRCalenderUtil;

public class JsonDeserializers {

	public static final JsonDeserializer<Timestamp> TIMESTAMP_DESERIALIZER = new TimestampDeserializer();

	public static class TimestampDeserializer implements JsonDeserializer<Timestamp> {

		@Override
		public Timestamp deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			try {
				return new Timestamp(json.getAsLong());
			} catch (Exception ex) {
				String value = json.getAsString();
				if (value != null) {
					value = value.trim();
					try {
						return new Timestamp(TRCalenderUtil.yyyyMMddHHmmssSSSFormatter.parse(value).getTime());
					} catch (ParseException e1) {
						try {
							return new Timestamp(TRCalenderUtil.yyyyMMddHHmmssFormatter.parse(value).getTime());
						} catch (ParseException e2) {
							try {
								return new Timestamp(TRCalenderUtil.yyyyMMddFormatter.parse(value).getTime());
							} catch (ParseException e3) {

							}
						}
					}
				}
				return null;
			}
		}
	}

}
