package io.graphenee.vaadin.flow.renderer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.vaadin.flow.data.renderer.BasicRenderer;
import com.vaadin.flow.function.ValueProvider;

import io.graphenee.core.util.TRCalendarUtil;

public class GxNumberToDateRenderer<T> extends BasicRenderer<T, Number> {

    private static final long serialVersionUID = 1L;

    public static enum GxDateResultion {
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

    public GxNumberToDateRenderer(ValueProvider<T, Number> valueProvider, GxDateResultion resolution) {
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
