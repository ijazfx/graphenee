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

import org.vaadin.viritin.layouts.MPanel;

import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.SingleComponentContainer;

public abstract class CardCollectionPanel<T> extends AbstractCollectionPanel<T> {

	@Override
	protected AbstractLayout collectionLayout() {
		CssLayout layout = new CssLayout();
		layout.setSizeFull();
		layout.addStyleName("card-collection");
		return layout;
	}

	@Override
	protected SingleComponentContainer layoutItem(T item) {
		MPanel panel = new MPanel();
		panel.addStyleName("card-item");
		layoutCard(panel, item);
		return panel;
	}

	protected abstract void layoutCard(final MPanel cardPanel, final T item);

}
