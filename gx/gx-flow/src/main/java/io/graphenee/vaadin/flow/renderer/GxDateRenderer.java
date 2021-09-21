package io.graphenee.vaadin.flow.renderer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.vaadin.flow.data.renderer.BasicRenderer;
import com.vaadin.flow.function.ValueProvider;

import io.graphenee.core.util.TRCalendarUtil;

public class GxDateRenderer<T> extends BasicRenderer<T, Date> {

    private static final long serialVersionUID = 1L;

<<<<<<< HEAD
    public static enum GxDateResultion {
=======
    public static enum GxDateResolution {
>>>>>>> c28c6acb5a426e637e7132eec1646a0639a30851
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

<<<<<<< HEAD
    public GxDateRenderer(ValueProvider<T, Date> valueProvider, GxDateResultion resolution) {
=======
    public GxDateRenderer(ValueProvider<T, Date> valueProvider, GxDateResolution resolution) {
>>>>>>> c28c6acb5a426e637e7132eec1646a0639a30851
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
