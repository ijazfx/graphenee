package io.graphenee.vaadin.flow.converter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import io.graphenee.core.model.BeanCollectionFault;

public class BeanCollectionFaultToSetConverter<T> implements Converter<Set<T>, BeanCollectionFault<T>> {

    private static final long serialVersionUID = 1L;

    @Override
    public Result<BeanCollectionFault<T>> convertToModel(Set<T> value, ValueContext context) {
        BeanCollectionFault<T> from = BeanCollectionFault.from(value);
        from.markAsModified();
        return Result.ok(from);
    }

    @Override
    public Set<T> convertToPresentation(BeanCollectionFault<T> value, ValueContext context) {
        if (value == null) {
            return Collections.emptySet();
        }
        return new HashSet<>(value.getBeans());
    }

}