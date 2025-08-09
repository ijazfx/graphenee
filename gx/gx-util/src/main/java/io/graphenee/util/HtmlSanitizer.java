package io.graphenee.util;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

/**
 * A utility class for sanitizing HTML.
 */
public class HtmlSanitizer {

	/**
	 * Creates a new instance of this sanitizer.
	 */
	public HtmlSanitizer() {
		// a default constructor
	}

	/**
	 * Cleans the given HTML.
	 * @param html The HTML to clean.
	 * @return The cleaned HTML.
	 */
    public static String clean(String html) {
        if (html == null) {
            return null;
        }
        return Jsoup.clean(html, Safelist.basic());
    }

}
