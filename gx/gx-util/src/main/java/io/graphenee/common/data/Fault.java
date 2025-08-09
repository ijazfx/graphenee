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
package io.graphenee.common.data;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A fault-tolerant value that can be used to lazily load a value.
 *
 * @param <KEY> The key type.
 * @param <T> The value type.
 */
public class Fault<KEY, T> {

	private static final Logger L = LoggerFactory.getLogger(Fault.class);

	private boolean isFault = true;
	private KEY key;
	private Function<KEY, T> resolver;
	private T value;
	private int lastHashcode;
	private Set<ModificationListener> modificationListeners;
	private boolean isModificationListenersNotified = false;

	/**
	 * Creates a new instance of this fault.
	 */
	Fault() {
	}

	/**
	 * Creates a new instance of this fault from a key and a value.
	 * @param <KEY> The key type.
	 * @param <T> The value type.
	 * @param key The key of the value.
	 * @param t The value.
	 * @return The new fault.
	 */
	public static <KEY, T> Fault<KEY, T> fault(KEY key, T t) {
		return new Fault<>(key, t);
	}

	/**
	 * Creates a new instance of this fault from a key and a resolver.
	 * @param <KEY> The key type.
	 * @param <T> The value type.
	 * @param key The key of the value.
	 * @param resolver The resolver for the value.
	 * @return The new fault.
	 */
	public static <KEY, T> Fault<KEY, T> fault(KEY key, Function<KEY, T> resolver) {
		return new Fault<>(key, resolver);
	}

	/**
	 * Creates a new instance of this fault from a key, a value and modification listeners.
	 * @param <KEY> The key type.
	 * @param <T> The value type.
	 * @param key The key of the value.
	 * @param t The value.
	 * @param modificationListeners The modification listeners.
	 * @return The new fault.
	 */
	public static <KEY, T> Fault<KEY, T> fault(KEY key, T t, ModificationListener... modificationListeners) {
		return new Fault<>(key, t, modificationListeners);
	}

	/**
	 * Creates a new instance of this fault from a key, a resolver and modification listeners.
	 * @param <KEY> The key type.
	 * @param <T> The value type.
	 * @param key The key of the value.
	 * @param resolver The resolver for the value.
	 * @param modificationListeners The modification listeners.
	 * @return The new fault.
	 */
	public static <KEY, T> Fault<KEY, T> fault(KEY key, Function<KEY, T> resolver, ModificationListener... modificationListeners) {
		return new Fault<>(key, resolver, modificationListeners);
	}

	/**
	 * Creates a new null instance of this fault.
	 * @param <KEY> The key type.
	 * @param <T> The value type.
	 * @return The new fault.
	 */
	public static <KEY, T> Fault<KEY, T> nullFault() {
		return new Fault<>(null, null);
	}

	/**
	 * Creates a new instance of this fault from a key and a resolver.
	 * @param key The key of the value.
	 * @param resolver The resolver for the value.
	 */
	public Fault(KEY key, Function<KEY, T> resolver) {
		this.key = key;
		this.resolver = resolver;
	}

	/**
	 * Creates a new instance of this fault from a key and a value.
	 * @param key The key of the value.
	 * @param value The value.
	 */
	public Fault(KEY key, T value) {
		this.key = key;
		this.resolver = (x) -> {
			return value;
		};
	}

	/**
	 * Creates a new instance of this fault from a key, a resolver and modification listeners.
	 * @param key The key of the value.
	 * @param resolver The resolver for the value.
	 * @param modificationListeners The modification listeners.
	 */
	public Fault(KEY key, Function<KEY, T> resolver, ModificationListener... modificationListeners) {
		this.key = key;
		this.resolver = resolver;
		if (modificationListeners != null) {
			for (ModificationListener modificationListener : modificationListeners) {
				addModificationListener(modificationListener);
			}
		}
	}

	/**
	 * Creates a new instance of this fault from a key, a value and modification listeners.
	 * @param key The key of the value.
	 * @param value The value.
	 * @param modificationListeners The modification listeners.
	 */
	public Fault(KEY key, T value, ModificationListener... modificationListeners) {
		this.key = key;
		this.resolver = (x) -> {
			return value;
		};
		if (modificationListeners != null) {
			for (ModificationListener modificationListener : modificationListeners) {
				addModificationListener(modificationListener);
			}
		}
	}

	/**
	 * Gets the invalidated value.
	 * @return The invalidated value.
	 */
	public T getInvalidatedValue() {
		invalidate();
		return getValue();
	}

	/**
	 * Gets the value.
	 * @return The value.
	 */
	public T getValue() {
		isFault = false;
		if (value == null) {
			try {
				value = resolver.apply(key);
				if (value != null) {
					lastHashcode = value.hashCode();
				}
			} catch (Exception ex) {
				L.warn(ex.getMessage(), ex);
				value = null;
			}
		}
		return value;
	}

	/**
	 * Invalidates this fault.
	 */
	public void invalidate() {
		isFault = true;
		isModificationListenersNotified = false;
		value = null;
	}

	/**
	 * Checks if this is a fault.
	 * @return True if this is a fault, false otherwise.
	 */
	public boolean isFault() {
		return isFault;
	}

	/**
	 * Checks if this fault is null.
	 * @return True if this fault is null, false otherwise.
	 */
	public boolean isNull() {
		return key == null && resolver == null;
	}

	/**
	 * Checks if this fault is not null.
	 * @return True if this fault is not null, false otherwise.
	 */
	public boolean isNotNull() {
		return key != null || resolver != null;
	}

	/**
	 * Gets the key.
	 * @return The key.
	 */
	public KEY getKey() {
		return key;
	}

	/**
	 * Checks if this fault has been modified.
	 * @return True if this fault has been modified, false otherwise.
	 */
	public boolean isModified() {
		return lastHashcode != (value != null ? value.hashCode() : 0);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Fault<KEY, T> other = (Fault<KEY, T>) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	private Set<ModificationListener> getModificationListeners() {
		if (modificationListeners == null) {
			synchronized (this) {
				if (modificationListeners == null) {
					modificationListeners = new HashSet<>();
					isModificationListenersNotified = false;
				}
			}
		}
		return modificationListeners;
	}

	/**
	 * Adds a modification listener to this fault.
	 * @param modificationListener The modification listener to add.
	 */
	public void addModificationListener(ModificationListener modificationListener) {
		getModificationListeners().add(modificationListener);
		isModificationListenersNotified = false;
	}

	/**
	 * Removes a modification listener from this fault.
	 * @param modificationListener The modification listener to remove.
	 */
	public void removeModificationListener(ModificationListener modificationListener) {
		getModificationListeners().remove(modificationListener);
		isModificationListenersNotified = false;
	}

	/**
	 * Notifies the modification listeners that this fault has been modified.
	 */
	public void notificationModificationListeners() {
		if (modificationListeners == null || isModificationListenersNotified) {
			return;
		}
		getModificationListeners().forEach(modificationListener -> {
			modificationListener.onModification();
		});
		isModificationListenersNotified = true;
	}

}
