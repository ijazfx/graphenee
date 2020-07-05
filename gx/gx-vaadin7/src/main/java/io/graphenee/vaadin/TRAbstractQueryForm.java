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

import java.util.function.Consumer;

import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.vaadin.server.Resource;
import com.vaadin.ui.HorizontalLayout;

public abstract class TRAbstractQueryForm<T> extends TRAbstractForm<T> {

	private static final long serialVersionUID = 1L;

	private QueryFormDelegate<T> queryFormDelegate;

	private MHorizontalLayout buttons;

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected void addButtonsToFooter(HorizontalLayout footer) {
		super.addButtonsToFooter(footer);
		footer.removeComponent(getSaveButton());
		setSavedHandler(event -> {
			if (queryFormDelegate != null) {
				queryFormDelegate.onSubmit(event);
			}
		});
		getSaveButton().setCaption("Submit");
		buttons = new MHorizontalLayout(getSaveButton());
		footer.addComponents(buttons);
		footer.setWidth("100%");
	}

	public QueryFormDelegate<T> getQueryFormDelegate() {
		return queryFormDelegate;
	}

	public void setQueryFormDelegate(QueryFormDelegate<T> queryFormDelegate) {
		this.queryFormDelegate = queryFormDelegate;
	}

	public void setSubmitButtonVisibility(boolean visibility) {
		if (getSaveButton() != null) {
			getSaveButton().setVisible(visibility);
		}
	}

	public static interface QueryFormDelegate<T> {
		void onSubmit(T queryBean);
	}

	public MButton addButtonToFooter(String caption, Consumer<T> callback) {
		return addButtonToFooter(null, caption, callback);
	}

	public MButton addButtonToFooter(Resource icon, String caption, Consumer<T> callback) {
		MButton button = new MButton(icon, caption, event -> {
			callback.accept(getEntity());
		});
		buttons.add(button);
		return button;
	}

	@Override
	protected boolean shouldShowDismissButton() {
		return false;
	}

}
