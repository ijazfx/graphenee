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
package io.graphenee.core.util;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanMap;

/**
 * This class is used to compose Email using predefined template. It provides
 * method to fill values in the template.
 *
 * @author aizazanwar
 */

public class TemplateUtil {

	public static final Pattern KEY_PATTERN = Pattern.compile("(?:\\#\\{)(\\w+)(?:\\})");

	/**
	 * The method parses template text and replaces all matching keys with
	 * values found in data map.
	 *
	 * @param template - A template string such as "Hello #{last_name}"
	 * @param data - key/value pairs to replace keys with values in template
	 * string
	 * @return - Parsed string where keys are replaced with values
	 */
	public static String parseTemplateWithMap(String template, Map<String, Object> data) {
		StringBuffer sb = new StringBuffer();
		Matcher m = KEY_PATTERN.matcher(template);

		while (m.find()) {
			Object value = data.get(m.group(1));
			if (value == null) {
				m.appendReplacement(sb, "\\#\\{" + m.group(1) + "\\}");
			} else {
				m.appendReplacement(sb, (String) value);
			}
		}

		m.appendTail(sb);

		return sb.toString();

	}

	/**
	 * The method parses template text and replaces all matching keys with
	 * matching bean property values.
	 *
	 * @param template - A template string such as "Hello #{last_name}"
	 * @param bean - A bean which is converted to key/value pair and then used
	 * for template parsing
	 * @return - Parsed string where keys are replaced with values
	 */
	public static String parseTemplateWithBean(String template, Object bean) {
		BeanMap bm = new BeanMap(bean);
		StringBuffer sb = new StringBuffer();
		Matcher m = KEY_PATTERN.matcher(template);

		while (m.find()) {
			Object value = bm.get(m.group(1));
			if (value == null) {
				m.appendReplacement(sb, "\\#\\{" + m.group(1) + "\\}");
			} else {
				m.appendReplacement(sb, (String) value);
			}
		}

		m.appendTail(sb);

		return sb.toString();
	}

	public static void main(String[] args) {
		HashMap<String, String> data = new HashMap<>();
		data.put("name", "Farrukh");
		data.put("place", "Lahore");
		data.put("date", LocalDate.now().toString());
	}

}
