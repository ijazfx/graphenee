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
package io.graphenee.workshop.vaadin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Image;

import io.graphenee.core.vaadin.SystemView;
import io.graphenee.i18n.vaadin.LocalizationView;
import io.graphenee.security.vaadin.SecurityView;
import io.graphenee.vaadin.AbstractDashboardSetup;
import io.graphenee.vaadin.TRMenuItem;
import io.graphenee.vaadin.TRSimpleMenuItem;

@Service
@UIScope
public class WorkshopDashboardSetup extends AbstractDashboardSetup {

	private ViewProvider viewProvider;

	public WorkshopDashboardSetup(ViewProvider viewProvider) {
		this.viewProvider = viewProvider;
	}

	@Override
	public String applicationTitle() {
		return "Graphenee Workshop";
	}

	@Override
	public Image applicationLogo() {
		return null;
	}

	@Override
	protected List<TRMenuItem> menuItems() {
		List<TRMenuItem> menus = new ArrayList<>();
		menus.add(TRSimpleMenuItem.createMenuItemForView(MetroStyleDashboardView.VIEW_NAME, "Home", FontAwesome.HOME));
		menus.add(TRSimpleMenuItem.createMenuItemForView(MeetingHostView.VIEW_NAME, "Meeting Host", FontAwesome.VIDEO_CAMERA));
		menus.add(TRSimpleMenuItem.createMenuItemForView(MeetingClientView.VIEW_NAME, "Meeting Client", FontAwesome.VIDEO_CAMERA));
		TRSimpleMenuItem dmMenuItem = TRSimpleMenuItem.createMenuItemForView(SystemView.VIEW_NAME, "Data Maintenance", FontAwesome.WRENCH);
		dmMenuItem.addChild(TRSimpleMenuItem.createMenuItemForView(SystemView.VIEW_NAME, "System", FontAwesome.SERVER));
		dmMenuItem.addChild(TRSimpleMenuItem.createMenuItemForView(LocalizationView.VIEW_NAME, "Localization", FontAwesome.GLOBE));
		dmMenuItem.addChild(TRSimpleMenuItem.createMenuItemForView(SecurityView.VIEW_NAME, "Security", FontAwesome.USER_SECRET));
		menus.add(dmMenuItem);
		return menus;
	}

	@Override
	protected List<TRMenuItem> profileMenuItems() {
		return null;
	}

	@Override
	public String dashboardViewName() {
		return MetroStyleDashboardView.VIEW_NAME;
	}

	@Override
	public void registerViewProviders(Navigator navigator) {
		navigator.addProvider(viewProvider);
	}

}
