package io.graphenee.vaadin.flow.renderer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.vaadin.flow.data.renderer.BasicRenderer;
import com.vaadin.flow.function.ValueProvider;

import io.graphenee.core.util.TRCalendarUtil;

public class GxDateRenderer<T> extends BasicRenderer<T, Date> {

    private static final long serialVersionUID = 1L;

    public static enum GxDateResolution {
        Date,
        Time,
        DateTime;
    }

    private String datePattern;
    private DateFormat dateFormat;

    public GxDateRenderer(ValueProvider<T, Date> valueProvider, String datePattern) {
        super(valueProvider);
        this.datePattern = datePattern;
    }

    public GxDateRenderer(ValueProvider<T, Date> valueProvider, GxDateResolution resolution) {
        super(valueProvider);
        switch (resolution) {
        case Date:
            dateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT);
        break;
        case Time:
            dateFormat = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT);
        break;
        case DateTime:
            dateFormat = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT);
        break;
        }
    }

    @Override
    protected String getFormattedValue(Date value) {
        if (value != null) {
            if (datePattern != null) {
                return TRCalendarUtil.getFormattedDate(value, datePattern);
            }
            return dateFormat.format(value);
        }
        return null;
    }

}
