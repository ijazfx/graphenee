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

import com.google.common.eventbus.EventBus;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Resource;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Image;

import io.graphenee.core.model.bean.GxSupportedLocaleBean;
import io.graphenee.gx.theme.graphenee.GrapheneeTheme;
import io.graphenee.i18n.api.LocalizerService;
import io.graphenee.vaadin.view.DashboardMenu;
import io.graphenee.vaadin.view.MainComponent;

public abstract class AbstractDashboardSetup implements Serializable {

	private static final long serialVersionUID = 1L;

	private EventBus eventBus;

	public abstract String applicationTitle();

	private BaseProfileForm profileForm = null;

	public EventBus eventBus() {
		if (eventBus == null) {
			synchronized (this) {
				if (eventBus == null) {
					eventBus = new EventBus();
				}
			}
		}
		return eventBus;
	}

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

	public AbstractMainComponent defaultComponent() {
		return new MainComponent(this).build();
	}

	public AbstractDashboardMenu dashboardMenu() {
		return new DashboardMenu(this).build();
	}

	public BaseProfileForm profileComponent() {
		if (profileForm == null) {
			profileForm = new BaseProfileForm();
			profileForm.setSavedHandler(event -> {

			});
		}
		return profileForm;
	}

	public AbstractComponent preferencesComponent() {
		return null;
	}

	public AbstractComponent leftComponent() {
		return null;
	}

	public abstract void registerViewProviders(Navigator navigator);

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

	public Resource femaleAvatar() {
		return GrapheneeTheme.AVATAR_FEMALE;
	}

	public Resource maleAvatar() {
		return GrapheneeTheme.AVATAR_MALE;
	}

}
