package io.graphenee.vaadin.flow.converter;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class TimestampToDateConverter implements Converter<LocalDate, Timestamp> {
	private static final long serialVersionUID = 1L;

	@Override
	public Result<Timestamp> convertToModel(LocalDate value, ValueContext context) {
		return value != null ? Result.ok(new Timestamp(Date.valueOf(value).getTime())) : Result.ok(null);
	}

	@Override
	public LocalDate convertToPresentation(Timestamp value, ValueContext context) {
		return value != null ? new Date(value.getTime()).toLocalDate() : null;
	}

}
