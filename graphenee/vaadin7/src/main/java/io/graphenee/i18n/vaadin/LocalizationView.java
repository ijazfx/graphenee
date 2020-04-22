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
package io.graphenee.i18n.vaadin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;

import io.graphenee.vaadin.AbstractDashboardPanel;
import io.graphenee.vaadin.TRView;

@SpringView(name = LocalizationView.VIEW_NAME)
@Scope("prototype")
public class LocalizationView extends AbstractDashboardPanel implements TRView {

	public static final String VIEW_NAME = "gx-i18n";

	@Autowired
	GxSupportedLocaleListPanel supportedLocaleListPanel;

	@Autowired
	GxTermListPanel termListPanel;

	@Override
	protected String panelTitle() {
		return "Localization";
	}

	@Override
	protected void postInitialize() {
		MenuBar menuBar = new MenuBar();
		MenuItem manageMenu = menuBar.addItem("Manage", null);
		manageMenu.addItem("Supported Locales", event -> {
			termListPanel.setVisible(false);
			supportedLocaleListPanel.setVisible(true);
			supportedLocaleListPanel.refresh();
		});
		manageMenu.addItem("Terms", event -> {
			supportedLocaleListPanel.setVisible(false);
			termListPanel.setVisible(true);
			termListPanel.refresh();
		});
		addComponentsToToolbar(menuBar);
		addComponent(supportedLocaleListPanel.build().withVisible(false));
		addComponent(termListPanel.build().withVisible(true));
	}

	@Override
	public void enter(ViewChangeEvent event) {
		if (supportedLocaleListPanel.isVisible()) {
			supportedLocaleListPanel.refresh();
		} else if (termListPanel.isVisible()) {
			termListPanel.refresh();
		}
	}

	@Override
	protected boolean shouldShowHeader() {
		return true;
	}

}
