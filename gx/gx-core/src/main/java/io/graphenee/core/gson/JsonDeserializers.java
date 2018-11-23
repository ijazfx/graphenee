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
package io.graphenee.core.gson;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Base64;

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

	public static class ByteArrayDeserializer implements JsonDeserializer<byte[]> {

		@Override
		public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			if (json == null)
				return new byte[] {};
			return Base64.getDecoder().decode(json.getAsString());
		}

	}

}
