package io.graphenee.vaadin.flow.data;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class DoubleToFloatConverter implements Converter<Double, Float> {
	private static final long serialVersionUID = 1L;

	@Override
	public Result<Float> convertToModel(Double value, ValueContext context) {
		return value != null ? Result.ok(value.floatValue()) : null;
	}

	@Override
	public Double convertToPresentation(Float value, ValueContext context) {
		return value != null ? Double.valueOf(value) : null;
	}

}
