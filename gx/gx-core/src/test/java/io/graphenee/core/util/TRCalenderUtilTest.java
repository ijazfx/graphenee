package io.graphenee.core.util;

import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class TRCalenderUtilTest {

	@Test
	public void testAddDaysToDate() {
		Date today = new Date();
		Date testDate = TRCalenderUtil.addDaysToDate(today, 1);
		assertTrue(today.before(testDate));

		today = new Date();
		testDate = TRCalenderUtil.addDaysToDate(today, -1);
		assertTrue(today.after(testDate));
	}

	@Test
	public void testAddMinutesToDate() {
		Date now = new Date();
		Date testDate1 = TRCalenderUtil.addMinutesToDate(now, 1);
		Date testDate2 = TRCalenderUtil.addMinutesToDate(now, 2);
		assertTrue(testDate1.after(now) && testDate1.before(testDate2));

		now = new Date();
		testDate1 = TRCalenderUtil.addMinutesToDate(now, -1);
		testDate2 = TRCalenderUtil.addMinutesToDate(now, -2);
		assertTrue(testDate1.before(now) && testDate1.after(testDate2));
	}

	@Test
	public void testDaysBetween() {
		Date now = new Date();
		Date after3Days = TRCalenderUtil.addDaysToDate(now, 3);
		long daysBetween = TRCalenderUtil.daysBetween(now, after3Days);
		assertTrue(daysBetween == 3);
	}

	@Test
	public void testDaysBetweenUsingChronoUnit() {
		Date now = new Date();
		Date before2Days = TRCalenderUtil.addDaysToDate(now, -2);
		long daysBetween = TRCalenderUtil.daysBetweenUsingChronoUnit(now, before2Days);
		assertTrue(daysBetween == 2);
	}

	@Test
	public void testElapsedTime() {
		Date now = new Date();
		String elapsedTime = TRCalenderUtil.elapsedTime(new Timestamp(now.getTime()));
		assertTrue(elapsedTime.endsWith(" seconds ago"));

		now = TRCalenderUtil.addMinutesToDate(now, -5);
		elapsedTime = TRCalenderUtil.elapsedTime(new Timestamp(now.getTime()));
		assertTrue(elapsedTime.endsWith(" minutes ago"));

		now = TRCalenderUtil.addMinutesToDate(now, -120);
		elapsedTime = TRCalenderUtil.elapsedTime(new Timestamp(now.getTime()));
		assertTrue(elapsedTime.endsWith(" hours ago"));

		now = TRCalenderUtil.addDaysToDate(now, -2);
		elapsedTime = TRCalenderUtil.elapsedTime(new Timestamp(now.getTime()));
		assertTrue(elapsedTime.endsWith(" days ago"));

		now = TRCalenderUtil.addDaysToDate(now, -200);
		elapsedTime = TRCalenderUtil.elapsedTime(new Timestamp(now.getTime()));
		assertTrue(elapsedTime.endsWith(" months ago"));

		now = TRCalenderUtil.addDaysToDate(now, -1600);
		elapsedTime = TRCalenderUtil.elapsedTime(new Timestamp(now.getTime()));
		assertTrue(elapsedTime.endsWith(" years ago"));
	}

	@Test
	public void testEndOfDay() {
		Date date = TRCalenderUtil.endOfDay();
		int h = TRCalenderUtil.calendarFieldFromDate(Calendar.HOUR_OF_DAY, date);
		int m = TRCalenderUtil.calendarFieldFromDate(Calendar.MINUTE, date);
		int s = TRCalenderUtil.calendarFieldFromDate(Calendar.SECOND, date);
		int ms = TRCalenderUtil.calendarFieldFromDate(Calendar.MILLISECOND, date);
		assertTrue(h == 23 && m == 59 && s == 59 && ms == 999);

	}

	@Test
	public void testEndOfDayDate() {
		Date date = TRCalenderUtil.endOfDay(new Date());
		int h = TRCalenderUtil.calendarFieldFromDate(Calendar.HOUR_OF_DAY, date);
		int m = TRCalenderUtil.calendarFieldFromDate(Calendar.MINUTE, date);
		int s = TRCalenderUtil.calendarFieldFromDate(Calendar.SECOND, date);
		int ms = TRCalenderUtil.calendarFieldFromDate(Calendar.MILLISECOND, date);
		assertTrue(h == 23 && m == 59 && s == 59 && ms == 999);
	}

}
