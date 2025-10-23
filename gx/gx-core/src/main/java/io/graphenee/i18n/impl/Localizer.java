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
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import io.graphenee.core.GxDataService;
import io.graphenee.core.model.entity.GxTermTranslation;

final class Localizer {

	private static final Logger L = LoggerFactory.getLogger(Localizer.class);

	private static final int DEFAULT_SINGULAR_MAP_MAX_SIZE = 2000;
	private static final int DEFAULT_PLURAL_MAP_MAX_SIZE = 1000;

	GxDataService dataService;

	private Cache<String, String> singularTerms;

	private Cache<String, String> termPlurals;

	private Locale locale;

	public Localizer() {
	}

	public Localizer(Locale locale, GxDataService dataService) {
		this.dataService = dataService;
		setLocale(locale);
		singularTerms = CacheBuilder.newBuilder().maximumSize(DEFAULT_SINGULAR_MAP_MAX_SIZE)
				.expireAfterWrite(15, TimeUnit.MINUTES).build();
		termPlurals = CacheBuilder.newBuilder().maximumSize(DEFAULT_PLURAL_MAP_MAX_SIZE)
				.expireAfterWrite(15, TimeUnit.MINUTES).build();

		List<GxTermTranslation> translations = dataService.findTermTranslationByLocale(locale);
		if (translations.size() > 0) {
			translations.parallelStream().forEach(t -> {
				if (t.getTermSingular() != null) {
					singularTerms.put(t.getTerm().getTermKey(), t.getTermSingular());
				}
				if (t.getTermPlural() != null) {
					termPlurals.put(t.getTerm().getTermKey(), t.getTermPlural());
				}
			});
		}
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public Cache<String, String> getSingularTerms() {
		return singularTerms;
	}

	public void setSingularTerms(Cache<String, String> terms) {
		this.singularTerms = terms;
	}

	public Cache<String, String> getPluralTerms() {
		return termPlurals;
	}

	public void setTermPlural(Cache<String, String> termPlurals) {
		this.termPlurals = termPlurals;
	}

	public String getLanguage() {
		return getLocale().getLanguage();
	}

	public String localizedValueForKey(String termKey) {
		return localizedValueForKey(termKey, false);
	}

	public String localizedValueForKey(String termKey, boolean forceRefresh) {
		if (termKey == null || termKey.length() == 0)
			return termKey;
		if (forceRefresh) {
			getSingularTerms().invalidate(termKey);
		}
		try {
			String value = getSingularTerms().get(termKey, new Callable<String>() {

				@Override
				public String call() throws Exception {

					GxTermTranslation translation = dataService.findEffectiveTermTranslationByTermKeyAndLocale(termKey,
							locale);
					if (translation != null && translation.getTerm().getTermKey().equals(termKey)
							&& !Strings.isNullOrEmpty(translation.getTermSingular())) {
						return translation.getTermSingular();
					}

					return termKey;
				}
			});
			return value;
		} catch (Exception e) {
			L.warn(e.getMessage());
			return termKey;
		}
	}

	public String localizedPluralForKey(String termKey, boolean forceRefresh) {
		if (termKey == null || termKey.length() == 0)
			return termKey;
		if (forceRefresh) {
			getPluralTerms().invalidate(termKey);
		}
		try {
			String value = getPluralTerms().get(termKey, new Callable<String>() {

				@Override
				public String call() throws Exception {

					GxTermTranslation translation = dataService.findEffectiveTermTranslationByTermKeyAndLocale(termKey,
							locale);
					if (translation != null && translation.getTerm().getTermKey().equals(termKey)
							&& !Strings.isNullOrEmpty(translation.getTermPlural())) {
						return translation.getTermPlural();
					}

					return termKey;
				}
			});
			return value;
		} catch (Exception e) {
			L.warn(e.getMessage());
			return termKey;
		}
	}

	public String localizedValueForKeyWithDefault(String termKey, String defaultValue) {
		return localizedValueForKeyWithDefault(termKey, defaultValue, false);
	}

	public String localizedValueForKeyWithDefault(String termKey, String defaultValue, boolean forceRefresh) {
		String value = localizedValueForKey(termKey, forceRefresh);
		if ((value == null || value.equals(termKey)) && defaultValue != null) {
			value = defaultValue;
		}
		return value;
	}

	public String localizedPluralForKeyWithDefault(String termKey, String defaultValue, boolean forceRefresh) {
		String value = localizedPluralForKey(termKey, forceRefresh);
		if (value == null || value.equals(termKey)) {
			value = defaultValue;
		}
		return value;
	}

	public void setLocalizedValueForKey(String termKey, String value) {
		getSingularTerms().put(termKey, value);
	}

}