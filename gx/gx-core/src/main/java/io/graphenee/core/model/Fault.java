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
package io.graphenee.core.model;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Fault<KEY, T> {

	private static final Logger L = LoggerFactory.getLogger(Fault.class);

	private boolean isFault = true;
	private KEY key;
	private Function<KEY, T> resolver;
	private T value;
	private int lastHashcode;
	private Set<ModificationListener> modificationListeners;
	private boolean isModificationListenersNotified = false;

	Fault() {
	}

	public static <KEY, T> Fault<KEY, T> fault(KEY key, T t) {
		return new Fault<>(key, t);
	}

	public static <KEY, T> Fault<KEY, T> fault(KEY key, Function<KEY, T> resolver) {
		return new Fault<>(key, resolver);
	}

	public static <KEY, T> Fault<KEY, T> fault(KEY key, T t, ModificationListener... modificationListeners) {
		return new Fault<>(key, t, modificationListeners);
	}

	public static <KEY, T> Fault<KEY, T> fault(KEY key, Function<KEY, T> resolver, ModificationListener... modificationListeners) {
		return new Fault<>(key, resolver, modificationListeners);
	}

	public static <KEY, T> Fault<KEY, T> nullFault() {
		return new Fault<>(null, null);
	}

	public Fault(KEY key, Function<KEY, T> resolver) {
		this.key = key;
		this.resolver = resolver;
	}

	public Fault(KEY key, T value) {
		this.key = key;
		this.resolver = (x) -> {
			return value;
		};
	}

	public Fault(KEY key, Function<KEY, T> resolver, ModificationListener... modificationListeners) {
		this.key = key;
		this.resolver = resolver;
		if (modificationListeners != null) {
			for (ModificationListener modificationListener : modificationListeners) {
				addModificationListener(modificationListener);
			}
		}
	}

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

	public T getInvalidatedValue() {
		invalidate();
		return getValue();
	}

	public T getValue() {
		isFault = false;
		if (value == null) {
			try {
				value = resolver.apply(key);
				if (value != null) {
					lastHashcode = value.hashCode();
				}
			} catch (Exception ex) {
				L.warn(ex.getMessage());
				value = null;
			}
		}
		return value;
	}

	public void invalidate() {
		isFault = true;
		isModificationListenersNotified = false;
		value = null;
	}

	public boolean isFault() {
		return isFault;
	}

	public boolean isNull() {
		return key == null && resolver == null;
	}

	public boolean isNotNull() {
		return key != null || resolver != null;
	}

	public KEY getKey() {
		return key;
	}

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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Fault other = (Fault) obj;
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

	public void addModificationListener(ModificationListener modificationListener) {
		getModificationListeners().add(modificationListener);
		isModificationListenersNotified = false;
	}

	public void removeModificationListener(ModificationListener modificationListener) {
		getModificationListeners().remove(modificationListener);
		isModificationListenersNotified = false;
	}

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
