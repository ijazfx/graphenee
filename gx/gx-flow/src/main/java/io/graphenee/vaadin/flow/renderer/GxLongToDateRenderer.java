package io.graphenee.vaadin.flow.renderer;

import java.sql.Date;

import com.vaadin.flow.data.renderer.BasicRenderer;
import com.vaadin.flow.function.ValueProvider;

import io.graphenee.core.util.TRCalendarUtil;

public class GxLongToDateRenderer<T> extends BasicRenderer<T, Long> {

    private static final long serialVersionUID = 1L;
    private String datePattern;

    public GxLongToDateRenderer(ValueProvider<T, Long> valueProvider, String datePattern) {
        super(valueProvider);
        this.datePattern = datePattern;   
    }

    public GxLongToDateRenderer(ValueProvider<T, Long> valueProvider) {
        super(valueProvider);
    }

    @Override
    protected String getFormattedValue(Long value) {
        if (value != null) {
            if(datePattern != null) {
                return TRCalendarUtil.getFormattedDate(new Date(value), datePattern);
            }
            return TRCalendarUtil.getFormattedDate(new Date(value));
        }
        return null;
    }

}
