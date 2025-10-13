package io.graphenee.vaadin.flow.utils;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.internal.LocaleUtil;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;

public class GxTranslationUtils {

    /**
     * Get the translation for the component locale.
     * <p>
     * The method never returns a null. If there is no {@link I18NProvider}
     * available or no translation for the {@code key} it returns an exception
     * string e.g. '!{key}!'.
     *
     * @see #getLocale()
     *
     * @param key
     *               translation key
     * @param params
     *               parameters used in translation string
     * @return translation for key if found (implementation should not return
     *         null)
     */
    public static String getTranslation(Object key, Object... params) {
        final Optional<I18NProvider> i18NProvider = LocaleUtil.getI18NProvider();

        VaadinSession session = VaadinSession.getCurrent();
        final Locale locale;
        if (session != null) {
            locale = session.getLocale();
        } else {
            locale = VaadinService.getCurrentRequest().getLocale();
        }

        return i18NProvider
                .map(i18n -> i18n.getTranslation(key, locale, params))
                .orElseGet(() -> "!{" + key + "}!");
    }

    /**
     * Get the translation for key with given locale.
     * <p>
     * The method never returns a null. If there is no {@link I18NProvider}
     * available or no translation for the {@code key} it returns an exception
     * string e.g. '!{key}!'.
     *
     * @param locale
     *               locale to use
     * @param key
     *               translation key
     * @param params
     *               parameters used in translation string
     * @return translation for key if found
     */
    public String getTranslation(Locale locale, String key, Object... params) {
        return LocaleUtil.getI18NProvider()
                .map(i18n -> i18n.getTranslation(key, locale, params))
                .orElseGet(() -> "!{" + key + "}!");
    }

    public static List<Locale> getProvidedLocales() {
        return LocaleUtil.getI18NProvider()
                .map(i18n -> i18n.getProvidedLocales()).orElse(Collections.emptyList());
    }

}
