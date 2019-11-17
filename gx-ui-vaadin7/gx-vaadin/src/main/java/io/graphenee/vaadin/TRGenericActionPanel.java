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

import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public abstract class TRGenericActionPanel<T> extends TRAbstractPanel {

	MarginInfo marginInfo = new MarginInfo(true);
	boolean showDismissButton = true;

	public Window openInModalPopupWithEntity(T entity) {
		build();
		initializeWithEntity(entity);
		window = new Window(panelTitle(), this);
		UI.getCurrent().addWindow(window);

		window.setModal(isPopupModal());
		window.setClosable(isPopupClosable());
		window.setResizable(isPopupResizable());
		window.setWidth(popupWidth());
		window.setHeight(popupHeight());
		window.setStyleName("popupWindow");

		Responsive.makeResponsive(window);
		window.setCaption(panelTitle());
		focusFirst();
		return window;
	}

	protected abstract void initializeWithEntity(T entity);

	public TRGenericActionPanel<T> withMargin(boolean margin) {
		marginInfo = new MarginInfo(margin);
		return this;
	}

	public TRGenericActionPanel<T> withMarginInfo(MarginInfo marginInfo) {
		this.marginInfo = marginInfo != null ? marginInfo : new MarginInfo(true);
		return this;
	}

	public TRGenericActionPanel<T> withDismissButton(boolean showDismissButton) {
		this.showDismissButton = showDismissButton;
		return this;
	}

	@Override
	protected boolean shouldShowDismissButton() {
		return showDismissButton;
	}

}
