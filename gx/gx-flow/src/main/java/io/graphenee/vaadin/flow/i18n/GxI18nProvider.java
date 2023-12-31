package io.graphenee.vaadin.flow.i18n;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.graphenee.i18n.LocalizerService;

@SuppressWarnings("serial")
@Service
public class GxI18nProvider implements com.vaadin.flow.i18n.I18NProvider {

	@Autowired
	LocalizerService service;

	@Override
	public List<Locale> getProvidedLocales() {
		return service.getAvailableLocales();
	}

	@Override
	public String getTranslation(String key, Locale locale, Object... params) {
		String value = service.getLocalizedValue(locale, key);
		if (params != null && params.length > 0) {
			return String.format(locale, value, params);
		}
		return value;
	}

}
