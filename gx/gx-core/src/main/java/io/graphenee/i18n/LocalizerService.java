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
package io.graphenee.i18n;

import java.util.List;
import java.util.Locale;

/**
 * This interface is used to get singular and plural values of the strings in
 * specific locales locally.
 * 
 * @author ijazfx
 */

public interface LocalizerService {

	String getPluralValue(Locale locale, String key, String defaultValue, boolean forceRefresh);

	String getPluralValue(String key, String defaultValue, boolean forceRefresh);

	String getPluralValue(Locale locale, String key, boolean forceRefresh);

	String getPluralValue(String key, boolean forceRefresh);

	String getPluralValue(Locale locale, String key, String defaultValue);

	String getPluralValue(String key, String defaultValue);

	String getPluralValue(Locale locale, String key);

	String getPluralValue(String key);

	String getSingularValue(Locale locale, String key, String defaultValue, boolean forceRefresh);

	String getSingularValue(String key, String defaultValue, boolean forceRefresh);

	String getSingularValue(Locale locale, String key, boolean forceRefresh);

	String getSingularValue(String key, boolean forceRefresh);

	String getSingularValue(Locale locale, String key, String defaultValue);

	String getSingularValue(String key, String defaultValue);

	String getSingularValue(Locale locale, String key);

	String getSingularValue(String key);

	List<Locale> getAvailableLocales();

	Locale getDefaultLocale();

	void setDefaultLocale(Locale locale);

	String getLocalizedPluralValue(Locale locale, String key, String defaultValue, boolean forceRefresh);

	String getLocalizedValue(Locale locale, String key, String defaultValue, boolean forceRefresh);

	String getLocalizedValue(String key, String defaultValue, boolean forceRefresh);

	String getLocalizedValue(Locale locale, String key, boolean forceRefresh);

	String getLocalizedValue(String key, boolean forceRefresh);

	String getLocalizedValue(String key, String defaultValue);

	String getLocalizedValue(String key);

	String getLocalizedValue(Locale locale, String key, String defaultValue);

	String getLocalizedValue(Locale locale, String key);

	void invalidateTerm(String key);

}
