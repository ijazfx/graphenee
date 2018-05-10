/*******************************************************************************
 * Copyright (c) 2016, 2017, Graphenee
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
package com.graphenee.vaadin;

import java.io.Serializable;
import java.util.List;

import com.graphenee.core.exception.AuthenticationFailedException;
import com.graphenee.core.model.bean.GxSupportedLocaleBean;
import com.graphenee.i18n.api.LocalizerService;
import com.graphenee.vaadin.domain.DashboardUser;
import com.graphenee.vaadin.event.DashboardEvent.UserLoginRequestedEvent;
import com.graphenee.vaadin.view.DashboardMenu;
import com.graphenee.vaadin.view.LoginComponent;
import com.graphenee.vaadin.view.MainComponent;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractSingleComponentContainer;
import com.vaadin.ui.Image;

public abstract class AbstractDashboardSetup implements Serializable {

	private static final long serialVersionUID = 1L;

	public abstract String applicationTitle();

	public String dashboardTitle() {
		return applicationTitle();
	}

	public String loginFormTitle() {
		return applicationTitle();
	}

	public abstract Image applicationLogo();

	public Image dashboardLogo() {
		return applicationLogo();
	}

	public Image loginFormLogo() {
		return applicationLogo();
	}

	protected abstract List<TRMenuItem> menuItems();

	protected abstract List<TRMenuItem> profileMenuItems();

	public abstract String dashboardViewName();

	public AbstractSingleComponentContainer loginComponent() {
		return new LoginComponent(this);
	}

	public AbstractMainComponent defaultComponent() {
		return new MainComponent(this).build();
	}

	public AbstractDashboardMenu dashboardMenu() {
		return new DashboardMenu(this).build();
	}

	public AbstractComponent profileComponent() {
		return null;
	}

	public AbstractComponent preferencesComponent() {
		return null;
	}

	public AbstractComponent leftComponent() {
		return null;
	}

	public abstract void registerViewProviders(Navigator navigator);

	public abstract DashboardUser authenticate(UserLoginRequestedEvent event) throws AuthenticationFailedException;

	public List<GxSupportedLocaleBean> supportedLocales() {
		return null;
	}

	public LocalizerService localizer() {
		return null;
	}

	protected boolean shouldLocalize() {
		return false;
	}

	public boolean shouldAccessView(String viewName) {
		return true;
	}

}
