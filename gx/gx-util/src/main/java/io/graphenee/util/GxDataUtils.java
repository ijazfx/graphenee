package io.graphenee.util;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A utility class for data.
 */
public class GxDataUtils {

	/**
	 * Creates a new instance of this utility class.
	 */
	public GxDataUtils() {
		// a default constructor
	}

	private static final Logger L = LoggerFactory.getLogger(GxDataUtils.class);

	/**
	 * Copies the values from a source object to a destination object.
	 * @param <S> The source type.
	 * @param <D> The destination type.
	 * @param sourceClass The source class.
	 * @param destClass The destination class.
	 * @param source The source object.
	 * @param dest The destination object.
	 */
	public static <S, D> void copyValues(Class<S> sourceClass, Class<D> destClass, S source, D dest) {
		try {
			BeanUtils.copyProperties(dest, source);
		} catch (IllegalAccessException | InvocationTargetException e) {
			L.warn(e.getMessage());
		}

		// TODO:Copy faults and collection faults.
	}

}
