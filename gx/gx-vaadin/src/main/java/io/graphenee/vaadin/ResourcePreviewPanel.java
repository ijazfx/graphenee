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

import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.server.Resource;
import com.vaadin.ui.BrowserFrame;

public class ResourcePreviewPanel extends TRAbstractPanel {

	private BrowserFrame viewer;

	@Override
	protected boolean isSpringComponent() {
		return false;
	}

	@Override
	protected void addButtonsToFooter(MHorizontalLayout layout) {
		layout.setVisible(false);
	}

	@Override
	protected String panelTitle() {
		return "Preview";
	}

	@Override
	protected void addComponentsToContentLayout(MVerticalLayout layout) {
		viewer = new BrowserFrame(null);
		viewer.setResponsive(true);
		viewer.setSizeFull();
		layout.add(viewer);
		layout.setExpandRatio(viewer, 1);
	}

	@Override
	protected String popupHeight() {
		return "600px";
	}

	@Override
	protected String popupWidth() {
		return "800px";
	}

	public void preview(Resource resource) {
		viewer.setSource(resource);
		viewer.markAsDirtyRecursive();
	}

}
