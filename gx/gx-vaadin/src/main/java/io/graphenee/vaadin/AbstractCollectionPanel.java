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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.vaadin.viritin.layouts.MPanel;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.SingleComponentContainer;

public abstract class AbstractCollectionPanel<T> extends MPanel {
	private BeanItemContainer<T> collectionContainer;
	private Map<T, SingleComponentContainer> itemContainerReferenceMap;
	private AbstractLayout rootLayout;
	private boolean isBuilt;

	public AbstractCollectionPanel() {
		build();
	}

	public AbstractCollectionPanel<T> build() {
		if (!isBuilt) {
			setSizeFull();
			rootLayout = collectionLayout();
			itemContainerReferenceMap = new HashMap<>();
			rootLayout.setSizeFull();
			setContent(rootLayout);
			postBuild();
			isBuilt = true;
		}
		return this;
	}

	protected abstract AbstractLayout collectionLayout();

	protected void postBuild() {
	}

	public AbstractCollectionPanel(String caption) {
		super(caption);
	}

	public BeanItemContainer<T> getCollectionContainer() {
		return collectionContainer;
	}

	public void setCollectionContainer(BeanItemContainer<T> collectionContainer) {
		this.collectionContainer = collectionContainer;
	}

	protected abstract SingleComponentContainer layoutItem(T item);

	public void refresh() {
		rootLayout.removeAllComponents();
		itemContainerReferenceMap.clear();
		if (getCollectionContainer() != null) {
			SingleComponentContainer lastItemContainer = null;
			Iterator<T> itemIterator = getCollectionContainer().getItemIds().iterator();
			while (itemIterator.hasNext()) {
				T item = itemIterator.next();
				SingleComponentContainer itemContainer = layoutItem(item);
				itemContainerReferenceMap.put(item, itemContainer);
				rootLayout.addComponent(itemContainer);
				lastItemContainer = itemContainer;
			}
			if (lastItemContainer != null && rootLayout instanceof AbstractOrderedLayout) {
				((AbstractOrderedLayout) rootLayout).setExpandRatio(lastItemContainer, 1);
			}
		}
	}

	protected void dismissItem(T item) {
		SingleComponentContainer itemContainer = itemContainerReferenceMap.get(item);
		if (itemContainer != null) {
			collectionContainer.removeItem(item);
			rootLayout.removeComponent(itemContainer);
			itemContainerReferenceMap.remove(item);
		}
	}

}
