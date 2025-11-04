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
package io.graphenee.core.flow.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.router.AfterNavigationEvent;

import io.graphenee.core.model.entity.GxUserAccount;
import io.graphenee.vaadin.flow.GxSecuredView;
import io.graphenee.vaadin.flow.GxVerticalLayoutView;

@SuppressWarnings("serial")
@GxSecuredView(GxTermView.VIEW_NAME)
@Scope("prototype")
public class GxTermView extends GxVerticalLayoutView {

	public static final String VIEW_NAME = "translations";

	@Autowired
	GxTermListPanel termListPanel;

	@Override
	protected String getCaption() {
		return "Translations";
	}

	@Override
	protected void decorateLayout(HasComponents rootLayout) {
		rootLayout.add(termListPanel);
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		if (loggedInUser() instanceof GxUserAccount) {
			termListPanel.initializeWithNamespace(((GxUserAccount) loggedInUser()).getNamespace());
		}
	}

}
