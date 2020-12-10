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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanCollectionFault<T> {

	private static final Logger L = LoggerFactory.getLogger(BeanCollectionFault.class);

	private boolean isFault = true;
	private boolean isModified = false;
	private Supplier<Collection<T>> resolver;
	private Collection<T> beansAdded;
	private Collection<T> beansRemoved;
	private Collection<T> beansUpdated;
	private Collection<T> beans;
	private Set<ModificationListener> modificationListeners;

	BeanCollectionFault() {
	}

	public static <T> BeanCollectionFault<T> from(Collection<T> collection) {
		BeanCollectionFault<T> beanCollectionFault = new BeanCollectionFault<>(new ArrayList<>());
		collection.forEach(item -> {
			beanCollectionFault.add(item);
		});
		return beanCollectionFault;
	}

	public static <T> BeanCollectionFault<T> emptyCollectionFault() {
		return new BeanCollectionFault<>(new ArrayList<>());
	}

	public static <T> BeanCollectionFault<T> collectionFault(Collection<T> collection) {
		return new BeanCollectionFault<>(collection);
	}

	public static <T> BeanCollectionFault<T> collectionFault(Supplier<Collection<T>> resolver) {
		return new BeanCollectionFault<>(resolver);
	}

	public static <T> BeanCollectionFault<T> collectionFault(Collection<T> collection, ModificationListener... modificationListeners) {
		return new BeanCollectionFault<>(collection, modificationListeners);
	}

	public static <T> BeanCollectionFault<T> collectionFault(Supplier<Collection<T>> resolver, ModificationListener... modificationListeners) {
		return new BeanCollectionFault<>(resolver, modificationListeners);
	}

	public BeanCollectionFault(Supplier<Collection<T>> resolver) {
		this.resolver = resolver;
	}

	public BeanCollectionFault(Collection<T> beans) {
		this.resolver = () -> {
			return beans;
		};
	}

	public BeanCollectionFault(Supplier<Collection<T>> resolver, ModificationListener... modificationListeners) {
		this.resolver = resolver;
		if (modificationListeners != null) {
			for (ModificationListener modificationListener : modificationListeners) {
				addModificationListener(modificationListener);
			}
		}
	}

	public BeanCollectionFault(Collection<T> beans, ModificationListener... modificationListeners) {
		this.resolver = () -> {
			return beans;
		};
		if (modificationListeners != null) {
			for (ModificationListener modificationListener : modificationListeners) {
				addModificationListener(modificationListener);
			}
		}
	}

	public void invalidate() {
		isFault = true;
		isModified = false;
		beans = null;
		beansAdded = null;
		beansRemoved = null;
		beansUpdated = null;
	}

	public Collection<T> getBeans() {
		isFault = false;
		_initializeBeansCollection();
		return Collections.unmodifiableCollection(beans);
	}

	private void _initializeBeansCollection() {
		if (beans == null) {
			beans = resolver.get();
			if (beans == null) {
				beans = new ArrayList<>();
			}
		}
	}

	public Collection<T> getBeansAdded() {
		_initializeBeansAddedCollection();
		return Collections.unmodifiableCollection(beansAdded);
	}

	private void _initializeBeansAddedCollection() {
		if (beansAdded == null) {
			beansAdded = new ArrayList<>();
		}
	}

	public Collection<T> getBeansUpdated() {
		_initializeBeansUpdatedCollection();
		return Collections.unmodifiableCollection(beansUpdated);
	}

	private void _initializeBeansUpdatedCollection() {
		if (beansUpdated == null) {
			beansUpdated = new ArrayList<>();
		}
	}

	public Collection<T> getBeansRemoved() {
		_initializeBeansRemovedCollection();
		return Collections.unmodifiableCollection(beansRemoved);
	}

	private void _initializeBeansRemovedCollection() {
		if (beansRemoved == null) {
			beansRemoved = new ArrayList<>();
		}
	}

	public void add(T bean) {
		_initializeBeansCollection();
		beans.add(bean);
		_initializeBeansAddedCollection();
		if (!beansAdded.contains(bean)) {
			beansAdded.add(bean);
		}
		_initializeBeansRemovedCollection();
		if (beansRemoved.contains(bean)) {
			beansRemoved.remove(bean);
		}
		notificationModificationListeners();
	}

	public void update(T bean) {
		_initializeBeansCollection();
		beans.remove(bean);
		beans.add(bean);
		_initializeBeansUpdatedCollection();
		if (!beansUpdated.contains(bean)) {
			beansUpdated.add(bean);
		}
		_initializeBeansRemovedCollection();
		if (beansRemoved.contains(bean)) {
			beansRemoved.remove(bean);
		}
		notificationModificationListeners();
	}

	public void remove(T bean) {
		_initializeBeansCollection();
		beans.remove(bean);
		_initializeBeansRemovedCollection();
		if (!beansRemoved.contains(bean)) {
			beansRemoved.add(bean);
		}
		_initializeBeansAddedCollection();
		if (beansAdded.contains(bean)) {
			beansAdded.remove(bean);
		}
		_initializeBeansUpdatedCollection();
		if (beansUpdated.contains(bean)) {
			beansUpdated.remove(bean);
		}
		notificationModificationListeners();
	}

	public boolean isFault() {
		return isFault;
	}

	public boolean isModified() {
		return isModified || (beansAdded != null && !beansAdded.isEmpty())
				|| (beansRemoved != null && !beansRemoved.isEmpty() || (beansUpdated != null && !beansUpdated.isEmpty()));
	}

	public void markAsModified() {
		this.isModified = true;
	}

	private Set<ModificationListener> getModificationListeners() {
		if (modificationListeners == null) {
			synchronized (this) {
				if (modificationListeners == null) {
					modificationListeners = new HashSet<>();
				}
			}
		}
		return modificationListeners;
	}

	public void addModificationListener(ModificationListener modificationListener) {
		getModificationListeners().add(modificationListener);
	}

	public void removeModificationListener(ModificationListener modificationListener) {
		getModificationListeners().remove(modificationListener);
	}

	public void notificationModificationListeners() {
		if (modificationListeners == null) {
			return;
		}
		getModificationListeners().forEach(modificationListener -> {
			modificationListener.onModification();
		});
	}

}
