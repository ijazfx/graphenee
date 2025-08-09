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
package io.graphenee.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is used to perform operations related to calendar, e.g., getting
 * current date, getting number of days between two dates.
 *
 * @author fijaz
 */
public class TRCalendarUtil {

	/**
	 * Creates a new instance of this utility class.
	 */
	public TRCalendarUtil() {
		// a default constructor
	}

	private static Map<String, SimpleDateFormat> COMPILED_FORMATTERS = new ConcurrentHashMap<>();

	/**
	 * The names of the months.
	 */
	public static final String[] MONTH_NAMES = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };

	static SimpleDateFormat customDateFormatter;
	static SimpleDateFormat customTimeFormatter;
	static SimpleDateFormat customDateTimeFormatter;

	/**
	 * Sets the custom date formatter.
	 * @param sdf The custom date formatter.
	 */
	synchronized public static void setCustomDateFormatter(SimpleDateFormat sdf) {
		customDateFormatter = sdf;
	}

	/**
	 * Sets the custom time formatter.
	 * @param sdf The custom time formatter.
	 */
	synchronized public static void setCustomTimeFormatter(SimpleDateFormat sdf) {
		customTimeFormatter = sdf;
	}

	/**
	 * Sets the custom date time formatter.
	 * @param sdf The custom date time formatter.
	 */
	synchronized public static void setCustomDateTimeFormatter(SimpleDateFormat sdf) {
		customDateTimeFormatter = sdf;
	}

	/**
	 * Gets the custom date formatter.
	 * @return The custom date formatter.
	 */
	public static SimpleDateFormat getCustomDateFormatter() {
		return customDateFormatter != null ? customDateFormatter : dateFormatter;
	}

	/**
	 * Gets the custom time formatter.
	 * @return The custom time formatter.
	 */
	public static SimpleDateFormat getCustomTimeFormatter() {
		return customTimeFormatter != null ? customTimeFormatter : timeFormatter;
	}

	/**
	 * Gets the custom date time formatter.
	 * @return The custom date time formatter.
	 */
	public static SimpleDateFormat getCustomDateTimeFormatter() {
		return customDateTimeFormatter != null ? customDateTimeFormatter : dateTimeFormatter;
	}

	/**
	 * A short date formatter with a 6-digit month.
	 */
	public static final SimpleDateFormat shortM6Formatter = new SimpleDateFormat("MM/dd/yy");
	/**
	 * A short date formatter with an 8-digit month.
	 */
	public static final SimpleDateFormat shortM8Formatter = new SimpleDateFormat("MM/dd/yyyy");
	/**
	 * A short date formatter with a 6-digit day.
	 */
	public static final SimpleDateFormat shortD6Formatter = new SimpleDateFormat("dd/MM/yy");
	/**
	 * A short date formatter with an 8-digit day.
	 */
	public static final SimpleDateFormat shortD8Formatter = new SimpleDateFormat("dd/MM/yyyy");
	/**
	 * A date formatter.
	 */
	public static final SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM d, yyyy");
	/**
	 * A day formatter.
	 */
	public static final SimpleDateFormat dayFormatter = new SimpleDateFormat("EEEE");
	/**
	 * A month formatter.
	 */
	public static final SimpleDateFormat monthFormatter = new SimpleDateFormat("MMMM");
	/**
	 * A date time formatter.
	 */
	public static final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("MMM d, yyyy hh:mm aaa");
	/**
	 * A time formatter.
	 */
	public static final SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm aaa");
	/**
	 * A date formatter with the format yyyy-MM-dd.
	 */
	public static final SimpleDateFormat yyyyMMddFormatter = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * A date time formatter with the format yyyy-MM-dd HH:mm:ss.
	 */
	public static final SimpleDateFormat yyyyMMddHHmmssFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * A date time formatter with the format yyyy-MM-dd HH:mm:ss.SSS.
	 */
	public static final SimpleDateFormat yyyyMMddHHmmssSSSFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	/**
	 * A Zulu time formatter.
	 */
	public static final SimpleDateFormat zuluTimeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	/**
	 * Adds days to a date.
	 * @param currentDate The date to add days to.
	 * @param days The number of days to add.
	 * @return The new date.
	 */
	public static Date addDaysToDate(Date currentDate, int days) {
		LocalDate now = toLocalDateFromDate(currentDate);
		now = now.plusDays(days);
		return toDateFromLocalDate(now);
	}

	/**
	 * Subtracts days from a date.
	 * @param currentDate The date to subtract days from.
	 * @param days The number of days to subtract.
	 * @return The new date.
	 */
	public static Date minusDaysToDate(Date currentDate, int days) {
		LocalDate now = toLocalDateFromDate(currentDate);
		now = now.minusDays(days);
		return toDateFromLocalDate(now);
	}

	/**
	 * Adds minutes to a date.
	 * @param currentDate The date to add minutes to.
	 * @param minutes The number of minutes to add.
	 * @return The new date.
	 */
	public static Date addMinutesToDate(Date currentDate, int minutes) {
		LocalDateTime now = toLocalDateTimeFromDate(currentDate);
		now = now.plusMinutes(minutes);
		return toDateFromLocalDateTime(now);
	}

	/**
	 * Adds months to a date.
	 * @param currentDate The date to add months to.
	 * @param months The number of months to add.
	 * @return The new date.
	 */
	public static Date addMonthsToDate(Date currentDate, int months) {
		LocalDateTime now = toLocalDateTimeFromDate(currentDate);
		now = now.plusMonths(months);
		return toDateFromLocalDateTime(now);
	}

	/**
	 * Gets a calendar field from a date.
	 * @param calendarField The calendar field to get.
	 * @param date The date to get the field from.
	 * @return The value of the field.
	 */
	public static int calendarFieldFromDate(int calendarField, Date date) {
		GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
		cal.setTime(date);
		return cal.get(calendarField);
	}

	/**
	 * Gets the number of months between two dates.
	 * @param startDate The start date.
	 * @param endDate The end date.
	 * @return The number of months between the two dates.
	 */
	public static long monthsBetween(Date startDate, Date endDate) {
		return monthsBetweenUsingChronoUnit(startDate, endDate);
	}

	/**
	 * Gets the number of months between two dates using ChronoUnit.
	 * @param startDate The start date.
	 * @param endDate The end date.
	 * @return The number of months between the two dates.
	 */
	public static long monthsBetweenUsingChronoUnit(Date startDate, Date endDate) {
		return Math.abs(ChronoUnit.MONTHS.between(toLocalDateFromDate(startDate), toLocalDateFromDate(endDate)));
	}

	/**
	 * Gets the number of days between two dates.
	 * @param startDate The start date.
	 * @param endDate The end date.
	 * @return The number of days between the two dates.
	 */
	public static long daysBetween(Date startDate, Date endDate) {
		return daysBetweenUsingChronoUnit(startDate, endDate);
	}

	/**
	 * Gets the number of days between two dates using ChronoUnit.
	 * @param startDate The start date.
	 * @param endDate The end date.
	 * @return The number of days between the two dates.
	 */
	public static long daysBetweenUsingChronoUnit(Date startDate, Date endDate) {
		return Math.abs(ChronoUnit.DAYS.between(toLocalDateFromDate(startDate), toLocalDateFromDate(endDate)));
	}

	/**
	 * Gets the elapsed time since a timestamp.
	 * @param timestamp The timestamp.
	 * @return The elapsed time.
	 */
	public static String elapsedTime(Timestamp timestamp) {

		String value = null;
		if (timestamp == null) {
			return value;
		}
		LocalDateTime localDateTime = timestamp.toLocalDateTime();
		LocalDateTime currentLocalDateTime = LocalDateTime.now();

		long between = ChronoUnit.SECONDS.between(localDateTime, currentLocalDateTime);

		if (between >= 60l) {
			between = ChronoUnit.MINUTES.between(localDateTime, currentLocalDateTime);
			value = between == 1 ? between + " minute" : between + " minutes";
			if (between >= 60l) {
				between = ChronoUnit.HOURS.between(localDateTime, currentLocalDateTime);
				value = between == 1 ? between + " hour" : between + " hours";

				if (between >= 24l) {
					between = ChronoUnit.DAYS.between(localDateTime, currentLocalDateTime);
					value = between == 1 ? between + " day" : between + " days";

					if (between >= 7l) {
						between = ChronoUnit.WEEKS.between(localDateTime, currentLocalDateTime);
						value = between == 1 ? between + " week" : between + " weeks";

						if (between >= 4l) {
							between = ChronoUnit.MONTHS.between(localDateTime, currentLocalDateTime);
							value = between == 1 ? between + " month" : between + " months";

							if (between >= 12l) {
								between = ChronoUnit.YEARS.between(localDateTime, currentLocalDateTime);
								value = between == 1 ? between + " year" : between + " years";

							}
						}
					}
				}
			}

		} else {
			value = between == 1 ? between + " second" : between + " seconds";
		}

		value += " ago";

		return value;
	}

	/**
	 * Gets the age of a person.
	 * @param timestamp The timestamp of the person's birth.
	 * @return The age of the person.
	 */
	public static String age(Timestamp timestamp) {
		long diff = monthsBetween(timestamp, getCurrentTimeStamp());
		int years = (int) diff / 12;
		int remainingMonths = (int) diff % 12;
		return years + "y " + remainingMonths + "m";
	}

	/**
	 * Gets the end of the day.
	 * @return The end of the day.
	 */
	public static Date endOfDay() {
		GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}

	/**
	 * Gets the end of the day as a timestamp.
	 * @return The end of the day as a timestamp.
	 */
	public static Timestamp endOfDayAsTimestamp() {
		return new Timestamp(endOfDay().getTime());
	}

	/**
	 * Gets the end of the day for a given date.
	 * @param currentDate The date.
	 * @return The end of the day.
	 */
	public static Date endOfDay(Date currentDate) {
		GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
		cal.setTime(currentDate);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}

	/**
	 * Gets the end of the day for a given date as a timestamp.
	 * @param currentDate The date.
	 * @return The end of the day as a timestamp.
	 */
	public static Timestamp endOfDayAsTimestamp(Date currentDate) {
		return new Timestamp(endOfDay(currentDate).getTime());
	}

	/**
	 * Gets the end of the last month.
	 * @return The end of the last month.
	 */
	public static Date endOfLastMonth() {
		Calendar cal = Calendar.getInstance();
		cal.setLenient(true);
		cal.setTime(new Date());
		cal.add(Calendar.MONTH, -1);
		Timestamp ts = new Timestamp(cal.getTime().getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.lastDayOfMonth());
		return endOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

	/**
	 * Gets the end of the last month for a given date.
	 * @param currentDate The date.
	 * @return The end of the last month.
	 */
	public static Date endOfLastMonth(Date currentDate) {
		Calendar cal = Calendar.getInstance();
		cal.setLenient(true);
		cal.setTime(currentDate);
		cal.add(Calendar.MONTH, -1);
		Timestamp ts = new Timestamp(cal.getTime().getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.lastDayOfMonth());
		return endOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

	/**
	 * Gets the end of the last week.
	 * @return The end of the last week.
	 */
	public static Date endOfLastWeek() {
		GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.setTime(addDaysToDate(new Date(), -7));
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}

	/**
	 * Gets the end of the last year.
	 * @return The end of the last year.
	 */
	public static Date endOfLastYear() {
		Calendar cal = Calendar.getInstance();
		cal.setLenient(true);
		cal.setTime(new Date());
		cal.add(Calendar.YEAR, -1);
		Timestamp ts = new Timestamp(cal.getTime().getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.lastDayOfYear());
		return endOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

	/**
	 * Gets the end of the last year for a given date.
	 * @param currentDate The date.
	 * @return The end of the last year.
	 */
	public static Date endOfLastYear(Date currentDate) {
		Calendar cal = Calendar.getInstance();
		cal.setLenient(true);
		cal.setTime(currentDate);
		cal.add(Calendar.YEAR, -1);
		Timestamp ts = new Timestamp(cal.getTime().getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.lastDayOfYear());
		return endOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

	/**
	 * Gets the end of the month.
	 * @return The end of the month.
	 */
	public static Date endOfMonth() {
		Timestamp ts = new Timestamp(new Date().getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.lastDayOfMonth());
		return endOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

	/**
	 * Gets the end of the month for a given date.
	 * @param currentDate The date.
	 * @return The end of the month.
	 */
	public static Date endOfMonth(Date currentDate) {
		Timestamp ts = new Timestamp(currentDate.getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.lastDayOfMonth());
		return endOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

	/**
	 * Gets the end of the next month.
	 * @return The end of the next month.
	 */
	public static Date endOfNextMonth() {
		Calendar cal = Calendar.getInstance();
		cal.setLenient(true);
		cal.setTime(new Date());
		cal.add(Calendar.MONTH, 1);
		Timestamp ts = new Timestamp(cal.getTime().getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.lastDayOfMonth());
		return endOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

	/**
	 * Gets the end of the next week.
	 * @return The end of the next week.
	 */
	public static Date endOfNextWeek() {
		GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.setTime(addDaysToDate(new Date(), 7));
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}

	/**
	 * Gets the end of the next year.
	 * @return The end of the next year.
	 */
	public static Date endOfNextYear() {
		Calendar cal = Calendar.getInstance();
		cal.setLenient(true);
		cal.setTime(new Date());
		cal.add(Calendar.YEAR, 1);
		Timestamp ts = new Timestamp(cal.getTime().getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.lastDayOfYear());
		return endOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

	/**
	 * Gets the end of tomorrow.
	 * @return The end of tomorrow.
	 */
	public static Date endOfTomorrow() {
		return endOfDay(addDaysToDate(new Date(), 1));
	}

	/**
	 * Gets the end of tomorrow for a given date.
	 * @param currentDate The date.
	 * @return The end of tomorrow.
	 */
	public static Date endOfTomorrow(Date currentDate) {
		return endOfDay(addDaysToDate(currentDate, 1));
	}

	/**
	 * Gets the end of the week.
	 * @return The end of the week.
	 */
	public static Date endOfWeek() {
		GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.setTime(new Date());
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}

	/**
	 * Gets the end of the week for a given date.
	 * @param currentDate The date.
	 * @return The end of the week.
	 */
	public static Date endOfWeek(Date currentDate) {
		GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.setTime(currentDate);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}

	/**
	 * Gets the end of the year.
	 * @return The end of the year.
	 */
	public static Date endOfYear() {
		Timestamp ts = new Timestamp(new Date().getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.lastDayOfYear());
		return endOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

	/**
	 * Gets the end of yesterday.
	 * @return The end of yesterday.
	 */
	public static Date endOfYesterday() {
		return endOfDay(addDaysToDate(new Date(), -1));
	}

	/**
	 * Gets the end of yesterday for a given date.
	 * @param currentDate The date.
	 * @return The end of yesterday.
	 */
	public static Date endOfYesterday(Date currentDate) {
		return endOfDay(addDaysToDate(currentDate, -1));
	}

	/**
	 * Gets the current date.
	 * @return The current date.
	 */
	public static Date getCurrentDate() {
		return Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * Gets the current formatted date.
	 * @return The current formatted date.
	 */
	public static String getCurrentFormattedDate() {
		return getFormattedDate(getCurrentDate());
	}

	/**
	 * Gets the current timestamp.
	 * @return The current timestamp.
	 */
	public static Timestamp getCurrentTimeStamp() {
		return Timestamp.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * Gets the day of the month for a given date.
	 * @param date The date.
	 * @return The day of the month.
	 */
	public static final Integer getDayOfMonth(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Gets the day of the week for a given date.
	 * @param date The date.
	 * @return The day of the week.
	 */
	public static final Integer getDay(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * Gets the name of the day for a given date.
	 * @param date The date.
	 * @return The name of the day.
	 */
	public static final String getDayName(Date date) {
		if (date == null) {
			return null;
		}
		return dayFormatter.format(date);
	}

	/**
	 * Gets the formatted date for a given date.
	 * @param date The date.
	 * @return The formatted date.
	 */
	public static String getFormattedDate(Date date) {
		if (date == null) {
			return null;
		}
		return dateFormatter.format(date);
	}

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
		SimpleDateFormat formatter = COMPILED_FORMATTERS.get(pattern);
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
	 * Gets the formatted date time for a given date.
	 * @param date The date.
	 * @return The formatted date time.
	 */
	public static String getFormattedDateTime(Date date) {
		if (date == null) {
			return null;
		}
		return dateTimeFormatter.format(date);
	}

	/**
	 * Gets the formatted date time for a given date and pattern.
	 * @param date The date.
	 * @param pattern The pattern.
	 * @return The formatted date time.
	 */
	public static String getFormattedDateTime(Date date, String pattern) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat formatter = COMPILED_FORMATTERS.get(pattern);
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
	 * Gets the formatted time for a given date.
	 * @param date The date.
	 * @return The formatted time.
	 */
	public static String getFormattedTime(Date date) {
		if (date == null) {
			return null;
		}
		return timeFormatter.format(date);
	}

	/**
	 * Gets the hour of the day for a given date.
	 * @param date The date.
	 * @return The hour of the day.
	 */
	public static final Integer getHourOfDay(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * Gets the minutes for a given date.
	 * @param date The date.
	 * @return The minutes.
	 */
	public static final Integer getMinutes(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MINUTE);
	}

	/**
	 * Gets the seconds for a given date.
	 * @param date The date.
	 * @return The seconds.
	 */
	public static final Integer getSeconds(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.SECOND);
	}

	/**
	 * Gets the year for a given date.
	 * @param date The date.
	 * @return The year.
	 */
	public static final Integer getYear(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}

	/**
	 * Gets the month for a given date.
	 * @param date The date.
	 * @return The month.
	 */
	public static final Integer getMonth(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH);
	}

	/**
	 * Gets the name of the month for a given date.
	 * @param date The date.
	 * @return The name of the month.
	 */
	public static final String getMonthName(Date date) {
		if (date == null) {
			return null;
		}
		return monthFormatter.format(date);
	}

	/**
	 * Gets the time only for a given date.
	 * @param date The date.
	 * @return The time only.
	 */
	public static Timestamp getTimeOnly(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.YEAR, 1970);
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		return new Timestamp(calendar.getTime().getTime());
	}

	/**
	 * Gets the number of hours between two dates.
	 * @param startDate The start date.
	 * @param endDate The end date.
	 * @return The number of hours between the two dates.
	 */
	public static long hoursBetween(Date startDate, Date endDate) {
		long difference = (startDate.getTime() - endDate.getTime()) / 3600000;
		return Math.abs(difference);
	}

	/**
	 * Gets the number of minutes between two dates.
	 * @param startDate The start date.
	 * @param endDate The end date.
	 * @return The number of minutes between the two dates.
	 */
	public static long minutesBetween(Date startDate, Date endDate) {
		long difference = (startDate.getTime() - endDate.getTime()) / 60000;
		return Math.abs(difference);
	}

	/**
	 * Gets the remaining time until a timestamp.
	 * @param timestamp The timestamp.
	 * @return The remaining time.
	 */
	public static String remainingTime(Timestamp timestamp) {
		String value = null;
		if (timestamp == null) {
			return value;
		}
		LocalDateTime currentDateTime = LocalDateTime.now();
		LocalDateTime endDateTime = timestamp.toLocalDateTime();

		long between = ChronoUnit.SECONDS.between(currentDateTime, endDateTime);
		if (between > 0 && between < 60) {
			value = between == 1 ? between + " second" : between + " seconds";
		}
		if (between >= 60l) {
			between = ChronoUnit.MINUTES.between(currentDateTime, endDateTime);
			value = between == 1 ? between + " minute" : between + " minutes";
			if (between >= 60l) {
				between = ChronoUnit.HOURS.between(currentDateTime, endDateTime);
				value = between == 1 ? between + " hour" : between + " hours";

				if (between >= 24l) {
					between = ChronoUnit.DAYS.between(currentDateTime, endDateTime);
					value = between == 1 ? between + " day" : between + " days";

					if (between >= 7l) {
						between = ChronoUnit.WEEKS.between(currentDateTime, endDateTime);
						value = between == 1 ? between + " week" : between + " weeks";

						if (between >= 4l) {
							between = ChronoUnit.MONTHS.between(currentDateTime, endDateTime);
							value = between == 1 ? between + " month" : between + " months";

							if (between >= 12l) {
								between = ChronoUnit.YEARS.between(currentDateTime, endDateTime);
								value = between == 1 ? between + " year" : between + " years";
							}
						}
					}
				}
			}

		}

		return value;
	}

	/**
	 * Gets the start of the day.
	 * @return The start of the day.
	 */
	public static Date startOfDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * Gets the start of the day as a timestamp.
	 * @return The start of the day as a timestamp.
	 */
	public static Timestamp startOfDayAsTimestamp() {
		return new Timestamp(startOfDay().getTime());
	}

	/**
	 * Gets the start of the day for a given date.
	 * @param currentDate The date.
	 * @return The start of the day.
	 */
	public static Date startOfDay(Date currentDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * Gets the start of the day for a given date as a timestamp.
	 * @param currentDate The date.
	 * @return The start of the day as a timestamp.
	 */
	public static Timestamp startOfDayAsTimestamp(Date currentDate) {
		return new Timestamp(startOfDay(currentDate).getTime());
	}

	/**
	 * Gets the start of the day in the future.
	 * @param numberOfDaysFuture The number of days in the future.
	 * @return The start of the day in the future.
	 */
	public static Timestamp startOfDayFuture(Integer numberOfDaysFuture) {
		return new Timestamp(startOfDay(addDaysToDate(new Date(), Math.abs(numberOfDaysFuture))).getTime());
	}

	/**
	 * Gets the start of the day in the past.
	 * @param numberOfDaysAgo The number of days in the past.
	 * @return The start of the day in the past.
	 */
	public static Timestamp startOfDayInPast(Integer numberOfDaysAgo) {
		return new Timestamp(startOfDay(addDaysToDate(new Date(), Math.abs(numberOfDaysAgo) * -1)).getTime());
	}

	/**
	 * Gets the start of the last month.
	 * @return The start of the last month.
	 */
	public static Date startOfLastMonth() {
		Calendar cal = Calendar.getInstance();
		cal.setLenient(true);
		cal.setTime(new Date());
		cal.add(Calendar.MONTH, -1);
		Timestamp ts = new Timestamp(cal.getTime().getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.firstDayOfMonth());
		return startOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

	/**
	 * Gets the start of the last month for a given date.
	 * @param currentDate The date.
	 * @return The start of the last month.
	 */
	public static Date startOfLastMonth(Date currentDate) {
		Calendar cal = Calendar.getInstance();
		cal.setLenient(true);
		cal.setTime(currentDate);
		cal.add(Calendar.MONTH, -1);
		Timestamp ts = new Timestamp(cal.getTime().getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.firstDayOfMonth());
		return startOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

	/**
	 * Gets the start of the last week.
	 * @return The start of the last week.
	 */
	public static Date startOfLastWeek() {
		GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.setTime(addDaysToDate(new Date(), -7));
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * Gets the start of the last year.
	 * @return The start of the last year.
	 */
	public static Date startOfLastYear() {
		Calendar cal = Calendar.getInstance();
		cal.setLenient(true);
		cal.setTime(new Date());
		cal.add(Calendar.YEAR, -1);
		Timestamp ts = new Timestamp(cal.getTime().getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.firstDayOfYear());
		return Date.from(from.toInstant(ZoneOffset.UTC));
	}

	/**
	 * Gets the start of the last year for a given date.
	 * @param currentDate The date.
	 * @return The start of the last year.
	 */
	public static Date startOfLastYear(Date currentDate) {
		Calendar cal = Calendar.getInstance();
		cal.setLenient(true);
		cal.setTime(currentDate);
		cal.add(Calendar.YEAR, -1);
		Timestamp ts = new Timestamp(cal.getTime().getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.firstDayOfYear());
		return startOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

	/**
	 * Gets the start of the month.
	 * @return The start of the month.
	 */
	public static Date startOfMonth() {
		Timestamp ts = new Timestamp(new Date().getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.firstDayOfMonth());
		return startOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

	/**
	 * Gets the start of the month for a given date.
	 * @param currentDate The date.
	 * @return The start of the month.
	 */
	public static Date startOfMonth(Date currentDate) {
		Timestamp ts = new Timestamp(currentDate.getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.firstDayOfMonth());
		return startOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

	/**
	 * Gets the start of the next month.
	 * @return The start of the next month.
	 */
	public static Date startOfNextMonth() {
		Calendar cal = Calendar.getInstance();
		cal.setLenient(true);
		cal.setTime(new Date());
		cal.add(Calendar.MONTH, 1);
		Timestamp ts = new Timestamp(cal.getTime().getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.firstDayOfMonth());
		return startOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

	/**
	 * Gets the start of the next week.
	 * @return The start of the next week.
	 */
	public static Date startOfNextWeek() {
		GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.setTime(addDaysToDate(new Date(), 7));
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * Gets the start of the next year.
	 * @return The start of the next year.
	 */
	public static Date startOfNextYear() {
		Calendar cal = Calendar.getInstance();
		cal.setLenient(true);
		cal.setTime(new Date());
		cal.add(Calendar.YEAR, 1);
		Timestamp ts = new Timestamp(cal.getTime().getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.firstDayOfYear());
		return startOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

	/**
	 * Gets the start of tomorrow.
	 * @return The start of tomorrow.
	 */
	public static Date startOfTomorrow() {
		return startOfDay(addDaysToDate(new Date(), 1));
	}

	/**
	 * Gets the start of tomorrow for a given date.
	 * @param currentDate The date.
	 * @return The start of tomorrow.
	 */
	public static Date startOfTomorrow(Date currentDate) {
		return startOfDay(addDaysToDate(currentDate, 1));
	}

	/**
	 * Gets the start of the week.
	 * @return The start of the week.
	 */
	public static Date startOfWeek() {
		GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.setTime(new Date());
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * Gets the start of the week for a given date.
	 * @param currentDate The date.
	 * @return The start of the week.
	 */
	public static Date startOfWeek(Date currentDate) {
		GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.setTime(currentDate);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * Gets the start of the year.
	 * @return The start of the year.
	 */
	public static Date startOfYear() {
		Timestamp ts = new Timestamp(new Date().getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.firstDayOfYear());
		return startOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

	/**
	 * Gets the start of the year for a given date.
	 * @param currentDate The date.
	 * @return The start of the year.
	 */
	public static Date startOfYear(Date currentDate) {
		Timestamp ts = new Timestamp(currentDate.getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.firstDayOfYear());
		return startOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

	/**
	 * Gets the start of yesterday.
	 * @return The start of yesterday.
	 */
	public static Date startOfYesterday() {
		return startOfDay(addDaysToDate(new Date(), -1));
	}

	/**
	 * Gets the start of yesterday for a given date.
	 * @param currentDate The date.
	 * @return The start of yesterday.
	 */
	public static Date startOfYesterday(Date currentDate) {
		return startOfDay(addDaysToDate(currentDate, -1));
	}

	/**
	 * Converts a LocalDate to a Date.
	 * @param localDate The LocalDate to convert.
	 * @return The converted Date.
	 */
	public static Date toDateFromLocalDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * Converts a LocalDateTime to a Date.
	 * @param localDateTime The LocalDateTime to convert.
	 * @return The converted Date.
	 */
	public static Date toDateFromLocalDateTime(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * Converts a Date to a LocalDate.
	 * @param date The Date to convert.
	 * @return The converted LocalDate.
	 */
	public static LocalDate toLocalDateFromDate(Date date) {
		Instant instant = Instant.ofEpochMilli(date.getTime());
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
	}

	/**
	 * Converts a Date to a LocalDateTime.
	 * @param date The Date to convert.
	 * @return The converted LocalDateTime.
	 */
	public static LocalDateTime toLocalDateTimeFromDate(Date date) {
		Instant instant = Instant.ofEpochMilli(date.getTime());
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
	}

	/**
	 * Gets the number of years between two dates using ChronoUnit.
	 * @param startDate The start date.
	 * @param endDate The end date.
	 * @return The number of years between the two dates.
	 */
	public static long yearsBetweenUsingChronoUnit(Date startDate, Date endDate) {
		return ChronoUnit.YEARS.between(toLocalDateFromDate(startDate), toLocalDateFromDate(endDate));
	}

	/**
	 * Gets the time only for a given date.
	 * @param date The date.
	 * @return The time only.
	 */
	public static Date justTimeAsDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.YEAR, 1970);
		cal.set(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_YEAR, 1);
		return cal.getTime();
	}

	/**
	 * Gets the time only from a timestamp.
	 * @param date The timestamp.
	 * @return The time only.
	 */
	public static Timestamp justTimeFromTimestamp(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.YEAR, 1970);
		cal.set(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_YEAR, 1);
		return new Timestamp(cal.getTime().getTime());
	}

	/**
	 * Sets the time for a date.
	 * @param time The time to set.
	 * @param date The date to set the time for.
	 * @return The new date.
	 */
	public static Timestamp setTimeForDate(Timestamp time, Timestamp date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		cal.set(Calendar.YEAR, getYear(date));
		cal.set(Calendar.MONTH, getMonth(date));
		cal.set(Calendar.DAY_OF_MONTH, getDayOfMonth(date));
		return new Timestamp(cal.getTime().getTime());
	}
}
