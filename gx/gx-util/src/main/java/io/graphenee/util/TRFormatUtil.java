package io.graphenee.util;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A utility class for formatting dates and numbers.
 */
public class TRFormatUtil {

	private static Map<String, Format> COMPILED_FORMATTERS = new ConcurrentHashMap<>();

	/**
	 * The names of the months.
	 */
	public static final String[] MONTH_NAMES = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
	/**
	 * A short date formatter with a 6-digit month.
	 */
	public static SimpleDateFormat shortM6Formatter = new SimpleDateFormat("MM/dd/yy");
	/**
	 * A short date formatter with an 8-digit month.
	 */
	public static SimpleDateFormat shortM8Formatter = new SimpleDateFormat("MM/dd/yyyy");
	/**
	 * A short date formatter with a 6-digit day.
	 */
	public static SimpleDateFormat shortD6Formatter = new SimpleDateFormat("dd/MM/yy");
	/**
	 * A short date formatter with an 8-digit day.
	 */
	public static SimpleDateFormat shortD8Formatter = new SimpleDateFormat("dd/MM/yyyy");
	/**
	 * A date formatter.
	 */
	public static SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM d, yyyy");
	/**
	 * A day formatter.
	 */
	public static SimpleDateFormat dayFormatter = new SimpleDateFormat("EEEE");
	/**
	 * A month formatter.
	 */
	public static SimpleDateFormat monthFormatter = new SimpleDateFormat("MMMM");
	/**
	 * A date time formatter.
	 */
	public static SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("MMM d, yyyy hh:mm aaa");
	/**
	 * A time formatter.
	 */
	public static SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm aaa");
	/**
	 * A date formatter with the format yyyy-MM-dd.
	 */
	public static SimpleDateFormat yyyyMMddFormatter = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * A date time formatter with the format yyyy-MM-dd HH:mm:ss.
	 */
	public static SimpleDateFormat yyyyMMddHHmmssFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * A date time formatter with the format yyyy-MM-dd HH:mm:ss.SSS.
	 */
	public static SimpleDateFormat yyyyMMddHHmmssSSSFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	/**
	 * A date time formatter with the format MMM dd, yyyy hh:mm:ss a.
	 */
	public static SimpleDateFormat dateWithTimeFormatter = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
	/**
	 * A Zulu time formatter.
	 */
	public static SimpleDateFormat zuluTimeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	/**
	 * Gets the formatted date for a given date and pattern.
	 * @param date The date.
	 * @param pattern The pattern.
	 * @return The formatted date.
	 */
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

	/**
	 * Gets the formatted number for a given number and grouping size.
	 * @param number The number.
	 * @param groupingSize The grouping size.
	 * @return The formatted number.
	 */
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

	/**
	 * Gets the formatted number for a given number.
	 * @param number The number.
	 * @return The formatted number.
	 */
	public static String getFormattedNumber(Number number) {
		return getFormattedNumber(number, 3);
	}

	/**
	 * Gets the formatted number for a given number with a grouping size of 2.
	 * @param number The number.
	 * @return The formatted number.
	 */
	public static String getFormattedNumberGroupingSize2(Number number) {
		return getFormattedNumber(number, 2);
	}

	/**
	 * Gets the formatted number for a given number with a grouping size of 3.
	 * @param number The number.
	 * @return The formatted number.
	 */
	public static String getFormattedNumberGroupingSize3(Number number) {
		return getFormattedNumber(number, 3);
	}

}
