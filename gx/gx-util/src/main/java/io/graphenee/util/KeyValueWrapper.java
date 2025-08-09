/*******************************************************************************
 * Copyright (c) 2016, 2018 Farrukh Ijaz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package io.graphenee.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A wrapper class for accessing values from a key-value object.
 */
public class KeyValueWrapper {

	static final Logger L = LoggerFactory.getLogger(KeyValueWrapper.class);
	static volatile DateFormat _iso8601DateFormat;

	/**
	 * Gets the ISO 8601 date format.
	 * @return The date format.
	 */
	public static DateFormat iso8601DateFormat() {

		if (_iso8601DateFormat == null) {
			synchronized (KeyValueWrapper.class) {
				if (_iso8601DateFormat == null) {
					_iso8601DateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
					_iso8601DateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
				}
			}
		}
		return _iso8601DateFormat;
	}

	/**
	 * Gets a date from an ISO 8601 date string.
	 * @param dateString The date string.
	 * @return The date.
	 */
	public static Date getDateFromISO8601DateString(String dateString) {
		try {
			return iso8601DateFormat().parse(dateString);
		} catch (ParseException e) {
			return null;
		}
	}

	Object wrappedObject;

	/**
	 * Creates a new instance of this wrapper.
	 * @param wrappedObject The object to wrap.
	 */
	public KeyValueWrapper(Object wrappedObject) {
		this.wrappedObject = wrappedObject;
	}

	/**
	 * Gets a value for a key path.
	 * @param keyPath The key path.
	 * @return The value.
	 */
	public Object valueForKeyPath(String keyPath) {
		String[] parts = keyPath.split("\\.");
		Object currentObject = wrappedObject;
		for (String part : parts) {
			if (currentObject instanceof List) {
				Integer index = Integer.parseInt(part);
				currentObject = ((List<?>) currentObject).get(index);
			} else if (currentObject instanceof JSONArray) {
				try {
					Integer index = Integer.parseInt(part);
					currentObject = ((JSONArray) currentObject).get(index);
				} catch (JSONException je) {
					return null;
				}
			} else if (currentObject instanceof JSONObject) {
				try {
					currentObject = ((JSONObject) currentObject).get(part);
				} catch (JSONException je) {
					return null;
				}
			} else {
				if (currentObject == null)
					return null;
				String methodName = "get" + part.toUpperCase().charAt(0) + part.substring(1);
				try {
					Method method = currentObject.getClass().getMethod(methodName, new Class[] {});
					currentObject = method.invoke(currentObject, new Object[] {});
				} catch (NoSuchMethodException e) {
					methodName = "is" + part.toUpperCase().charAt(0) + part.substring(1);
					try {
						Method method = currentObject.getClass().getMethod(methodName, new Class[] {});
						currentObject = method.invoke(currentObject, new Object[] {});
					} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
						L.warn(e.getMessage(), e);
						return null;
					}
				} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					L.warn(e.getMessage(), e);
					return null;
				}
			}
		}
		return currentObject;
	}

	/**
	 * Gets an array for a key path.
	 * @param <T> The type of the array.
	 * @param keyPath The key path.
	 * @return The array.
	 */
	@SuppressWarnings("unchecked")
	public <T> Collection<T> arrayForKeyPath(String keyPath) {
		Object value = valueForKeyPath(keyPath);
		try {
			List<T> list = new ArrayList<>();
			if (value != null && value.getClass().isArray()) {
				T[] objects = (T[]) value;
				for (int i = 0; i < objects.length; i++) {
					list.add(objects[i]);
				}
			} else if (value instanceof Collection) {
				((Collection<T>) value).forEach(item -> {
					list.add(item);
				});
			} else if (value instanceof JSONArray) {
				JSONArray array = (JSONArray) value;
				for (int i = 0; i < array.length(); i++) {
					list.add((T) array.get(i));
				}
			}
			return list;
		} catch (Exception e) {
			L.warn(e.getMessage(), e);
			return Collections.emptyList();
		}
	}

	/**
	 * Gets a key-value wrapper array for a key path.
	 * @param keyPath The key path.
	 * @return The array.
	 */
	public Collection<KeyValueWrapper> kvwArrayForKeyPath(String keyPath) {
		Object value = valueForKeyPath(keyPath);
		try {
			List<KeyValueWrapper> list = new ArrayList<>();
			if (value != null && value.getClass().isArray()) {
				Object[] objects = (Object[]) value;
				for (int i = 0; i < objects.length; i++) {
					list.add(new KeyValueWrapper(objects[i]));
				}
			} else if (value instanceof Collection) {
				((Collection<?>) value).forEach(item -> {
					list.add(new KeyValueWrapper(item));
				});
			} else if (value instanceof JSONArray) {
				JSONArray array = (JSONArray) value;
				for (int i = 0; i < array.length(); i++) {
					list.add(new KeyValueWrapper(array.get(i)));
				}
			}
			return list;
		} catch (Exception e) {
			L.warn(e.getMessage(), e);
			return Collections.emptyList();
		}
	}

	/**
	 * Gets a string for a key path.
	 * @param keyPath The key path.
	 * @return The string.
	 */
	public String stringForKeyPath(String keyPath) {
		Object value = valueForKeyPath(keyPath);
		try {
			return (String) value;
		} catch (Exception e) {
			L.warn(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Gets a boolean for a key path.
	 * @param keyPath The key path.
	 * @return The boolean.
	 */
	public Boolean booleanForKeyPath(String keyPath) {
		Object value = valueForKeyPath(keyPath);
		try {
			if (value instanceof Boolean) {
				return (Boolean) value;
			}
			return Boolean.parseBoolean((String) value);
		} catch (Exception e) {
			L.warn(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Gets an integer for a key path.
	 * @param keyPath The key path.
	 * @return The integer.
	 */
	public Integer intForKeyPath(String keyPath) {
		Object value = valueForKeyPath(keyPath);
		try {
			if (value instanceof Number) {
				return ((Number) value).intValue();
			}
			return Integer.parseInt((String) value);
		} catch (Exception e) {
			L.warn(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Gets a short for a key path.
	 * @param keyPath The key path.
	 * @return The short.
	 */
	public Short shortForKeyPath(String keyPath) {
		Object value = valueForKeyPath(keyPath);
		try {
			if (value instanceof Number) {
				return ((Number) value).shortValue();
			}
			return Short.parseShort((String) value);
		} catch (Exception e) {
			L.warn(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Gets a long for a key path.
	 * @param keyPath The key path.
	 * @return The long.
	 */
	public Long longForKeyPath(String keyPath) {
		Object value = valueForKeyPath(keyPath);
		try {
			if (value instanceof Number) {
				return ((Number) value).longValue();
			}
			return Long.parseLong((String) value);
		} catch (Exception e) {
			L.warn(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Gets an ISO 8601 date for a key path.
	 * @param keyPath The key path.
	 * @return The date.
	 */
	public Date iso8601DateForKeyPath(String keyPath) {
		Object value = valueForKeyPath(keyPath);
		try {
			if (value instanceof Date) {
				return (Date) value;
			}
			return iso8601DateFormat().parse((String) value);
		} catch (Exception e) {
			L.warn(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Gets a date for a key path.
	 * @param keyPath The key path.
	 * @param datePattern The date pattern.
	 * @return The date.
	 */
	public Date dateForKeyPath(String keyPath, String datePattern) {
		Object value = valueForKeyPath(keyPath);
		try {
			if (value instanceof Date) {
				return (Date) value;
			}
			return new SimpleDateFormat(datePattern).parse((String) value);
		} catch (Exception e) {
			L.warn(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Gets a float for a key path.
	 * @param keyPath The key path.
	 * @return The float.
	 */
	public Float floatForKeyPath(String keyPath) {
		Object value = valueForKeyPath(keyPath);
		try {
			if (value instanceof Float) {
				return ((Number) value).floatValue();
			}
			return Float.parseFloat((String) value);
		} catch (Exception e) {
			L.warn(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Gets a double for a key path.
	 * @param keyPath The key path.
	 * @return The double.
	 */
	public Double doubleForKeyPath(String keyPath) {
		Object value = valueForKeyPath(keyPath);
		try {
			if (value instanceof Number) {
				return ((Number) value).doubleValue();
			}
			return Double.parseDouble((String) value);
		} catch (Exception e) {
			L.warn(e.getMessage(), e);
			return null;
		}
	}

}
