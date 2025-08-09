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
 * @deprecated, use CollectionFault instead.
 * @param <T> The bean type.
 */
@Deprecated(forRemoval = true)
public class BeanCollectionFault<T> {

	private boolean isFault = true;
	private boolean isModified = false;
	private Supplier<Collection<T>> resolver;
	private Collection<T> beansAdded;
	private Collection<T> beansRemoved;
	private Collection<T> beansUpdated;
	private Collection<T> beans;
	private Set<ModificationListener> modificationListeners;

	/**
	 * Creates a new instance of this fault.
	 */
	BeanCollectionFault() {
	}

	/**
	 * Creates a new instance of this fault from a collection.
	 * @param <T> The bean type.
	 * @param collection The collection to create the fault from.
	 * @return The new fault.
	 */
	public static <T> BeanCollectionFault<T> from(Collection<T> collection) {
		BeanCollectionFault<T> beanCollectionFault = new BeanCollectionFault<>(new ArrayList<>());
		collection.forEach(item -> {
			beanCollectionFault.add(item);
		});
		return beanCollectionFault;
	}

	/**
	 * Creates a new empty instance of this fault.
	 * @param <T> The bean type.
	 * @return The new fault.
	 */
	public static <T> BeanCollectionFault<T> emptyCollectionFault() {
		return new BeanCollectionFault<>(new ArrayList<>());
	}

	/**
	 * Creates a new instance of this fault from a collection.
	 * @param <T> The bean type.
	 * @param collection The collection to create the fault from.
	 * @return The new fault.
	 */
	public static <T> BeanCollectionFault<T> collectionFault(Collection<T> collection) {
		return new BeanCollectionFault<>(collection);
	}

	/**
	 * Creates a new instance of this fault from a resolver.
	 * @param <T> The bean type.
	 * @param resolver The resolver to create the fault from.
	 * @return The new fault.
	 */
	public static <T> BeanCollectionFault<T> collectionFault(Supplier<Collection<T>> resolver) {
		return new BeanCollectionFault<>(resolver);
	}

	/**
	 * Creates a new instance of this fault from a collection and modification listeners.
	 * @param <T> The bean type.
	 * @param collection The collection to create the fault from.
	 * @param modificationListeners The modification listeners.
	 * @return The new fault.
	 */
	public static <T> BeanCollectionFault<T> collectionFault(Collection<T> collection, ModificationListener... modificationListeners) {
		return new BeanCollectionFault<>(collection, modificationListeners);
	}

	/**
	 * Creates a new instance of this fault from a resolver and modification listeners.
	 * @param <T> The bean type.
	 * @param resolver The resolver to create the fault from.
	 * @param modificationListeners The modification listeners.
	 * @return The new fault.
	 */
	public static <T> BeanCollectionFault<T> collectionFault(Supplier<Collection<T>> resolver, ModificationListener... modificationListeners) {
		return new BeanCollectionFault<>(resolver, modificationListeners);
	}

	/**
	 * Creates a new instance of this fault from a resolver.
	 * @param resolver The resolver to create the fault from.
	 */
	public BeanCollectionFault(Supplier<Collection<T>> resolver) {
		this.resolver = resolver;
	}

	/**
	 * Creates a new instance of this fault from a collection.
	 * @param beans The collection to create the fault from.
	 */
	public BeanCollectionFault(Collection<T> beans) {
		this.resolver = () -> {
			return beans;
		};
	}

	/**
	 * Creates a new instance of this fault from a resolver and modification listeners.
	 * @param resolver The resolver to create the fault from.
	 * @param modificationListeners The modification listeners.
	 */
	public BeanCollectionFault(Supplier<Collection<T>> resolver, ModificationListener... modificationListeners) {
		this.resolver = resolver;
		if (modificationListeners != null) {
			for (ModificationListener modificationListener : modificationListeners) {
				addModificationListener(modificationListener);
			}
		}
	}

	/**
	 * Creates a new instance of this fault from a collection and modification listeners.
	 * @param beans The collection to create the fault from.
	 * @param modificationListeners The modification listeners.
	 */
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

	/**
	 * Invalidates this fault.
	 */
	public void invalidate() {
		isFault = true;
		isModified = false;
		beans = null;
		beansAdded = null;
		beansRemoved = null;
		beansUpdated = null;
	}

	/**
	 * Gets the beans in this fault.
	 * @return The beans.
	 */
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

	/**
	 * Gets the beans that have been added to this fault.
	 * @return The added beans.
	 */
	public Collection<T> getBeansAdded() {
		_initializeBeansAddedCollection();
		return Collections.unmodifiableCollection(beansAdded);
	}

	private void _initializeBeansAddedCollection() {
		if (beansAdded == null) {
			beansAdded = new ArrayList<>();
		}
	}

	/**
	 * Gets the beans that have been updated in this fault.
	 * @return The updated beans.
	 */
	public Collection<T> getBeansUpdated() {
		_initializeBeansUpdatedCollection();
		return Collections.unmodifiableCollection(beansUpdated);
	}

	private void _initializeBeansUpdatedCollection() {
		if (beansUpdated == null) {
			beansUpdated = new ArrayList<>();
		}
	}

	/**
	 * Gets the beans that have been removed from this fault.
	 * @return The removed beans.
	 */
	public Collection<T> getBeansRemoved() {
		_initializeBeansRemovedCollection();
		return Collections.unmodifiableCollection(beansRemoved);
	}

	private void _initializeBeansRemovedCollection() {
		if (beansRemoved == null) {
			beansRemoved = new ArrayList<>();
		}
	}

	/**
	 * Adds a bean to this fault.
	 * @param bean The bean to add.
	 */
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

	/**
	 * Updates a bean in this fault.
	 * @param bean The bean to update.
	 */
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

	/**
	 * Removes a bean from this fault.
	 * @param bean The bean to remove.
	 */
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
		return isModified || (beansAdded != null && !beansAdded.isEmpty())
				|| (beansRemoved != null && !beansRemoved.isEmpty() || (beansUpdated != null && !beansUpdated.isEmpty()));
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
