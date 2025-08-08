package io.graphenee.util;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class HtmlSanitizer {

    public static String clean(String html) {
        if (html == null) {
            return null;
        }
        return Jsoup.clean(html, Safelist.basic());
    }

}
