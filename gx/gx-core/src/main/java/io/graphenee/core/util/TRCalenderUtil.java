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
 * @author tahamalik
 */
public class TRCalenderUtil {

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
	public static SimpleDateFormat dateWithTimeFormatter = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");

	public static long minutesBetween(Date startDate, Date endDate) {

		long difference = (startDate.getTime() - endDate.getTime()) / 60000;

		return Math.abs(difference);
	}

	public static long hoursBetween(Date startDate, Date endDate) {

		long difference = (startDate.getTime() - endDate.getTime()) / 3600000;

		return Math.abs(difference);
	}

	public static long daysBetween(Date startDate, Date endDate) {

		long difference = (startDate.getTime() - endDate.getTime()) / 86400000;

		return Math.abs(difference);
	}

	public static long yearsBetweenUsingChronoUnit(Date startDate, Date endDate) {
		return ChronoUnit.YEARS.between(toLocalDateFromDate(startDate), toLocalDateFromDate(endDate));
	}

	public static long daysBetweenUsingChronoUnit(Date startDate, Date endDate) {
		return ChronoUnit.DAYS.between(toLocalDateFromDate(startDate), toLocalDateFromDate(endDate));
	}

	public static Date toDateFromLocalDate(LocalDate localDate) {

		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

	}

	public static Date toDateFromLocalDateTime(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

	}

	public static String getCurrentFormattedDate() {
		return getFormattedDate(getCurrentDate());
	}

	public static Date getCurrentDate() {

		return Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

	}

	public static Timestamp getCurrentTimeStamp() {

		return Timestamp.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());

	}

	public static LocalDate toLocalDateFromDate(Date date) {
		Instant instant = Instant.ofEpochMilli(date.getTime());
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
	}

	public static LocalDateTime toLocalDateTimeFromDate(Date date) {
		Instant instant = Instant.ofEpochMilli(date.getTime());
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
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
			synchronized (TRCalenderUtil.class) {
				if (formatter == null) {
					formatter = new SimpleDateFormat(pattern);
					COMPILED_FORMATTERS.put(pattern, formatter);
				}
			}
		}
		return formatter.format(date);
	}

	public static String getFormattedDateTime(Timestamp date, String pattern) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat formatter = COMPILED_FORMATTERS.get(pattern);
		if (formatter == null) {
			synchronized (TRCalenderUtil.class) {
				if (formatter == null) {
					formatter = new SimpleDateFormat(pattern);
					COMPILED_FORMATTERS.put(pattern, formatter);
				}
			}
		}
		return formatter.format(date);
	}

	public static String getFormattedTime(Timestamp date) {
		if (date == null) {
			return null;
		}
		return timeFormatter.format(date);
	}

	public static String getFormattedDateTime(Timestamp date) {
		if (date == null) {
			return null;
		}
		return dateTimeFormatter.format(date);
	}

	public static String getFormattedDate(Timestamp date) {
		if (date == null) {
			return null;
		}
		return dateFormatter.format(date);
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
			value = between + " Minutes Ago.";
			if (between >= 60l) {
				between = ChronoUnit.HOURS.between(localDateTime, currentLocalDateTime);
				value = between + " Hours Ago.";

				if (between >= 24l) {
					between = ChronoUnit.DAYS.between(localDateTime, currentLocalDateTime);
					value = between + " Days Ago.";

					if (between >= 7l) {
						between = ChronoUnit.WEEKS.between(localDateTime, currentLocalDateTime);
						value = between + " Weeks Ago.";

						if (between >= 4l) {
							between = ChronoUnit.MONTHS.between(localDateTime, currentLocalDateTime);
							value = between + " Months Ago.";

							if (between >= 12l) {
								between = ChronoUnit.YEARS.between(localDateTime, currentLocalDateTime);
								value = between + " Years Ago.";

							}
						}
					}
				}
			}

		}

		return value;
	}

	public static Date addDaysToDate(Date currentDate, int days) {
		LocalDate now = toLocalDateFromDate(currentDate);
		now = now.plusDays(days);
		return toDateFromLocalDate(now);
	}

	public static Date addMinutesToDate(Date currentDate, int minutes) {
		LocalDateTime now = toLocalDateTimeFromDate(currentDate);
		now = now.plusMinutes(minutes);
		return toDateFromLocalDateTime(now);
	}

	public static Date startOfDayThisMonth(Date currentDate) {
		Timestamp ts = new Timestamp(currentDate.getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.firstDayOfMonth());
		return Date.from(from.toInstant(ZoneOffset.UTC));
	}

	public static Date endOfDayThisMonth(Date currentDate) {
		Timestamp ts = new Timestamp(currentDate.getTime());
		LocalDateTime from = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).with(TemporalAdjusters.lastDayOfMonth());
		return Date.from(from.toInstant(ZoneOffset.UTC));
	}

	public static Date startOfWeekThisMonth(Date currentDate) {
		GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
		cal.setTime(currentDate);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 1);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static Date endOfDayYesterday(Date currentDate) {
		GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
		cal.setTime(currentDate);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static Date endOfSameDay(Date currentDate) {
		GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
		cal.setTime(currentDate);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static Date startOfTomorrow(Date currentDate) {
		GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
		cal.setTime(currentDate);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static Date startOfSameDay(Date currentDate) {
		GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
		cal.setTime(currentDate);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static Timestamp startOfDay(Integer numberOfDaysAgo) {
		LocalDateTime sinceDate = LocalDateTime.now().minus(numberOfDaysAgo, ChronoUnit.DAYS);
		sinceDate = sinceDate.minusHours(sinceDate.getHour()).minusMinutes(sinceDate.getMinute());
		return Timestamp.valueOf(sinceDate);
	}

	public static Timestamp startOfDayInPast(Integer numberOfDaysAgo) {
		LocalDateTime sinceDate = LocalDateTime.now().minus(numberOfDaysAgo, ChronoUnit.DAYS);
		sinceDate = sinceDate.minusHours(sinceDate.getHour()).minusMinutes(sinceDate.getMinute());
		return Timestamp.valueOf(sinceDate);
	}

	public static Timestamp startOfDayFuture(Integer numberOfDaysFuture) {
		LocalDateTime sinceDate = LocalDateTime.now().plus(numberOfDaysFuture, ChronoUnit.DAYS);
		sinceDate = sinceDate.minusHours(sinceDate.getHour()).minusMinutes(sinceDate.getMinute());
		return Timestamp.valueOf(sinceDate);
	}

	public static int calendarFieldFromDate(int calendarField, Timestamp date) {
		GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
		cal.setTime(date);
		return cal.get(calendarField);
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

	public static final String getDayName(Timestamp timestamp) {
		if (timestamp == null) {
			return null;
		}
		return dayFormatter.format(timestamp);
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

	public static final String getMonthName(Timestamp timestamp) {
		if (timestamp == null) {
			return null;
		}
		return monthFormatter.format(timestamp);
	}

	public static Timestamp getTimeOnly(Timestamp timestamp) {
		if (timestamp == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(timestamp);
		calendar.set(Calendar.YEAR, 1970);
		calendar.set(Calendar.MONTH, 1);
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		return new Timestamp(calendar.getTime().getTime());
	}

}
