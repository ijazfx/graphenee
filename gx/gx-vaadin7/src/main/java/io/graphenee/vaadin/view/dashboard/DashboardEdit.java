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
package io.graphenee.vaadin.view.dashboard;

import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Simple name editor Window.
 */
@SuppressWarnings("serial")
public class DashboardEdit extends Window {

	private final MTextField nameField = new MTextField("Name");
	private final DashboardEditListener listener;

	public DashboardEdit(final DashboardEditListener listener, final String currentName) {
		this.listener = listener;
		setCaption("Edit Dashboard");
		setModal(true);
		setClosable(false);
		setResizable(false);
		setWidth(300.0f, Unit.PIXELS);

		addStyleName("edit-dashboard");

		setContent(buildContent(currentName));
	}

	private Component buildContent(final String currentName) {
		MVerticalLayout result = new MVerticalLayout();
		nameField.setValue(currentName);
		nameField.addStyleName("caption-on-left");
		nameField.focus();

		result.addComponent(nameField);
		result.addComponent(buildFooter());

		return result;
	}

	private Component buildFooter() {
		MHorizontalLayout footer = new MHorizontalLayout();
		footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
		footer.setWidth(100.0f, Unit.PERCENTAGE);

		Button cancel = new Button("Cancel");
		cancel.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				close();
			}
		});
		cancel.setClickShortcut(KeyCode.ESCAPE, null);

		Button save = new Button("Save");
		save.addStyleName(ValoTheme.BUTTON_PRIMARY);
		save.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				listener.dashboardNameEdited(nameField.getValue());
				close();
			}
		});
		save.setClickShortcut(KeyCode.ENTER, null);

		footer.addComponents(cancel, save);
		footer.setExpandRatio(cancel, 1);
		footer.setComponentAlignment(cancel, Alignment.TOP_RIGHT);
		return footer;
	}

	public interface DashboardEditListener {
		void dashboardNameEdited(String name);
	}
}
