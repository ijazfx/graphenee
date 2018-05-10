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
package io.graphenee.vaadin;

import javax.annotation.PostConstruct;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbstractSingleComponentContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.vaadin.event.DashboardEventBus;
import io.graphenee.vaadin.event.DashboardEvent.UserLoginRequestedEvent;

@SuppressWarnings("serial")
public abstract class AbstractLoginComponent extends AbstractSingleComponentContainer {

	private boolean isBuilt;

	public AbstractLoginComponent() {
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

	public AbstractLoginComponent build() {
		if (!isBuilt) {
			setSizeFull();
			Component loginForm = buildLoginForm();
			setContent(loginForm);
			postBuild();
			isBuilt = true;
		}
		return this;
	}

	protected void postBuild() {
	}

	private Component buildLoginForm() {
		final VerticalLayout loginPanel = new VerticalLayout();
		loginPanel.setSizeUndefined();
		loginPanel.setSpacing(true);
		Responsive.makeResponsive(loginPanel);
		loginPanel.addStyleName("login-panel");
		loginPanel.addComponent(buildLabels());
		loginPanel.addComponent(buildFields());
		if (isRememberMeEnabled()) {
			loginPanel.addComponent(new CheckBox("Remember me", true));
		}
		return loginPanel;
	}

	protected boolean isRememberMeEnabled() {
		return false;
	}

	private Component buildLabels() {
		CssLayout labels = new CssLayout();
		labels.addStyleName("labels");

		if (dashboardSetup().loginFormLogo() != null) {
			Image logoImage = dashboardSetup().loginFormLogo();
			logoImage.setWidth("50px");
			labels.addComponent(logoImage);
		} else {
			Label welcome = new Label("Welcome");
			welcome.setSizeUndefined();
			welcome.addStyleName(ValoTheme.LABEL_H4);
			welcome.addStyleName(ValoTheme.LABEL_COLORED);
			labels.addComponent(welcome);
		}

		Label title = new Label(dashboardSetup().loginFormTitle());
		title.setSizeUndefined();
		title.addStyleName(ValoTheme.LABEL_H3);
		title.addStyleName(ValoTheme.LABEL_LIGHT);
		labels.addComponent(title);
		return labels;
	}

	private Component buildFields() {
		HorizontalLayout fields = new HorizontalLayout();
		fields.setSpacing(true);
		fields.addStyleName("fields");

		final TextField username = new TextField("Username");
		username.setIcon(FontAwesome.USER);
		username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

		final PasswordField password = new PasswordField("Password");
		password.setIcon(FontAwesome.LOCK);
		password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

		final Button signin = new Button("Sign In");
		signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
		signin.setClickShortcut(KeyCode.ENTER);
		signin.focus();

		fields.addComponents(username, password, signin);
		fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

		signin.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				DashboardEventBus.sessionInstance().post(new UserLoginRequestedEvent(username.getValue(), password.getValue()));
			}
		});
		return fields;
	}

	protected abstract AbstractDashboardSetup dashboardSetup();

	protected void postInitialize() {
	}

}
