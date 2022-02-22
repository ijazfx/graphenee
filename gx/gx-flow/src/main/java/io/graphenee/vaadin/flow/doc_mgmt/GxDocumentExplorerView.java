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
package io.graphenee.vaadin.flow.doc_mgmt;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.router.AfterNavigationEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import io.graphenee.core.api.GxNamespaceService;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.vaadin.flow.base.GxSecuredView;
import io.graphenee.vaadin.flow.base.GxVerticalLayoutView;

@GxSecuredView(GxDocumentExplorerView.VIEW_NAME)
@Scope("prototype")
public class GxDocumentExplorerView extends GxVerticalLayoutView {

	public static final String VIEW_NAME = "documents";

	@Autowired
	GxDocumentExplorer list;

	@Autowired
	GxNamespaceService namespaceService;

	@Override
	protected String getCaption() {
		return "Documents";
	}

	@Override
	protected void decorateLayout(HasComponents rootLayout) {
		add(list);
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		GxNamespace namespace = namespaceService.applicationNamespaceEntity();
		list.initializeWithNamespace(namespace);
	}

}
