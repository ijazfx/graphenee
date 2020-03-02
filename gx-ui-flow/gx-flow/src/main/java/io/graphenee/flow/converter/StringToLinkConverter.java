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

public class StringToLinkConverter implements Converter<String, String> {

	private static final long serialVersionUID = 1L;
	private LinkType linkType;

	public static enum LinkType {
		EMAIL,
		HTTP,
		HTTPS,
		CALL
	};

	public StringToLinkConverter() {
		this(LinkType.HTTP);
	}

	public StringToLinkConverter(LinkType linkType) {
		this.linkType = linkType;
	}

	@Override
	public Result<String> convertToModel(String value, ValueContext context) {
		return Result.ok(null);
	}

	@Override
	public String convertToPresentation(String value, ValueContext context) {
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
