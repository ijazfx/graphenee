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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification.Type;

import io.graphenee.core.exception.AuthenticationFailedException;
import io.graphenee.core.model.GxAbstractCredentials;
import io.graphenee.core.model.GxAuthenticatedUser;
import io.graphenee.core.model.GxUsernamePasswordCredentials;
import io.graphenee.vaadin.ui.GxNotification;
import io.graphenee.vaadin.view.LoginComponent;

@SuppressWarnings("serial")
public abstract class AbstractLoginUI<C extends GxAbstractCredentials, R extends GxAuthenticatedUser> extends AbstractDashboardUI {

	private static final Logger L = LoggerFactory.getLogger(AbstractLoginUI.class);

	@SuppressWarnings("unchecked")
	protected Component createComponent() {
		setStyleName("loginview");
		LoginComponent loginComponent = new LoginComponent(dashboardSetup());
		loginComponent.addLoginListener(event -> {
			String username = event.getLoginParameter(LoginComponent.USERNAME);
			if (username != null)
				username = username.trim();
			String password = event.getLoginParameter(LoginComponent.PASSWORD);
			GxUsernamePasswordCredentials credentials = new GxUsernamePasswordCredentials(username, password);
			try {
				R user = authenticate((C) credentials);
				if (user.isPasswordChangeRequired()) {
					String location = Page.getCurrent().getLocation().toString();
					location = location.replaceAll("/login", "/reset-password");
					Page.getCurrent().setLocation(location);
				} else {
					String log = String.format("Remote IP: %s, Login Succeed for User: %s", getRemoteIpAddress(), username);
					L.warn(log);
					try {
						dashboardSetup().eventBus().unregister(user);
					} catch (Exception ex) {
						// will come here if user is not already registered.
					}
					dashboardSetup().eventBus().register(user);

					VaadinSession.getCurrent().setAttribute(GxAuthenticatedUser.class, user);
					String location = Page.getCurrent().getLocation().toString();
					location = location.replaceAll("/login", "");
					Page.getCurrent().setLocation(location);
				}
			} catch (Exception e) {
				String log = String.format("Remote IP: %s, Login Failed for User: %s", getRemoteIpAddress(), username);
				L.warn(log);
				String loginLink = Page.getCurrent().getLocation().toString();
				String resetPasswordLink = loginLink.replaceAll("/login", "/reset-password");
				StringBuilder sb = new StringBuilder();
				sb.append("If you don't remember your password then <a style=\"color: white;\" href=\"" + resetPasswordLink + "\">Click here!</a> to reset.");
				GxNotification notification = GxNotification.closable("Access Denied", sb.toString(), Type.ERROR_MESSAGE, Position.BOTTOM_CENTER);
				notification.setDelayMsec(5000);
				notification.setHtmlContentAllowed(true);
				notification.show(Page.getCurrent());
			}
		});
		return loginComponent;
	}

	protected abstract R authenticate(C credentials) throws AuthenticationFailedException;

}
