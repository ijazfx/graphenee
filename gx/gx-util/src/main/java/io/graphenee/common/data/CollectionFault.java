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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/**
 * A fault-tolerant collection that can be used to lazily load a collection of beans.
 *
 * @param <T> The bean type.
 */
public class CollectionFault<T> {

	private boolean isFault = true;
	private boolean isModified = false;
	private Supplier<Collection<T>> resolver;
	private Collection<T> valuesAdded;
	private Collection<T> valuesRemoved;
	private Collection<T> valuesUpdated;
	private Collection<T> values;
	private Set<ModificationListener> modificationListeners;

	/**
	 * Creates a new instance of this fault.
	 */
	CollectionFault() {
	}

	/**
	 * Creates a new instance of this fault from a collection.
	 * @param <T> The bean type.
	 * @param collection The collection to create the fault from.
	 * @return The new fault.
	 */
	public static <T> CollectionFault<T> from(Collection<T> collection) {
		CollectionFault<T> collectionFault = new CollectionFault<>(new ArrayList<>());
		collection.forEach(item -> {
			collectionFault.add(item);
		});
		return collectionFault;
	}

	/**
	 * Creates a new empty instance of this fault.
	 * @param <T> The bean type.
	 * @return The new fault.
	 */
	public static <T> CollectionFault<T> emptyCollectionFault() {
		return new CollectionFault<>(new ArrayList<>());
	}

	/**
	 * Creates a new instance of this fault from a collection.
	 * @param <T> The bean type.
	 * @param collection The collection to create the fault from.
	 * @return The new fault.
	 */
	public static <T> CollectionFault<T> collectionFault(Collection<T> collection) {
		return new CollectionFault<>(collection);
	}

	/**
	 * Creates a new instance of this fault from a resolver.
	 * @param <T> The bean type.
	 * @param resolver The resolver to create the fault from.
	 * @return The new fault.
	 */
	public static <T> CollectionFault<T> collectionFault(Supplier<Collection<T>> resolver) {
		return new CollectionFault<>(resolver);
	}

	/**
	 * Creates a new instance of this fault from a collection and modification listeners.
	 * @param <T> The bean type.
	 * @param collection The collection to create the fault from.
	 * @param modificationListeners The modification listeners.
	 * @return The new fault.
	 */
	public static <T> CollectionFault<T> collectionFault(Collection<T> collection, ModificationListener... modificationListeners) {
		return new CollectionFault<>(collection, modificationListeners);
	}

	/**
	 * Creates a new instance of this fault from a resolver and modification listeners.
	 * @param <T> The bean type.
	 * @param resolver The resolver to create the fault from.
	 * @param modificationListeners The modification listeners.
	 * @return The new fault.
	 */
	public static <T> CollectionFault<T> collectionFault(Supplier<Collection<T>> resolver, ModificationListener... modificationListeners) {
		return new CollectionFault<>(resolver, modificationListeners);
	}

	/**
	 * Creates a new instance of this fault from a resolver.
	 * @param resolver The resolver to create the fault from.
	 */
	public CollectionFault(Supplier<Collection<T>> resolver) {
		this.resolver = resolver;
	}

	/**
	 * Creates a new instance of this fault from a collection.
	 * @param values The collection to create the fault from.
	 */
	public CollectionFault(Collection<T> values) {
		this.resolver = () -> {
			return values;
		};
	}

	/**
	 * Creates a new instance of this fault from a resolver and modification listeners.
	 * @param resolver The resolver to create the fault from.
	 * @param modificationListeners The modification listeners.
	 */
	public CollectionFault(Supplier<Collection<T>> resolver, ModificationListener... modificationListeners) {
		this.resolver = resolver;
		if (modificationListeners != null) {
			for (ModificationListener modificationListener : modificationListeners) {
				addModificationListener(modificationListener);
			}
		}
	}

	/**
	 * Creates a new instance of this fault from a collection and modification listeners.
	 * @param values The collection to create the fault from.
	 * @param modificationListeners The modification listeners.
	 */
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

	/**
	 * Invalidates this fault.
	 */
	public void invalidate() {
		isFault = true;
		isModified = false;
		values = null;
		valuesAdded = null;
		valuesRemoved = null;
		valuesUpdated = null;
	}

	/**
	 * Gets the invalidated collection.
	 * @return The invalidated collection.
	 */
	public Collection<T> getInvalidatedCollection() {
		invalidate();
		return getCollection();
	}

	/**
	 * Gets the collection.
	 * @return The collection.
	 */
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

	/**
	 * Gets the added values.
	 * @return The added values.
	 */
	public Collection<T> getAdded() {
		_initializeAddedCollection();
		return Collections.unmodifiableCollection(valuesAdded);
	}

	private void _initializeAddedCollection() {
		if (valuesAdded == null) {
			valuesAdded = new ArrayList<>();
		}
	}

	/**
	 * Gets the updated values.
	 * @return The updated values.
	 */
	public Collection<T> getUpdated() {
		_initializeUpdatedCollection();
		return Collections.unmodifiableCollection(valuesUpdated);
	}

	private void _initializeUpdatedCollection() {
		if (valuesUpdated == null) {
			valuesUpdated = new ArrayList<>();
		}
	}

	/**
	 * Gets the removed values.
	 * @return The removed values.
	 */
	public Collection<T> getRemoved() {
		_initializeRemovedCollection();
		return Collections.unmodifiableCollection(valuesRemoved);
	}

	private void _initializeRemovedCollection() {
		if (valuesRemoved == null) {
			valuesRemoved = new ArrayList<>();
		}
	}

	/**
	 * Adds a value to this fault.
	 * @param value The value to add.
	 */
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

	/**
	 * Updates a value in this fault.
	 * @param value The value to update.
	 */
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

	/**
	 * Removes a value from this fault.
	 * @param value The value to remove.
	 */
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

	/**
	 * Checks if this is a fault.
	 * @return True if this is a fault, false otherwise.
	 */
	public boolean isFault() {
		return isFault;
	}

	/**
	 * Checks if this fault has been modified.
	 * @return True if this fault has been modified, false otherwise.
	 */
	public boolean isModified() {
		return isModified || (valuesAdded != null && !valuesAdded.isEmpty())
				|| (valuesRemoved != null && !valuesRemoved.isEmpty() || (valuesUpdated != null && !valuesUpdated.isEmpty()));
	}

	/**
	 * Marks this fault as modified.
	 */
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

	/**
	 * Adds a modification listener to this fault.
	 * @param modificationListener The modification listener to add.
	 */
	public void addModificationListener(ModificationListener modificationListener) {
		getModificationListeners().add(modificationListener);
	}

	/**
	 * Removes a modification listener from this fault.
	 * @param modificationListener The modification listener to remove.
	 */
	public void removeModificationListener(ModificationListener modificationListener) {
		getModificationListeners().remove(modificationListener);
	}

	/**
	 * Notifies the modification listeners that this fault has been modified.
	 */
	public void notificationModificationListeners() {
		if (modificationListeners == null) {
			return;
		}
		getModificationListeners().forEach(modificationListener -> {
			modificationListener.onModification();
		});
	}

}
