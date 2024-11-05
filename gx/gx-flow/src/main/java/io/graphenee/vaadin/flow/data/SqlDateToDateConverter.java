package io.graphenee.vaadin.flow.data;

import java.sql.Date;
import java.time.LocalDate;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class SqlDateToDateConverter implements Converter<LocalDate, Date> {
	private static final long serialVersionUID = 1L;

	@Override
	public Result<Date> convertToModel(LocalDate value, ValueContext context) {
		return value != null ? Result.ok(Date.valueOf(value)) : Result.ok(null);
	}

	@Override
	public LocalDate convertToPresentation(Date value, ValueContext context) {
		return value != null ? new Date(value.getTime()).toLocalDate() : null;
	}

}
