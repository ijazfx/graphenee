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

import com.vaadin.ui.HorizontalLayout;

public abstract class TRAbstractSearchForm<T> extends TRAbstractForm<T> {

	private static final long serialVersionUID = 1L;

	private SearchFormDelegate<T> searchFormDelegate;

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected void addButtonsToFooter(HorizontalLayout footer) {
		super.addButtonsToFooter(footer);
		setSavedHandler(event -> {
			if (searchFormDelegate != null) {
				searchFormDelegate.onSearch(event);
			}
			closePopup();
		});
		getSaveButton().setCaption("Search");
	}

	public void setSearchFormDelegate(SearchFormDelegate<T> searchFormDelegate) {
		this.searchFormDelegate = searchFormDelegate;
	}

	public void setSubmitButtonVisibility(boolean visibility) {
		if (getSaveButton() != null) {
			getSaveButton().setVisible(visibility);
		}
	}

	public static interface SearchFormDelegate<T> {
		void onSearch(T queryBean);
	}

}
