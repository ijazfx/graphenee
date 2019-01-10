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

public class CollectionFault<T> {

	private static final Logger L = LoggerFactory.getLogger(CollectionFault.class);

	private boolean isFault = true;
	private boolean isModified = false;
	private Supplier<Collection<T>> resolver;
	private Collection<T> valuesAdded;
	private Collection<T> valuesRemoved;
	private Collection<T> valuesUpdated;
	private Collection<T> values;
	private Set<ModificationListener> modificationListeners;

	CollectionFault() {
	}

	public static <T> CollectionFault<T> from(Collection<T> collection) {
		CollectionFault<T> collectionFault = new CollectionFault<>(new ArrayList<>());
		collection.forEach(item -> {
			collectionFault.add(item);
		});
		return collectionFault;
	}

	public static <T> CollectionFault<T> emptyCollectionFault() {
		return new CollectionFault<>(new ArrayList<>());
	}

	public static <T> CollectionFault<T> collectionFault(Collection<T> collection) {
		return new CollectionFault<>(collection);
	}

	public static <T> CollectionFault<T> collectionFault(Supplier<Collection<T>> resolver) {
		return new CollectionFault<>(resolver);
	}

	public static <T> CollectionFault<T> collectionFault(Collection<T> collection, ModificationListener... modificationListeners) {
		return new CollectionFault<>(collection, modificationListeners);
	}

	public static <T> CollectionFault<T> collectionFault(Supplier<Collection<T>> resolver, ModificationListener... modificationListeners) {
		return new CollectionFault<>(resolver, modificationListeners);
	}

	public CollectionFault(Supplier<Collection<T>> resolver) {
		this.resolver = resolver;
	}

	public CollectionFault(Collection<T> values) {
		this.resolver = () -> {
			return values;
		};
	}

	public CollectionFault(Supplier<Collection<T>> resolver, ModificationListener... modificationListeners) {
		this.resolver = resolver;
		if (modificationListeners != null) {
			for (ModificationListener modificationListener : modificationListeners) {
				addModificationListener(modificationListener);
			}
		}
	}

	public CollectionFault(Collection<T> values, ModificationListener... modificationListeners) {
		this.resolver = () -> {
			return values;
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
		values = null;
		valuesAdded = null;
		valuesRemoved = null;
		valuesUpdated = null;
	}

	public Collection<T> getInvalidatedCollection() {
		invalidate();
		return getCollection();
	}

	public Collection<T> getCollection() {
		isFault = false;
		_initializeCollection();
		return Collections.unmodifiableCollection(values);
	}

	private void _initializeCollection() {
		if (values == null) {
			values = resolver.get();
			if (values == null) {
				values = new ArrayList<>();
			}
		}
	}

	public Collection<T> getAdded() {
		_initializeAddedCollection();
		return Collections.unmodifiableCollection(valuesAdded);
	}

	private void _initializeAddedCollection() {
		if (valuesAdded == null) {
			valuesAdded = new ArrayList<>();
		}
	}

	public Collection<T> getUpdated() {
		_initializeUpdatedCollection();
		return Collections.unmodifiableCollection(valuesUpdated);
	}

	private void _initializeUpdatedCollection() {
		if (valuesUpdated == null) {
			valuesUpdated = new ArrayList<>();
		}
	}

	public Collection<T> getRemoved() {
		_initializeRemovedCollection();
		return Collections.unmodifiableCollection(valuesRemoved);
	}

	private void _initializeRemovedCollection() {
		if (valuesRemoved == null) {
			valuesRemoved = new ArrayList<>();
		}
	}

	public void add(T value) {
		_initializeCollection();
		values.add(value);
		_initializeAddedCollection();
		if (!valuesAdded.contains(value)) {
			valuesAdded.add(value);
		}
		_initializeRemovedCollection();
		if (valuesRemoved.contains(value)) {
			valuesRemoved.remove(value);
		}
		notificationModificationListeners();
	}

	public void update(T value) {
		_initializeCollection();
		values.remove(value);
		values.add(value);
		_initializeUpdatedCollection();
		if (!valuesUpdated.contains(value)) {
			valuesUpdated.add(value);
		}
		notificationModificationListeners();
	}

	public void remove(T value) {
		_initializeCollection();
		values.remove(value);
		_initializeRemovedCollection();
		if (!valuesRemoved.contains(value)) {
			valuesRemoved.add(value);
		}
		_initializeAddedCollection();
		if (valuesAdded.contains(value)) {
			valuesAdded.remove(value);
		}
		_initializeUpdatedCollection();
		if (valuesUpdated.contains(value)) {
			valuesUpdated.remove(value);
		}
		notificationModificationListeners();
	}

	public boolean isFault() {
		return isFault;
	}

	public boolean isModified() {
		return isModified || (valuesAdded != null && !valuesAdded.isEmpty())
				|| (valuesRemoved != null && !valuesRemoved.isEmpty() || (valuesUpdated != null && !valuesUpdated.isEmpty()));
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
