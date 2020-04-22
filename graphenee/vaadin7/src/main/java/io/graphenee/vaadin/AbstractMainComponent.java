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

import javax.annotation.PostConstruct;

import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

import io.graphenee.vaadin.event.DashboardEvent.CloseOpenWindowsEvent;
import io.graphenee.vaadin.event.DashboardEvent.PostViewChangeEvent;
import io.graphenee.vaadin.event.DashboardEventBus;
import io.graphenee.vaadin.view.DashboardMenu;

/*
 * Dashboard MainView is a simple HorizontalLayout that wraps the menu on the
 * left and creates a simple container for the navigator on the right.
 */
@SuppressWarnings("serial")
public abstract class AbstractMainComponent extends HorizontalLayout {

	private ComponentContainer componentContainer;
	private DashboardMenu dashboardMenu;
	private boolean isBuilt;
	private MVerticalLayout headerLayout;

	public AbstractMainComponent() {
		if (!isSpringComponent()) {
			postConstruct();
		}
	}

	protected boolean isSpringComponent() {
		return this.getClass().getAnnotation(SpringComponent.class) != null;
	}

	@PostConstruct
	private void postConstruct() {
		postInitialize();
	}

	public AbstractMainComponent build() {
		if (!isBuilt) {
			setSizeFull();
			addStyleName("mainview");

			addComponent(dashboardMenu());
			HorizontalLayout mainLayout = new HorizontalLayout();
			mainLayout.setSizeFull();
			if (leftComponent() != null) {
				mainLayout.addComponent(leftComponent());
			}
			MVerticalLayout rightComponent = new MVerticalLayout().withMargin(false).withSpacing(false).withFullWidth().withFullHeight();
			headerLayout = new MVerticalLayout().withFullWidth().withVisible(shouldShowHeaderLayout());

			ComponentContainer content = new CssLayout();
			content.addStyleName("view-content");
			content.setSizeFull();

			rightComponent.add(headerLayout);
			rightComponent.add(content);
			rightComponent.setExpandRatio(content, 1);
			mainLayout.addComponent(rightComponent);
			mainLayout.setExpandRatio(rightComponent, 1);
			addComponent(mainLayout);
			setExpandRatio(mainLayout, 1.0f);

			componentContainer = content;
			Navigator navigator = new DashboardNavigator(componentContainer);
			dashboardSetup().registerViewProviders(navigator);
			navigator.addViewChangeListener(new ViewChangeListener() {
				@Override
				public boolean beforeViewChange(final ViewChangeEvent event) {
					return dashboardSetup().shouldAccessView(event.getViewName());
				}

				@Override
				public void afterViewChange(final ViewChangeEvent event) {
					DashboardEventBus dashboardEventBus = DashboardEventBus.sessionInstance();
					dashboardEventBus.post(new PostViewChangeEvent(event.getViewName(), event.getParameters()));
					dashboardEventBus.post(new CloseOpenWindowsEvent());
				}
			});
			UI.getCurrent().setNavigator(navigator);
			postBuild();
			isBuilt = true;
		}
		return this;
	}

	protected void postBuild() {
	}

	protected Component dashboardMenu() {
		return dashboardSetup().dashboardMenu();
	}

	protected abstract AbstractDashboardSetup dashboardSetup();

	protected void postInitialize() {
	}

	public MVerticalLayout getHeaderLayout() {
		return headerLayout;
	}

	protected boolean shouldShowHeaderLayout() {
		return false;
	}

	protected Component leftComponent() {
		return null;
	}

}
