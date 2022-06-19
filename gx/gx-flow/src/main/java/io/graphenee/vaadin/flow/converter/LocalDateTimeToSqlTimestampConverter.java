package io.graphenee.vaadin.flow.converter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class LocalDateTimeToSqlTimestampConverter implements Converter<LocalDateTime, Timestamp> {

	@Override
	public Result<Timestamp> convertToModel(LocalDateTime value, ValueContext context) {
		if (value != null)
			return Result.ok(Timestamp.valueOf(value));
		return Result.ok(null);

	}

	@Override
	public LocalDateTime convertToPresentation(Timestamp value, ValueContext context) {
		if (value == null)
			return null;
		return value.toLocalDateTime();
	}

}
