package io.graphenee.vaadin.flow.data;

import java.text.DateFormat;
import java.util.Date;

import com.vaadin.flow.data.renderer.BasicRenderer;
import com.vaadin.flow.function.ValueProvider;

import io.graphenee.util.TRCalendarUtil;

public class GxNumberToDateRenderer<T> extends BasicRenderer<T, Number> {

	private static final long serialVersionUID = 1L;

	public static enum GxDateResolution {
		Date,
		Time,
		DateTime;
	}

	private String datePattern;
	private DateFormat dateFormat;

	public GxNumberToDateRenderer(ValueProvider<T, Number> valueProvider, String datePattern) {
		super(valueProvider);
		this.datePattern = datePattern;
	}

	public GxNumberToDateRenderer(ValueProvider<T, Number> valueProvider, GxDateResolution resolution) {
		super(valueProvider);
		switch (resolution) {
		case Date:
			dateFormat = TRCalendarUtil.getCustomDateFormatter();
		break;
		case Time:
			dateFormat = TRCalendarUtil.getCustomTimeFormatter();
		break;
		case DateTime:
			dateFormat = TRCalendarUtil.getCustomDateTimeFormatter();
		break;
		}
	}

	@Override
	protected String getFormattedValue(Number value) {
		if (value != null) {
			if (datePattern != null) {
				return TRCalendarUtil.getFormattedDate(new Date(value.longValue()), datePattern);
			}
			return dateFormat.format(new Date(value.longValue()));
		}
		return null;
	}

}