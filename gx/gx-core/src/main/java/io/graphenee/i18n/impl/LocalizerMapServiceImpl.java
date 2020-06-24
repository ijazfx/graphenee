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
package io.graphenee.i18n.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Service;

import io.graphenee.core.model.api.GxDataService;
import io.graphenee.i18n.GrapheneeI18nConfiguration;
import io.graphenee.i18n.api.LocalizerMapService;

@Service
@ConditionalOnClass(GrapheneeI18nConfiguration.class)
//@ConditionalOnProperty(prefix = "graphenee", name = "modules.enabled", havingValue = "true")
public class LocalizerMapServiceImpl implements LocalizerMapService {

	@Autowired
	GxDataService dataService;

	private Map<Locale, Localizer> localizerMap;

	public LocalizerMapServiceImpl() {
		localizerMap = new HashMap<>();
	}

	private Localizer getLocalizer(Locale locale) {
		if (!localizerMap.containsKey(locale)) {
			Locale requestLocale = locale;
			if (locale.getLanguage().startsWith(Locale.ENGLISH.getLanguage())) {
				locale = Locale.CANADA;
			}
			if (locale.getLanguage().startsWith(Locale.FRANCE.getLanguage())) {
				locale = Locale.CANADA_FRENCH;
			}
			Localizer localizer = localizerMap.get(locale);
			if (localizer == null) {
				localizer = new Localizer(locale, dataService);
				localizerMap.put(locale, localizer);
			}
			localizerMap.put(requestLocale, localizer);
		}
		return localizerMap.get(locale);
	}

	@Override
	public String getLocalizedValue(Locale locale, String key, String defaultValue) {
		return getLocalizer(locale).localizedValueForKeyWithDefault(key, defaultValue);
	}

	@Override
	public String getLocalizedValue(Locale locale, String key, String defaultValue, boolean forceRefresh) {
		return getLocalizer(locale).localizedValueForKeyWithDefault(key, defaultValue, forceRefresh);
	}

	@Override
	public String getLocalizedPluralValue(Locale locale, String key, String defaultValue, boolean forceRefresh) {
		return getLocalizer(locale).localizedPluralForKeyWithDefault(key, defaultValue, forceRefresh);
	}

	@Override
	public List<Locale> getAvailableLocales() {
		List<Locale> locales = new ArrayList<>();
		dataService.findSupportedLocale().forEach(bean -> {
			locales.add(new Locale(bean.getLocaleCode()));
		});
		return locales;
	}

	@Override
	public void invalidateTerm(String key) {
		localizerMap.keySet().forEach(locale -> {
			Localizer localizerForLocale = localizerMap.get(locale);
			localizerForLocale.getPluralTerms().invalidate(key);
			localizerForLocale.getSingularTerms().invalidate(key);
		});
	}

}
