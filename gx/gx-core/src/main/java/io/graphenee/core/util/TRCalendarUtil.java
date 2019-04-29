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
package io.graphenee.core.util;

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

	private static Map<String, SimpleDateFormat> COMPILED_FORMATTERS = new ConcurrentHashMap<>();

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
	public static SimpleDateFormat zuluTimeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	public static Date addDaysToDate(Date currentDate, int days) {
		LocalDate now = toLocalDateFromDate(currentDate);
		now = now.plusDays(days);
		return toDateFromLocalDate(now);
	}

	public static Date minusDaysToDate(Date currentDate, int days) {
		LocalDate now = toLocalDateFromDate(currentDate);
		now = now.minusDays(days);
		return toDateFromLocalDate(now);
	}

	public static Date addMinutesToDate(Date currentDate, int minutes) {
		LocalDateTime now = toLocalDateTimeFromDate(currentDate);
		now = now.plusMinutes(minutes);
		return toDateFromLocalDateTime(now);
	}

	public static Date addMonthsToDate(Date currentDate, int months) {
		LocalDateTime now = toLocalDateTimeFromDate(currentDate);
		now = now.plusMonths(months);
		return toDateFromLocalDateTime(now);
	}

	public static int calendarFieldFromDate(int calendarField, Date date) {
		GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
		cal.setTime(date);
		return cal.get(calendarField);
	}

	public static long monthsBetween(Date startDate, Date endDate) {
		return monthsBetweenUsingChronoUnit(startDate, endDate);
	}

	public static long monthsBetweenUsingChronoUnit(Date startDate, Date endDate) {
		return Math.abs(ChronoUnit.MONTHS.between(toLocalDateFromDate(startDate), toLocalDateFromDate(endDate)));
	}

	public static long daysBetween(Date startDate, Date endDate) {
		return daysBetweenUsingChronoUnit(startDate, endDate);
	}

	public static long daysBetweenUsingChronoUnit(Date startDate, Date endDate) {
		return Math.abs(ChronoUnit.DAYS.between(toLocalDateFromDate(startDate), toLocalDateFromDate(endDate)));
	}

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
			value = between + " minutes ago";
			if (between >= 60l) {
				between = ChronoUnit.HOURS.between(localDateTime, currentLocalDateTime);
				value = between + " hours ago";

				if (between >= 24l) {
					between = ChronoUnit.DAYS.between(localDateTime, currentLocalDateTime);
					value = between + " days ago";

					if (between >= 7l) {
						between = ChronoUnit.WEEKS.between(localDateTime, currentLocalDateTime);
						value = between + " weeks ago";

						if (between >= 4l) {
							between = ChronoUnit.MONTHS.between(localDateTime, currentLocalDateTime);
							value = between + " months ago";

							if (between >= 12l) {
								between = ChronoUnit.YEARS.between(localDateTime, currentLocalDateTime);
								value = between + " years ago";

							}
						}
					}
				}
			}

		} else {
			value = between + " seconds ago";
		}

		return value;
	}

	public static String age(Timestamp timestamp) {
		long diff = monthsBetween(timestamp, getCurrentTimeStamp());
		int years = (int) diff / 12;
		int remainingMonths = (int) diff % 12;
		return years + "y " + remainingMonths + "m";
	}

	public static Date endOfDay() {
		GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}

	public static Timestamp endOfDayAsTimestamp() {
		return new Timestamp(endOfDay().getTime());
	}

	public static Date endOfDay(Date currentDate) {
		GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
		cal.setTime(currentDate);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}

	public static Timestamp endOfDayAsTimestamp(Date currentDate) {
		return new Timestamp(endOfDay(currentDate).getTime());
	}

	public static Date endOfLastMonth() {
		Calendar cal = Calendar.getInstance();
		cal.setLenient(true);
		cal.setTime(new Date());
		cal.add(Calendar.MONTH, -1);
		Timestamp ts = new Timestamp(cal.getTime().getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.lastDayOfMonth());
		return endOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

	public static Date endOfLastMonth(Date currentDate) {
		Calendar cal = Calendar.getInstance();
		cal.setLenient(true);
		cal.setTime(currentDate);
		cal.add(Calendar.MONTH, -1);
		Timestamp ts = new Timestamp(cal.getTime().getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.lastDayOfMonth());
		return endOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

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

	public static Date endOfLastYear() {
		Calendar cal = Calendar.getInstance();
		cal.setLenient(true);
		cal.setTime(new Date());
		cal.add(Calendar.YEAR, -1);
		Timestamp ts = new Timestamp(cal.getTime().getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.lastDayOfYear());
		return endOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

	public static Date endOfLastYear(Date currentDate) {
		Calendar cal = Calendar.getInstance();
		cal.setLenient(true);
		cal.setTime(currentDate);
		cal.add(Calendar.YEAR, -1);
		Timestamp ts = new Timestamp(cal.getTime().getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.lastDayOfYear());
		return endOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

	public static Date endOfMonth() {
		Timestamp ts = new Timestamp(new Date().getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.lastDayOfMonth());
		return endOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

	public static Date endOfMonth(Date currentDate) {
		Timestamp ts = new Timestamp(currentDate.getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.lastDayOfMonth());
		return endOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

	public static Date endOfNextMonth() {
		Calendar cal = Calendar.getInstance();
		cal.setLenient(true);
		cal.setTime(new Date());
		cal.add(Calendar.MONTH, 1);
		Timestamp ts = new Timestamp(cal.getTime().getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.lastDayOfMonth());
		return endOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

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

	public static Date endOfNextYear() {
		Calendar cal = Calendar.getInstance();
		cal.setLenient(true);
		cal.setTime(new Date());
		cal.add(Calendar.YEAR, 1);
		Timestamp ts = new Timestamp(cal.getTime().getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.lastDayOfYear());
		return endOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

	public static Date endOfTomorrow() {
		return endOfDay(addDaysToDate(new Date(), 1));
	}

	public static Date endOfTomorrow(Date currentDate) {
		return endOfDay(addDaysToDate(currentDate, 1));
	}

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

	public static Date endOfYear() {
		Timestamp ts = new Timestamp(new Date().getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.lastDayOfYear());
		return endOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

	public static Date endOfYesterday() {
		return endOfDay(addDaysToDate(new Date(), -1));
	}

	public static Date endOfYesterday(Date currentDate) {
		return endOfDay(addDaysToDate(currentDate, -1));
	}

	public static Date getCurrentDate() {
		return Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	public static String getCurrentFormattedDate() {
		return getFormattedDate(getCurrentDate());
	}

	public static Timestamp getCurrentTimeStamp() {
		return Timestamp.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
	}

	public static final Integer getDayOfMonth(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public static final Integer getDay(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	public static final String getDayName(Date date) {
		if (date == null) {
			return null;
		}
		return dayFormatter.format(date);
	}

	public static String getFormattedDate(Date date) {
		if (date == null) {
			return null;
		}
		return dateFormatter.format(date);
	}

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

	public static String getFormattedDateTime(Date date) {
		if (date == null) {
			return null;
		}
		return dateTimeFormatter.format(date);
	}

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

	public static String getFormattedTime(Date date) {
		if (date == null) {
			return null;
		}
		return timeFormatter.format(date);
	}

	public static final Integer getHourOfDay(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	public static final Integer getMinutes(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MINUTE);
	}

	public static final Integer getSeconds(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.SECOND);
	}

	public static final Integer getYear(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}

	public static final Integer getMonth(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH);
	}

	public static final String getMonthName(Date date) {
		if (date == null) {
			return null;
		}
		return monthFormatter.format(date);
	}

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

	public static long hoursBetween(Date startDate, Date endDate) {
		long difference = (startDate.getTime() - endDate.getTime()) / 3600000;
		return Math.abs(difference);
	}

	public static long minutesBetween(Date startDate, Date endDate) {
		long difference = (startDate.getTime() - endDate.getTime()) / 60000;
		return Math.abs(difference);
	}

	public static String remainingTime(Timestamp timestamp) {
		String value = null;
		if (timestamp == null) {
			return value;
		}
		LocalDateTime currentDateTime = LocalDateTime.now();
		LocalDateTime endDateTime = timestamp.toLocalDateTime();

		long between = ChronoUnit.SECONDS.between(currentDateTime, endDateTime);
		if (between > 0 && between < 60) {
			value = "After " + between + " Seconds.";
		}
		if (between >= 60l) {
			between = ChronoUnit.MINUTES.between(currentDateTime, endDateTime);
			value = "After " + between + " Minutes.";
			if (between >= 60l) {
				between = ChronoUnit.HOURS.between(currentDateTime, endDateTime);
				value = "After " + between + " Hours.";

				if (between >= 24l) {
					between = ChronoUnit.DAYS.between(currentDateTime, endDateTime);
					value = "After " + between + " Days.";

					if (between >= 7l) {
						between = ChronoUnit.WEEKS.between(currentDateTime, endDateTime);
						value = "After " + between + " Week.";

						if (between >= 4l) {
							between = ChronoUnit.MONTHS.between(currentDateTime, endDateTime);
							value = "After " + between + " Months.";

							if (between >= 12l) {
								between = ChronoUnit.YEARS.between(currentDateTime, endDateTime);
								value = "After " + between + " Years.";
							}
						}
					}
				}
			}

		}
		return value;
	}

	public static Date startOfDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Timestamp startOfDayAsTimestamp() {
		return new Timestamp(startOfDay().getTime());
	}

	public static Date startOfDay(Date currentDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Timestamp startOfDayAsTimestamp(Date currentDate) {
		return new Timestamp(startOfDay(currentDate).getTime());
	}

	public static Timestamp startOfDayFuture(Integer numberOfDaysFuture) {
		return new Timestamp(startOfDay(addDaysToDate(new Date(), Math.abs(numberOfDaysFuture))).getTime());
	}

	public static Timestamp startOfDayInPast(Integer numberOfDaysAgo) {
		return new Timestamp(startOfDay(addDaysToDate(new Date(), Math.abs(numberOfDaysAgo) * -1)).getTime());
	}

	public static Date startOfLastMonth() {
		Calendar cal = Calendar.getInstance();
		cal.setLenient(true);
		cal.setTime(new Date());
		cal.add(Calendar.MONTH, -1);
		Timestamp ts = new Timestamp(cal.getTime().getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.firstDayOfMonth());
		return startOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

	public static Date startOfLastMonth(Date currentDate) {
		Calendar cal = Calendar.getInstance();
		cal.setLenient(true);
		cal.setTime(currentDate);
		cal.add(Calendar.MONTH, -1);
		Timestamp ts = new Timestamp(cal.getTime().getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.firstDayOfMonth());
		return startOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

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

	public static Date startOfLastYear() {
		Calendar cal = Calendar.getInstance();
		cal.setLenient(true);
		cal.setTime(new Date());
		cal.add(Calendar.YEAR, -1);
		Timestamp ts = new Timestamp(cal.getTime().getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.firstDayOfYear());
		return Date.from(from.toInstant(ZoneOffset.UTC));
	}

	public static Date startOfLastYear(Date currentDate) {
		Calendar cal = Calendar.getInstance();
		cal.setLenient(true);
		cal.setTime(currentDate);
		cal.add(Calendar.YEAR, -1);
		Timestamp ts = new Timestamp(cal.getTime().getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.firstDayOfYear());
		return startOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

	public static Date startOfMonth() {
		Timestamp ts = new Timestamp(new Date().getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.firstDayOfMonth());
		return startOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

	public static Date startOfMonth(Date currentDate) {
		Timestamp ts = new Timestamp(currentDate.getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.firstDayOfMonth());
		return startOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

	public static Date startOfNextMonth() {
		Calendar cal = Calendar.getInstance();
		cal.setLenient(true);
		cal.setTime(new Date());
		cal.add(Calendar.MONTH, 1);
		Timestamp ts = new Timestamp(cal.getTime().getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.firstDayOfMonth());
		return startOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

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

	public static Date startOfNextYear() {
		Calendar cal = Calendar.getInstance();
		cal.setLenient(true);
		cal.setTime(new Date());
		cal.add(Calendar.YEAR, 1);
		Timestamp ts = new Timestamp(cal.getTime().getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.firstDayOfYear());
		return startOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

	public static Date startOfTomorrow() {
		return startOfDay(addDaysToDate(new Date(), 1));
	}

	public static Date startOfTomorrow(Date currentDate) {
		return startOfDay(addDaysToDate(currentDate, 1));
	}

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

	public static Date startOfYear() {
		Timestamp ts = new Timestamp(new Date().getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.firstDayOfYear());
		return startOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

	public static Date startOfYear(Date currentDate) {
		Timestamp ts = new Timestamp(currentDate.getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.firstDayOfYear());
		return startOfDay(Date.from(from.toInstant(ZoneOffset.UTC)));
	}

	public static Date startOfYesterday() {
		return startOfDay(addDaysToDate(new Date(), -1));
	}

	public static Date startOfYesterday(Date currentDate) {
		return startOfDay(addDaysToDate(currentDate, -1));
	}

	public static Date toDateFromLocalDate(LocalDate localDate) {

		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

	}

	public static Date toDateFromLocalDateTime(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

	}

	public static LocalDate toLocalDateFromDate(Date date) {
		Instant instant = Instant.ofEpochMilli(date.getTime());
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
	}

	public static LocalDateTime toLocalDateTimeFromDate(Date date) {
		Instant instant = Instant.ofEpochMilli(date.getTime());
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
	}

	public static long yearsBetweenUsingChronoUnit(Date startDate, Date endDate) {
		return ChronoUnit.YEARS.between(toLocalDateFromDate(startDate), toLocalDateFromDate(endDate));
	}

	public static Date justTimeAsDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.YEAR, 1970);
		cal.set(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_YEAR, 1);
		return cal.getTime();
	}

	public static Timestamp justTimeFromTimestamp(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.YEAR, 1970);
		cal.set(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_YEAR, 1);
		return new Timestamp(cal.getTime().getTime());
	}

	public static Timestamp setTimeForDate(Timestamp time, Timestamp date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		cal.set(Calendar.YEAR, getYear(date));
		cal.set(Calendar.MONTH, getMonth(date));
		cal.set(Calendar.DAY_OF_MONTH, getDayOfMonth(date));
		return new Timestamp(cal.getTime().getTime());
	}
}
