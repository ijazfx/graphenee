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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import io.graphenee.i18n.api.LocalizerService;
import io.graphenee.vaadin.AbstractDashboardView;
import com.vaadin.spring.annotation.SpringView;

@SpringView(name = MainDashboardView.VIEW_NAME)
@Scope("prototype")
public class MainDashboardView extends AbstractDashboardView {

	@Autowired
	LocalizerService localizer;

	@Override
	protected boolean isSpringView() {
		return super.isSpringView();
	}

	@Override
	protected String dashboardTitle() {
		return localizer.getSingularValue("Dashboard");
	}

	@Override
	protected List<Dashlet> dashlets() {
		return null;
	}

}
