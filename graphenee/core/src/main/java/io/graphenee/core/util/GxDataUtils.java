package io.graphenee.core.util;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GxDataUtils {

	private static final Logger L = LoggerFactory.getLogger(GxDataUtils.class);

	public static <S, D> void copyValues(Class<S> sourceClass, Class<D> destClass, S source, D dest) {
		try {
			BeanUtils.copyProperties(dest, source);
		} catch (IllegalAccessException | InvocationTargetException e) {
			L.warn(e.getMessage());
		}

		// TODO:Copy faults and collection faults.
	}

}
