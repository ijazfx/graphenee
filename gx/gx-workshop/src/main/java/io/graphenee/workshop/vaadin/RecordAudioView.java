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

import org.vaadin.viritin.navigator.MView;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;

import io.graphenee.vaadin.AbstractDashboardPanel;
import io.graphenee.vaadin.component.RecordAudioComponent;

@SuppressWarnings("serial")
@SpringView(name = RecordAudioView.VIEW_NAME)
public class RecordAudioView extends AbstractDashboardPanel implements MView {

	public static final String VIEW_NAME = "record-audio";

	@Override
	public void enter(ViewChangeEvent event) {
	}

	@Override
	public boolean beforeViewChange(ViewChangeEvent event) {
		return true;
	}

	@Override
	public void afterViewChange(ViewChangeEvent event) {
	}

	@Override
	protected String panelTitle() {
		return "Record Audio";
	}

	@Override
	protected void postInitialize() {
		RecordAudioComponent button = new RecordAudioComponent();
		addComponent(button);
	}

	@Override
	protected boolean shouldShowHeader() {
		return true;
	}

}
