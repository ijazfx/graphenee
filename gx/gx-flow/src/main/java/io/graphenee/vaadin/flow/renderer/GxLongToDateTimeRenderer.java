package io.graphenee.vaadin.flow.renderer;

import java.sql.Timestamp;

import com.vaadin.flow.data.renderer.BasicRenderer;
import com.vaadin.flow.function.ValueProvider;

import io.graphenee.core.util.TRCalendarUtil;

public class GxLongToDateTimeRenderer<T> extends BasicRenderer<T, Long> {

    private static final long serialVersionUID = 1L;
    private String dateTimePattern;

    public GxLongToDateTimeRenderer(ValueProvider<T, Long> valueProvider, String dateTimePattern) {
        super(valueProvider);
        this.dateTimePattern = dateTimePattern;   
    }

    public GxLongToDateTimeRenderer(ValueProvider<T, Long> valueProvider) {
        super(valueProvider);
    }

    @Override
    protected String getFormattedValue(Long value) {
        if (value != null) {
            if(dateTimePattern != null) {
                return TRCalendarUtil.getFormattedDateTime(new Timestamp(value), dateTimePattern);
            }
            return TRCalendarUtil.getFormattedDateTime(new Timestamp(value));
        }
        return null;
    }

}
