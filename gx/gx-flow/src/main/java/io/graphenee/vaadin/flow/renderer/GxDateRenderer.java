package io.graphenee.vaadin.flow.renderer;

import java.sql.Date;

import com.vaadin.flow.data.renderer.BasicRenderer;
import com.vaadin.flow.function.ValueProvider;

import io.graphenee.core.util.TRCalendarUtil;


public class GxDateRenderer<T> extends BasicRenderer<T, Date> {

    private static final long serialVersionUID = 1L;
    private String datePattern;

    public GxDateRenderer(ValueProvider<T, Date> valueProvider, String datePattern) {
        super(valueProvider);
        this.datePattern = datePattern;   
    }

    public GxDateRenderer(ValueProvider<T, Date> valueProvider) {
        super(valueProvider);
    }

    @Override
    protected String getFormattedValue(Date value) {
        if (value != null) {
            if(datePattern != null) {
                return TRCalendarUtil.getFormattedDate(value, datePattern);
            }
            return TRCalendarUtil.getFormattedDate(value);
        }
        return null;
    }

}
