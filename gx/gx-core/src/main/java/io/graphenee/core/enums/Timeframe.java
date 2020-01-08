package io.graphenee.core.enums;

import java.sql.Timestamp;

import io.graphenee.core.util.TRCalendarUtil;

public enum Timeframe {
	Today("Today"),
	ThisWeek("This Week"),
	ThisMonth("This Month"),
	ThisYear("This Year"),
	Yesterday("Yesterday"),
	LastWeek("Last Week"),
	LastMonth("Last Month"),
	LastYear("Last Year"),
	Tomorrow("Tomorrow"),
	NextWeek("Next Week"),
	NextMonth("Next Month"),
	NextYear("Next Year"),
	Past("Past");

	private String description;

	Timeframe(String description) {
		this.description = description;
	}

	public Timestamp[] getDateRange() {
		Timestamp[] range = new Timestamp[2];
		switch (this) {
		case Today:
			range[0] = new Timestamp(TRCalendarUtil.startOfDay().getTime());
			range[1] = new Timestamp(TRCalendarUtil.endOfDay().getTime());
		break;
		case ThisWeek:
			range[0] = new Timestamp(TRCalendarUtil.startOfWeek().getTime());
			range[1] = new Timestamp(TRCalendarUtil.endOfWeek().getTime());
		break;
		case ThisMonth:
			range[0] = new Timestamp(TRCalendarUtil.startOfMonth().getTime());
			range[1] = new Timestamp(TRCalendarUtil.endOfMonth().getTime());
		break;
		case ThisYear:
			range[0] = new Timestamp(TRCalendarUtil.startOfYear().getTime());
			range[1] = new Timestamp(TRCalendarUtil.endOfYear().getTime());
		break;

		case Yesterday:
			range[0] = new Timestamp(TRCalendarUtil.startOfYesterday().getTime());
			range[1] = new Timestamp(TRCalendarUtil.endOfYesterday().getTime());
		break;
		case LastWeek:
			range[0] = new Timestamp(TRCalendarUtil.startOfLastWeek().getTime());
			range[1] = new Timestamp(TRCalendarUtil.endOfLastWeek().getTime());
		break;
		case LastMonth:
			range[0] = new Timestamp(TRCalendarUtil.startOfLastMonth().getTime());
			range[1] = new Timestamp(TRCalendarUtil.endOfLastMonth().getTime());
		break;
		case LastYear:
			range[0] = new Timestamp(TRCalendarUtil.startOfLastYear().getTime());
			range[1] = new Timestamp(TRCalendarUtil.endOfLastYear().getTime());
		break;
		case Tomorrow:
			range[0] = new Timestamp(TRCalendarUtil.startOfTomorrow().getTime());
			range[1] = new Timestamp(TRCalendarUtil.endOfTomorrow().getTime());
		break;
		case NextWeek:
			range[0] = new Timestamp(TRCalendarUtil.startOfNextWeek().getTime());
			range[1] = new Timestamp(TRCalendarUtil.endOfNextWeek().getTime());
		break;
		case NextMonth:
			range[0] = new Timestamp(TRCalendarUtil.startOfNextMonth().getTime());
			range[1] = new Timestamp(TRCalendarUtil.endOfNextMonth().getTime());
		break;
		case NextYear:
			range[0] = new Timestamp(TRCalendarUtil.startOfNextYear().getTime());
			range[1] = new Timestamp(TRCalendarUtil.endOfNextYear().getTime());
		break;
		case Past:
			range[0] = new Timestamp(TRCalendarUtil.startOfLastYear().getTime());
			range[1] = new Timestamp(TRCalendarUtil.endOfLastMonth().getTime());
		break;
		default:
			range[0] = new Timestamp(TRCalendarUtil.startOfDay().getTime());
			range[1] = new Timestamp(TRCalendarUtil.endOfDay().getTime());
		}
		return range;
	}

	public String getDescription() {
		return description;
	}

	public Timestamp getFromDate() {
		return getDateRange()[0];
	}

	public Timestamp getToDate() {
		return getDateRange()[1];
	}

	@Override
	public String toString() {
		return getDescription();
	}
}
