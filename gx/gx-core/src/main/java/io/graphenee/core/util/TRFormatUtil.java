package io.graphenee.core.util;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TRFormatUtil {

	private static Map<String, Format> COMPILED_FORMATTERS = new ConcurrentHashMap<>();

	public static final String[] MONTH_NAMES = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
	public static SimpleDateFormat shortM6Formatter = new SimpleDateFormat("MM/dd/yy");
	public static SimpleDateFormat shortM8Formatter = new SimpleDateFormat("MM/dd/yyyy");
	public static SimpleDateFormat shortD6Formatter = new SimpleDateFormat("dd/MM/yy");
	public static SimpleDateFormat shortD8Formatter = new SimpleDateFormat("dd/MM/yyyy");
	public static SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM d, yyyy");
	public static SimpleDateFormat dayFormatter = new SimpleDateFormat("EEEE");
	public static SimpleDateFormat monthFormatter = new SimpleDateFormat("MMMM");
	public static SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("MMM d, yyyy hh:mm aaa");
	public static SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm aaa");
	public static SimpleDateFormat yyyyMMddFormatter = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat yyyyMMddHHmmssFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat yyyyMMddHHmmssSSSFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	public static SimpleDateFormat dateWithTimeFormatter = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
	public static SimpleDateFormat zuluTimeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	public static String getFormattedDate(Date date, String pattern) {
		if (date == null) {
			return null;
		}
		Format formatter = COMPILED_FORMATTERS.get(pattern);
		if (formatter == null) {
			synchronized (TRCalendarUtil.class) {
				if (formatter == null) {
					formatter = new SimpleDateFormat(pattern);
					COMPILED_FORMATTERS.put(pattern, formatter);
				}
			}
		}
		return formatter.format(date);
	}

	public static String getFormattedNumber(Number number, int groupingSize) {
		if (number == null) {
			return null;
		}
		String key = "#.##-" + groupingSize;
		Format formatter = COMPILED_FORMATTERS.get(key);
		if (formatter == null) {
			synchronized (TRCalendarUtil.class) {
				if (formatter == null) {
					DecimalFormat df = new DecimalFormat("#.##");
					df.setGroupingUsed(true);
					df.setGroupingSize(groupingSize);
					formatter = df;
					COMPILED_FORMATTERS.put(key, formatter);
				}
			}
		}
		return formatter.format(number);
	}

	public static String getFormattedNumber(Number number) {
		return getFormattedNumber(number, 3);
	}

	public static String getFormattedNumberGroupingSize2(Number number) {
		return getFormattedNumber(number, 2);
	}

	public static String getFormattedNumberGroupingSize3(Number number) {
		return getFormattedNumber(number, 3);
	}

}
