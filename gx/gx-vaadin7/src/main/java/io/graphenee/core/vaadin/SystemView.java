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
package io.graphenee.core.vaadin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;

import io.graphenee.vaadin.AbstractDashboardPanel;
import io.graphenee.vaadin.TRView;

@SpringView(name = SystemView.VIEW_NAME)
@Scope("prototype")
public class SystemView extends AbstractDashboardPanel implements TRView {

	public static final String VIEW_NAME = "gx-system";

	@Autowired
	GxNamespaceListPanel namespaceListPanel;

	@Autowired
	GxResourceListPanel resourceListPanel;

	@Override
	protected String panelTitle() {
		return "System";
	}

	@Override
	protected void postInitialize() {
		MenuBar menuBar = new MenuBar();
		MenuItem manageMenu = menuBar.addItem("Manage", null);
		manageMenu.addItem("Namespaces", event -> {
			resourceListPanel.setVisible(false);
			namespaceListPanel.setVisible(true);
			namespaceListPanel.refresh();
		});
		manageMenu.addItem("Resources", event -> {
			namespaceListPanel.setVisible(false);
			resourceListPanel.setVisible(true);
			resourceListPanel.refresh();
		});
		addComponentsToToolbar(menuBar);
		addComponent(namespaceListPanel.build().withVisible(true));
		addComponent(resourceListPanel.build().withVisible(false));
	}

	@Override
	public void enter(ViewChangeEvent event) {
		if (namespaceListPanel.isVisible()) {
			namespaceListPanel.refresh();
		} else if (resourceListPanel.isVisible()) {
			resourceListPanel.refresh();
		}
	}

	@Override
	protected boolean shouldShowHeader() {
		return true;
	}

}
