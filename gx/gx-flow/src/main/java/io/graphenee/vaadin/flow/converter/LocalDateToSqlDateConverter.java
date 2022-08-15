package io.graphenee.vaadin.flow.converter;

import java.sql.Date;
import java.time.LocalDate;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class LocalDateToSqlDateConverter implements Converter<LocalDate, Date> {

	@Override
	public Result<Date> convertToModel(LocalDate value, ValueContext context) {
		if (value != null)
			return Result.ok(Date.valueOf(value));
		return Result.ok(null);

	}

	@Override
	public LocalDate convertToPresentation(Date value, ValueContext context) {
		if (value == null)
			return null;
		return value.toLocalDate();
	}

}
