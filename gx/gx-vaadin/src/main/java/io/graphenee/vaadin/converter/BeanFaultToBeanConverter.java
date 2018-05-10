package io.graphenee.vaadin.converter;

import java.util.Locale;

import com.graphenee.core.model.BeanFault;
import com.graphenee.core.util.KeyValueWrapper;
import com.vaadin.data.util.converter.Converter;

public class BeanFaultToBeanConverter<ID, T> implements Converter<T, BeanFault<ID, T>> {

	private static final long serialVersionUID = 1L;
	private String idProperty;
	private Class<T> typeOfT;

	public BeanFaultToBeanConverter(Class<T> typeOfT) {
		this(typeOfT, "oid");
	}

	public BeanFaultToBeanConverter(Class<T> typeOfT, String idProperty) {
		this.typeOfT = typeOfT;
		this.idProperty = idProperty;
	}

	@Override
	public BeanFault<ID, T> convertToModel(T value, Class<? extends BeanFault<ID, T>> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value == null)
			return null;
		ID id = (ID) new KeyValueWrapper(value).valueForKeyPath(idProperty);
		BeanFault<ID, T> beanFault = BeanFault.beanFault(id, value);
		return beanFault;
	}

	@Override
	public T convertToPresentation(BeanFault<ID, T> value, Class<? extends T> targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value == null)
			return null;
		return value.getBean();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<BeanFault<ID, T>> getModelType() {
		return (Class<BeanFault<ID, T>>) BeanFault.nullFault().getClass();
	}

	@Override
	public Class<T> getPresentationType() {
		return typeOfT;
	}

}