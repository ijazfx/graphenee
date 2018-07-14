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

public class BeanFault<ID, T> {

	private static final Logger L = LoggerFactory.getLogger(BeanFault.class);

	private boolean isFault = true;
	private ID oid;
	private Function<ID, T> resolver;
	private T bean;
	private int lastHashcode;
	private Set<ModificationListener> modificationListeners;
	private boolean isModificationListenersNotified = false;

	BeanFault() {
	}

	public static <ID, T> BeanFault<ID, T> beanFault(ID id, T t) {
		return new BeanFault<>(id, t);
	}

	public static <ID, T> BeanFault<ID, T> beanFault(ID id, Function<ID, T> resolver) {
		return new BeanFault<>(id, resolver);
	}

	public static <ID, T> BeanFault<ID, T> beanFault(ID id, T t, ModificationListener... modificationListeners) {
		return new BeanFault<>(id, t, modificationListeners);
	}

	public static <ID, T> BeanFault<ID, T> beanFault(ID id, Function<ID, T> resolver, ModificationListener... modificationListeners) {
		return new BeanFault<>(id, resolver, modificationListeners);
	}

	public static <ID, T> BeanFault<ID, T> nullFault() {
		return new BeanFault<>(null, null);
	}

	public BeanFault(ID oid, Function<ID, T> resolver) {
		this.oid = oid;
		this.resolver = resolver;
	}

	public BeanFault(ID oid, T bean) {
		this.oid = oid;
		this.resolver = (x) -> {
			return bean;
		};
	}

	public BeanFault(ID oid, Function<ID, T> resolver, ModificationListener... modificationListeners) {
		this.oid = oid;
		this.resolver = resolver;
		if (modificationListeners != null) {
			for (ModificationListener modificationListener : modificationListeners) {
				addModificationListener(modificationListener);
			}
		}
	}

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

	public T getBean() {
		isFault = false;
		if (bean == null) {
			try {
				bean = resolver.apply(oid);
				if (bean != null) {
					lastHashcode = bean.hashCode();
				}
			} catch (Exception ex) {
				L.warn(ex.getMessage());
				bean = null;
			}
		}
		return bean;
	}

	public void invalidate() {
		isFault = true;
		isModificationListenersNotified = false;
		bean = null;
	}

	public boolean isFault() {
		return isFault;
	}

	public boolean isNull() {
		return oid == null && resolver == null;
	}

	public boolean isNotNull() {
		return oid != null || resolver != null;
	}

	public ID getOid() {
		return oid;
	}

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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BeanFault other = (BeanFault) obj;
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
