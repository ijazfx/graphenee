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

import org.jsoup.nodes.Element;

import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;

@SuppressWarnings("serial")
public class DashboardSessionInitListener implements SessionInitListener {

	@Override
	public final void sessionInit(final SessionInitEvent event) throws ServiceException {

		// event.getSession().setAttribute(DashboardEventBus.class, new
		// DashboardEventBus());

		event.getSession().addBootstrapListener(new BootstrapListener() {

			@Override
			public void modifyBootstrapPage(final BootstrapPageResponse response) {
				final Element head = response.getDocument().head();
				head.appendElement("meta").attr("name", "viewport").attr("content", "width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no");
				head.appendElement("meta").attr("name", "apple-mobile-web-app-capable").attr("content", "yes");
				head.appendElement("meta").attr("name", "apple-mobile-web-app-status-bar-style").attr("content", "black-translucent");

				String contextPath = response.getRequest().getContextPath();
				head.appendElement("link").attr("rel", "apple-touch-icon").attr("href", contextPath + "/VAADIN/themes/dashboard/img/app-icon.png");

			}

			@Override
			public void modifyBootstrapFragment(final BootstrapFragmentResponse response) {
			}
		});
	}

}
