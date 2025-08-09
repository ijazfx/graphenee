package io.graphenee.vaadin.flow.data;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import io.graphenee.common.data.Fault;
import io.graphenee.util.KeyValueWrapper;

/**
 * A converter that converts a {@link Fault} to a value.
 *
 * @param <ID> The ID type.
 * @param <T> The value type.
 */
public class FaultToValueConverter<ID, T> implements Converter<T, Fault<ID, T>> {
	private static final long serialVersionUID = 1L;

	private String idProperty;
	private Class<T> typeOfT;

	/**
	 * Creates a new instance of this converter.
	 * @param typeOfT The type of the value.
	 */
	public FaultToValueConverter(Class<T> typeOfT) {
		this(typeOfT, "oid");
	}

	/**
	 * Creates a new instance of this converter.
	 * @param typeOfT The type of the value.
	 * @param idProperty The name of the ID property.
	 */
	public FaultToValueConverter(Class<T> typeOfT, String idProperty) {
		this.typeOfT = typeOfT;
		this.idProperty = idProperty;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result<Fault<ID, T>> convertToModel(T value, ValueContext context) {
		if (value == null)
			return null;
		ID id = (ID) new KeyValueWrapper(value).valueForKeyPath(idProperty);
		Fault<ID, T> fault = Fault.fault(id, value);
		return Result.ok(fault);
	}

	@Override
	public T convertToPresentation(Fault<ID, T> value, ValueContext arg1) {
		if (value == null)
			return null;
		return value.getValue();
	}

	/**
	 * Gets the presentation type.
	 * @return The presentation type.
	 */
	public Class<T> getPresentationType() {
		return typeOfT;
	}

	/**
	 * Gets the model type.
	 * @return The model type.
	 */
	@SuppressWarnings("unchecked")
	public Class<Fault<ID, T>> getModelType() {
		return (Class<Fault<ID, T>>) Fault.nullFault().getClass();
	}

}
