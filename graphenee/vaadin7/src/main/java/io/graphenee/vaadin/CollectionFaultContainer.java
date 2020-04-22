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
package io.graphenee.vaadin;

import java.util.Collection;

import com.vaadin.data.Container;
import com.vaadin.data.util.AbstractBeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;

import io.graphenee.core.model.CollectionFault;

@SuppressWarnings("serial")
public class CollectionFaultContainer<BEANTYPE> extends BeanItemContainer<BEANTYPE> {

	CollectionFault<BEANTYPE> collectionFault = CollectionFault.emptyCollectionFault();

	public CollectionFaultContainer(Class<? super BEANTYPE> type) {
		super(type);
	}

	public CollectionFaultContainer(Class<? super BEANTYPE> type, CollectionFault<BEANTYPE> beanCollectionFault) throws IllegalArgumentException {
		super(type);
		setBeanCollectionFault(beanCollectionFault);
	}

	public void setBeanCollectionFault(CollectionFault<BEANTYPE> beanCollectionFault) {
		this.collectionFault = beanCollectionFault;
		addAll(beanCollectionFault.getCollection());
	}

	/**
	 * Adds all the beans from a {@link Collection} in one go. More efficient
	 * than adding them one by one.
	 *
	 * @param collection The collection of beans to add. Must not be null.
	 */
	@Override
	public void addAll(Collection<? extends BEANTYPE> collection) {
		if (collection != null) {
			collection.forEach(item -> {
				collectionFault.add(item);
			});
		}
		super.addAll(collectionFault.getCollection());
	}

	/**
	 * Adds the bean after the given bean. The bean is used both as the item
	 * contents and as the item identifier.
	 *
	 * @param previousItemId the bean (of type BT) after which to add newItemId
	 * @param newItemId the bean (of type BT) to add (not null)
	 * @see Container.Ordered#addItemAfter(Object, Object)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public BeanItem<BEANTYPE> addItemAfter(Object previousItemId, Object newItemId) throws IllegalArgumentException {
		collectionFault.add((BEANTYPE) newItemId);
		return super.addBeanAfter((BEANTYPE) previousItemId, (BEANTYPE) newItemId);
	}

	/**
	 * Adds a new bean at the given index. The bean is used both as the item
	 * contents and as the item identifier.
	 *
	 * @param index Index at which the bean should be added.
	 * @param newItemId The bean to add to the container.
	 * @return Returns the new BeanItem or null if the operation fails.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public BeanItem<BEANTYPE> addItemAt(int index, Object newItemId) throws IllegalArgumentException {
		collectionFault.add((BEANTYPE) newItemId);
		return super.addBeanAt(index, (BEANTYPE) newItemId);
	}

	/**
	 * Adds the bean to the Container. The bean is used both as the item
	 * contents and as the item identifier.
	 *
	 * @see Container#addItem(Object)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public BeanItem<BEANTYPE> addItem(Object itemId) {
		collectionFault.add((BEANTYPE) itemId);
		return super.addBean((BEANTYPE) itemId);
	}

	/**
	 * Adds the bean to the Container. The bean is used both as the item
	 * contents and as the item identifier.
	 *
	 * @see Container#addItem(Object)
	 */
	@Override
	public BeanItem<BEANTYPE> addBean(BEANTYPE bean) {
		collectionFault.add(bean);
		return addItem(bean);
	}

	/**
	 * Unsupported in BeanItemContainer.
	 */
	@Override
	protected void setBeanIdResolver(AbstractBeanContainer.BeanIdResolver<BEANTYPE, BEANTYPE> beanIdResolver) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("BeanItemContainer always uses an IdentityBeanIdResolver");
	}

	@Override
	public boolean removeItem(Object itemId) {
		collectionFault.remove((BEANTYPE) itemId);
		// return super.removeItem(itemId);
		return true;
	}

	@Override
	public boolean removeAllItems() {
		getAllItemIds().forEach(item -> {
			collectionFault.remove(item);
		});
		// return super.removeAllItems();
		return true;
	}

}
