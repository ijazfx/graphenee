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
package io.graphenee.core.flow.documents;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.router.AfterNavigationEvent;

import io.graphenee.core.GxDataService;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxUserAccount;
import io.graphenee.util.storage.FileStorage;
import io.graphenee.vaadin.flow.GxSecuredView;
import io.graphenee.vaadin.flow.GxVerticalLayoutView;

@SuppressWarnings("serial")
@GxSecuredView(GxDocumentArchivedListView.VIEW_NAME)
@Scope("prototype")
public class GxDocumentArchivedListView extends GxVerticalLayoutView {

	public static final String VIEW_NAME = "trash-documents";

	@Autowired
	GxDocumentArchivedList list;

	@Autowired(required = false)
	FileStorage storage;

	@Autowired
	GxDataService dataService;

	@Override
	protected String getCaption() {
		return "Trash";
	}

	@Override
	protected void decorateLayout(HasComponents rootLayout) {
		list.setEditable(false);
		rootLayout.add(list);
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		if (loggedInUser() instanceof GxUserAccount) {
			GxUserAccount user = ((GxUserAccount) loggedInUser());
			GxNamespace namespace = user.getNamespace();
			list.initializeByNamespace(namespace);
			list.initializeWithNamespaceAndStorageAndSearchListAndLayoutAndUser(namespace, storage, user);
		}
	}

}
