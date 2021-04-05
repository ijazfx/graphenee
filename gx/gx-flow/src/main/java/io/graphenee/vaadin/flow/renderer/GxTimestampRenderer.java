package io.graphenee.vaadin.flow.renderer;

import java.sql.Timestamp;

import com.vaadin.flow.data.renderer.BasicRenderer;
import com.vaadin.flow.function.ValueProvider;

import io.graphenee.core.util.TRCalendarUtil;

public class GxTimestampRenderer<T> extends BasicRenderer<T, Timestamp> {

    private static final long serialVersionUID = 1L;
    private String dateTimePattern;

    public GxTimestampRenderer(ValueProvider<T, Timestamp> valueProvider, String dateTimePattern) {
        super(valueProvider);
        this.dateTimePattern = dateTimePattern;   
    }

    public GxTimestampRenderer(ValueProvider<T, Timestamp> valueProvider) {
        super(valueProvider);
    }

    @Override
    protected String getFormattedValue(Timestamp value) {
        if (value != null) {
            if(dateTimePattern != null) {
                return TRCalendarUtil.getFormattedDateTime(value, dateTimePattern);
            }
            return TRCalendarUtil.getFormattedDateTime(value);
        }
        return null;
    }

}
