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

import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class TRCalendarUtilTest {

	@Test
	public void testAddDaysToDate() {
		Date today = new Date();
		Date testDate = TRCalendarUtil.addDaysToDate(today, 1);
		assertTrue(today.before(testDate));

		today = new Date();
		testDate = TRCalendarUtil.addDaysToDate(today, -1);
		assertTrue(today.after(testDate));
	}

	@Test
	public void testAddMinutesToDate() {
		Date now = new Date();
		Date testDate1 = TRCalendarUtil.addMinutesToDate(now, 1);
		Date testDate2 = TRCalendarUtil.addMinutesToDate(now, 2);
		assertTrue(testDate1.after(now) && testDate1.before(testDate2));

		now = new Date();
		testDate1 = TRCalendarUtil.addMinutesToDate(now, -1);
		testDate2 = TRCalendarUtil.addMinutesToDate(now, -2);
		assertTrue(testDate1.before(now) && testDate1.after(testDate2));
	}

	@Test
	public void testDaysBetween() {
		Date now = new Date();
		Date after3Days = TRCalendarUtil.addDaysToDate(now, 3);
		long daysBetween = TRCalendarUtil.daysBetween(now, after3Days);
		assertTrue(daysBetween == 3);
	}

	@Test
	public void testDaysBetweenUsingChronoUnit() {
		Date now = new Date();
		Date before2Days = TRCalendarUtil.addDaysToDate(now, -2);
		long daysBetween = TRCalendarUtil.daysBetweenUsingChronoUnit(now, before2Days);
		assertTrue(daysBetween == 2);
	}

	@Test
	public void testElapsedTime() {
		Date now = new Date();
		String elapsedTime = TRCalendarUtil.elapsedTime(new Timestamp(now.getTime()));
		assertTrue(elapsedTime.endsWith(" seconds ago"));

		now = TRCalendarUtil.addMinutesToDate(now, -5);
		elapsedTime = TRCalendarUtil.elapsedTime(new Timestamp(now.getTime()));
		assertTrue(elapsedTime.endsWith(" minutes ago"));

		now = TRCalendarUtil.addMinutesToDate(now, -120);
		elapsedTime = TRCalendarUtil.elapsedTime(new Timestamp(now.getTime()));
		assertTrue(elapsedTime.endsWith(" hours ago"));

		now = TRCalendarUtil.addDaysToDate(now, -2);
		elapsedTime = TRCalendarUtil.elapsedTime(new Timestamp(now.getTime()));
		assertTrue(elapsedTime.endsWith(" days ago"));

		now = TRCalendarUtil.addDaysToDate(now, -200);
		elapsedTime = TRCalendarUtil.elapsedTime(new Timestamp(now.getTime()));
		assertTrue(elapsedTime.endsWith(" months ago"));

		now = TRCalendarUtil.addDaysToDate(now, -1600);
		elapsedTime = TRCalendarUtil.elapsedTime(new Timestamp(now.getTime()));
		assertTrue(elapsedTime.endsWith(" years ago"));
	}

	@Test
	public void testEndOfDay() {
		Date date = TRCalendarUtil.endOfDay();
		int h = TRCalendarUtil.calendarFieldFromDate(Calendar.HOUR_OF_DAY, date);
		int m = TRCalendarUtil.calendarFieldFromDate(Calendar.MINUTE, date);
		int s = TRCalendarUtil.calendarFieldFromDate(Calendar.SECOND, date);
		int ms = TRCalendarUtil.calendarFieldFromDate(Calendar.MILLISECOND, date);
		assertTrue(h == 23 && m == 59 && s == 59 && ms == 999);

	}

	@Test
	public void testEndOfDayDate() {
		Date date = TRCalendarUtil.endOfDay(new Date());
		int h = TRCalendarUtil.calendarFieldFromDate(Calendar.HOUR_OF_DAY, date);
		int m = TRCalendarUtil.calendarFieldFromDate(Calendar.MINUTE, date);
		int s = TRCalendarUtil.calendarFieldFromDate(Calendar.SECOND, date);
		int ms = TRCalendarUtil.calendarFieldFromDate(Calendar.MILLISECOND, date);
		assertTrue(h == 23 && m == 59 && s == 59 && ms == 999);
	}

	@Test
	public void testJustTimeAsDate() {
		Date today = new Date();
		Date past = TRCalendarUtil.justTimeAsDate(today);
		System.out.println(today + " => " + past);
		assert (today.after(past));
	}

}
