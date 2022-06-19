package io.graphenee.vaadin.flow.converter;

import java.sql.Date;
import java.time.LocalDate;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class LongToDateConverter implements Converter<LocalDate, Long> {

	private static final long serialVersionUID = 1L;

	@Override
	public Result<Long> convertToModel(LocalDate value, ValueContext context) {
		if (value != null)
			return Result.ok(Date.valueOf(value).getTime());
		return Result.ok(0L);

	}

	@Override
	public LocalDate convertToPresentation(Long value, ValueContext context) {
		if (value == null || value == 0)
			return null;
		Date date = new Date(value);
		return date.toLocalDate();
	}

}
