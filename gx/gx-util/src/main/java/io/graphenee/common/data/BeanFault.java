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
 * @deprecated, use Fault instead.
 * @param <ID> The ID type.
 * @param <T> The bean type.
 */
@Deprecated(forRemoval = true)
public class BeanFault<ID, T> {

	private static final Logger L = LoggerFactory.getLogger(BeanFault.class);

	private boolean isFault = true;
	private ID oid;
	private Function<ID, T> resolver;
	private T bean;
	private int lastHashcode;
	private Set<ModificationListener> modificationListeners;
	private boolean isModificationListenersNotified = false;

	/**
	 * Creates a new instance of this fault.
	 */
	BeanFault() {
	}

	/**
	 * Creates a new instance of this fault from an id and a bean.
	 * @param <ID> The ID type.
	 * @param <T> The bean type.
	 * @param id The id of the bean.
	 * @param t The bean.
	 * @return The new fault.
	 */
	public static <ID, T> BeanFault<ID, T> beanFault(ID id, T t) {
		return new BeanFault<>(id, t);
	}

	/**
	 * Creates a new instance of this fault from an id and a resolver.
	 * @param <ID> The ID type.
	 * @param <T> The bean type.
	 * @param id The id of the bean.
	 * @param resolver The resolver for the bean.
	 * @return The new fault.
	 */
	public static <ID, T> BeanFault<ID, T> beanFault(ID id, Function<ID, T> resolver) {
		return new BeanFault<>(id, resolver);
	}

	/**
	 * Creates a new instance of this fault from an id, a bean and modification listeners.
	 * @param <ID> The ID type.
	 * @param <T> The bean type.
	 * @param id The id of the bean.
	 * @param t The bean.
	 * @param modificationListeners The modification listeners.
	 * @return The new fault.
	 */
	public static <ID, T> BeanFault<ID, T> beanFault(ID id, T t, ModificationListener... modificationListeners) {
		return new BeanFault<>(id, t, modificationListeners);
	}

	/**
	 * Creates a new instance of this fault from an id, a resolver and modification listeners.
	 * @param <ID> The ID type.
	 * @param <T> The bean type.
	 * @param id The id of the bean.
	 * @param resolver The resolver for the bean.
	 * @param modificationListeners The modification listeners.
	 * @return The new fault.
	 */
	public static <ID, T> BeanFault<ID, T> beanFault(ID id, Function<ID, T> resolver, ModificationListener... modificationListeners) {
		return new BeanFault<>(id, resolver, modificationListeners);
	}

	/**
	 * Creates a new null instance of this fault.
	 * @param <ID> The ID type.
	 * @param <T> The bean type.
	 * @return The new fault.
	 */
	public static <ID, T> BeanFault<ID, T> nullFault() {
		return new BeanFault<>(null, null);
	}

	/**
	 * Creates a new instance of this fault from an id and a resolver.
	 * @param oid The id of the bean.
	 * @param resolver The resolver for the bean.
	 */
	public BeanFault(ID oid, Function<ID, T> resolver) {
		this.oid = oid;
		this.resolver = resolver;
	}

	/**
	 * Creates a new instance of this fault from an id and a bean.
	 * @param oid The id of the bean.
	 * @param bean The bean.
	 */
	public BeanFault(ID oid, T bean) {
		this.oid = oid;
		this.resolver = (x) -> {
			return bean;
		};
	}

	/**
	 * Creates a new instance of this fault from an id, a resolver and modification listeners.
	 * @param oid The id of the bean.
	 * @param resolver The resolver for the bean.
	 * @param modificationListeners The modification listeners.
	 */
	public BeanFault(ID oid, Function<ID, T> resolver, ModificationListener... modificationListeners) {
		this.oid = oid;
		this.resolver = resolver;
		if (modificationListeners != null) {
			for (ModificationListener modificationListener : modificationListeners) {
				addModificationListener(modificationListener);
			}
		}
	}

	/**
	 * Creates a new instance of this fault from an id, a bean and modification listeners.
	 * @param oid The id of the bean.
	 * @param bean The bean.
	 * @param modificationListeners The modification listeners.
	 */
	public BeanFault(ID oid, T bean, ModificationListener... modificationListeners) {
		this.oid = oid;
		this.resolver = (x) -> {
			return bean;
		};
		if (modificationListeners != null) {
			for (ModificationListener modificationListener : modificationListeners) {
				addModificationListener(modificationListener);
			}
		}
	}

	/**
	 * Gets the bean.
	 * @return The bean.
	 */
	public T getBean() {
		isFault = false;
		if (bean == null) {
			try {
				bean = resolver.apply(oid);
				if (bean != null) {
					lastHashcode = bean.hashCode();
				}
			} catch (Exception ex) {
				L.warn(ex.getMessage(), ex);
				bean = null;
			}
		}
		return bean;
	}

	/**
	 * Invalidates this fault.
	 */
	public void invalidate() {
		isFault = true;
		isModificationListenersNotified = false;
		bean = null;
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
		return oid == null && resolver == null;
	}

	/**
	 * Checks if this fault is not null.
	 * @return True if this fault is not null, false otherwise.
	 */
	public boolean isNotNull() {
		return oid != null || resolver != null;
	}

	/**
	 * Gets the id of the bean.
	 * @return The id of the bean.
	 */
	public ID getOid() {
		return oid;
	}

	/**
	 * Checks if this fault has been modified.
	 * @return True if this fault has been modified, false otherwise.
	 */
	public boolean isModified() {
		return lastHashcode != (bean != null ? bean.hashCode() : 0);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((oid == null) ? 0 : oid.hashCode());
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
		BeanFault<ID, T> other = (BeanFault<ID, T>) obj;
		if (oid == null) {
			if (other.oid != null)
				return false;
		} else if (!oid.equals(other.oid))
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
