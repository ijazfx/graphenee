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
package io.graphenee.i18n.api;

import java.util.List;
import java.util.Locale;

public interface LocalizerMapService {

	String getLocalizedValue(Locale locale, String key, String defaultValue);

	String getLocalizedValue(Locale locale, String key, String defaultValue, boolean forceRefresh);

	String getLocalizedPluralValue(Locale locale, String key, String defaultValue, boolean forceRefresh);

	List<Locale> getAvailableLocales();

	void invalidateTerm(String key);

}