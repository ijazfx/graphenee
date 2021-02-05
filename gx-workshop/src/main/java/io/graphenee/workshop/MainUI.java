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
package io.graphenee.workshop;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Viewport;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;

import io.graphenee.vaadin.AbstractDashboardSetup;
import io.graphenee.vaadin.AbstractDashboardUI;
import io.graphenee.vaadin.GxSecuredUI;
import io.graphenee.workshop.vaadin.WorkshopDashboardSetup;

@SuppressWarnings("serial")
@SpringUI
@Theme("graphenee")
@Push(transport = Transport.WEBSOCKET, value = PushMode.MANUAL)
@Viewport(value = "width=device-width")
@GxSecuredUI
public class MainUI extends AbstractDashboardUI {

	@Autowired
	SpringViewProvider viewProvider;

	@Autowired
	WorkshopDashboardSetup dashboardSetup;

	@Override
	protected AbstractDashboardSetup dashboardSetup() {
		return dashboardSetup;
	}

}
