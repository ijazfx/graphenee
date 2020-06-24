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

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.SingleComponentContainer;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public abstract class ListViewCollectionPanel<T> extends AbstractCollectionPanel<T> {

	private VerticalLayout layout;
	private boolean hoverShadow;

	@Override
	protected AbstractLayout collectionLayout() {
		if (layout == null) {
			layout = new VerticalLayout();
			layout.setSizeFull();
		}
		return layout;
	}

	public ListViewCollectionPanel<T> withMargin(boolean value) {
		collectionLayout();
		layout.setMargin(value);
		return this;
	}

	public ListViewCollectionPanel<T> withMargin(MarginInfo marginInfo) {
		collectionLayout();
		layout.setMargin(marginInfo);
		return this;
	}

	public ListViewCollectionPanel<T> withSpacing(boolean value) {
		collectionLayout();
		layout.setSpacing(value);
		return this;
	}

	public ListViewCollectionPanel<T> withHoverShadow(boolean hoverShadow) {
		this.hoverShadow = hoverShadow;
		return this;
	}

	@Override
	protected SingleComponentContainer layoutItem(T item) {
		MPanel panel = new MPanel().withStyleName(ValoTheme.PANEL_BORDERLESS);
		if (hoverShadow) {
			panel.addStyleName("hover-shadow");
		} else {
			panel.removeStyleName("hover-shadow");
			panel.addStyleName("separator");
		}
		layoutItem(panel, item);
		return panel;
	}

	protected abstract void layoutItem(final MPanel panel, final T item);

}
