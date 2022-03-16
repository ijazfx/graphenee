package io.graphenee.vaadin.flow.converter;

import java.sql.Time;
import java.time.LocalTime;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class LocalTimeToSqlTimeConverter implements Converter<LocalTime, Time> {

    @Override
    public Result<Time> convertToModel(LocalTime value, ValueContext context) {
        return value != null ? Result.ok(Time.valueOf(value)) : null;
    }

    @Override
    public LocalTime convertToPresentation(Time value, ValueContext context) {
        return value != null ? value.toLocalTime() : null;
    }

}
