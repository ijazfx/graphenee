package io.graphenee.vaadin.flow.converter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class TimestampToDateTimeConverter implements Converter<LocalDateTime, Timestamp> {
	private static final long serialVersionUID = 1L;

	@Override
	public Result<Timestamp> convertToModel(LocalDateTime value, ValueContext context) {
		return value != null ? Result.ok(Timestamp.valueOf(value)) : null;
	}

	@Override
	public LocalDateTime convertToPresentation(Timestamp value, ValueContext context) {
		return value != null ? value.toLocalDateTime() : null;
	}

}
