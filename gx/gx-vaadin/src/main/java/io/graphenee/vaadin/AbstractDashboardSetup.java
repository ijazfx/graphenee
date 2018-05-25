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

import java.io.Serializable;
import java.util.List;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractSingleComponentContainer;
import com.vaadin.ui.Image;

import io.graphenee.core.exception.AuthenticationFailedException;
import io.graphenee.core.model.bean.GxSupportedLocaleBean;
import io.graphenee.i18n.api.LocalizerService;
import io.graphenee.vaadin.domain.DashboardUser;
import io.graphenee.vaadin.event.DashboardEvent.UserLoginRequestedEvent;
import io.graphenee.vaadin.view.DashboardMenu;
import io.graphenee.vaadin.view.LoginComponent;
import io.graphenee.vaadin.view.MainComponent;

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

	public boolean shouldShowPoweredByGraphenee() {
		return true;
	}

}
