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

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Service;

import io.graphenee.i18n.GrapheneeI18nConfiguration;
import io.graphenee.i18n.api.LocalizerMapService;
import io.graphenee.i18n.api.LocalizerService;

@Service
@ConditionalOnClass(GrapheneeI18nConfiguration.class)
//@ConditionalOnProperty(prefix = "graphenee", name = "modules.enabled", havingValue = "true")
public class LocalizerServiceImpl implements LocalizerService {

	@Autowired
	LocalizerMapService localizerMapService;

	private Locale defaultLocale;

	public LocalizerServiceImpl() {
		defaultLocale = Locale.ENGLISH;
	}

	@Override
	public String getLocalizedValue(Locale locale, String key) {
		return localizerMapService.getLocalizedValue(locale, key, key);
	}

	@Override
	public String getLocalizedValue(Locale locale, String key, String defaultValue) {
		return localizerMapService.getLocalizedValue(locale, key, defaultValue);
	}

	@Override
	public String getLocalizedValue(String key) {
		return localizerMapService.getLocalizedValue(getDefaultLocale(), key, key);
	}

	@Override
	public String getLocalizedValue(String key, String defaultValue) {
		return localizerMapService.getLocalizedValue(getDefaultLocale(), key, defaultValue);
	}

	@Override
	public String getLocalizedValue(String key, boolean forceRefresh) {
		return localizerMapService.getLocalizedValue(getDefaultLocale(), key, key, forceRefresh);
	}

	@Override
	public String getLocalizedValue(Locale locale, String key, boolean forceRefresh) {
		return localizerMapService.getLocalizedValue(locale, key, key, forceRefresh);
	}

	@Override
	public String getLocalizedValue(String key, String defaultValue, boolean forceRefresh) {
		return localizerMapService.getLocalizedValue(getDefaultLocale(), key, defaultValue, forceRefresh);
	}

	@Override
	public String getLocalizedValue(Locale locale, String key, String defaultValue, boolean forceRefresh) {
		return localizerMapService.getLocalizedValue(locale, key, defaultValue, forceRefresh);
	}

	@Override
	public String getLocalizedPluralValue(Locale locale, String key, String defaultValue, boolean forceRefresh) {
		return localizerMapService.getLocalizedPluralValue(locale, key, defaultValue, forceRefresh);
	}

	@Override
	public void setDefaultLocale(Locale locale) {
		defaultLocale = Locale.ENGLISH;

	}

	@Override
	public Locale getDefaultLocale() {
		return defaultLocale;
	}

	@Override
	public List<Locale> getAvailableLocales() {
		return localizerMapService.getAvailableLocales();
	}

	@Override
	public String getSingularValue(String key) {
		return getLocalizedValue(getDefaultLocale(), key, key, false);
	}

	@Override
	public String getSingularValue(Locale locale, String key) {
		return getLocalizedValue(locale, key, key, false);
	}

	@Override
	public String getSingularValue(String key, String defaultValue) {
		return getLocalizedValue(getDefaultLocale(), key, defaultValue, false);
	}

	@Override
	public String getSingularValue(Locale locale, String key, String defaultValue) {
		return getLocalizedValue(locale, key, defaultValue, false);
	}

	@Override
	public String getSingularValue(String key, boolean forceRefresh) {
		return getLocalizedValue(getDefaultLocale(), key, key, forceRefresh);
	}

	@Override
	public String getSingularValue(Locale locale, String key, boolean forceRefresh) {
		return getLocalizedValue(locale, key, key, forceRefresh);
	}

	@Override
	public String getSingularValue(String key, String defaultValue, boolean forceRefresh) {
		return getLocalizedValue(getDefaultLocale(), key, defaultValue, forceRefresh);
	}

	@Override
	public String getSingularValue(Locale locale, String key, String defaultValue, boolean forceRefresh) {
		return getLocalizedValue(locale, key, defaultValue, forceRefresh);
	}

	@Override
	public String getPluralValue(String key) {
		return getLocalizedPluralValue(getDefaultLocale(), key, key, false);
	}

	@Override
	public String getPluralValue(Locale locale, String key) {
		return getLocalizedPluralValue(locale, key, key, false);
	}

	@Override
	public String getPluralValue(String key, String defaultValue) {
		return getLocalizedPluralValue(getDefaultLocale(), key, defaultValue, false);
	}

	@Override
	public String getPluralValue(Locale locale, String key, String defaultValue) {
		return getLocalizedPluralValue(locale, key, defaultValue, false);
	}

	@Override
	public String getPluralValue(String key, boolean forceRefresh) {
		return getLocalizedPluralValue(getDefaultLocale(), key, key, forceRefresh);
	}

	@Override
	public String getPluralValue(Locale locale, String key, boolean forceRefresh) {
		return getLocalizedPluralValue(locale, key, key, forceRefresh);
	}

	@Override
	public String getPluralValue(String key, String defaultValue, boolean forceRefresh) {
		return getLocalizedPluralValue(getDefaultLocale(), key, defaultValue, forceRefresh);
	}

	@Override
	public String getPluralValue(Locale locale, String key, String defaultValue, boolean forceRefresh) {
		return getLocalizedPluralValue(locale, key, defaultValue, forceRefresh);
	}

	@Override
	public void invalidateTerm(String key) {
		localizerMapService.invalidateTerm(key);
	}

}
