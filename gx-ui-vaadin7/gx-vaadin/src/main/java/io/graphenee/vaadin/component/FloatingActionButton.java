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
package io.graphenee.vaadin.component;

import java.util.List;

import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.themes.ValoTheme;

public class FloatingActionButton extends CssLayout {

	private MButton actionButton;
	private MVerticalLayout actionListLayout;
	private Resource defaultIcon;

	public FloatingActionButton() {
		actionButton = new MButton().withStyleName(ValoTheme.BUTTON_ICON_ONLY, "circle", "action-button").withIcon(FontAwesome.ELLIPSIS_V);
		actionListLayout = new MVerticalLayout().withDefaultComponentAlignment(Alignment.MIDDLE_RIGHT).withSizeUndefined().withStyleName("action-button-action-list");
		actionButton.addClickListener(event -> {
			if (!actionListLayout.getStyleName().contains("action-button-action-list-appear")) {
				defaultIcon = actionButton.getIcon();
				actionButton.setIcon(FontAwesome.PLUS);
				actionButton.addStyleName("action-button-rotate");
				actionListLayout.addStyleName("action-button-action-list-appear");
			} else {
				actionButton.setIcon(defaultIcon);
				actionButton.removeStyleName("action-button-rotate");
				actionListLayout.removeStyleName("action-button-action-list-appear");
			}
		});

		addComponents(actionListLayout, actionButton);
	}

	public void setActionListButtons(List<Button> actionListButtons) {
		actionListLayout.removeAllComponents();
		if (actionListButtons != null) {
			actionListButtons.forEach(button -> {
				button.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
				button.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
				button.addStyleName(ValoTheme.BUTTON_SMALL);
				button.addClickListener(event -> {
					actionButton.setIcon(defaultIcon);
					actionButton.removeStyleName("action-button-rotate");
					actionListLayout.removeStyleName("action-button-action-list-appear");
				});
				actionListLayout.add(button);
			});
		}
		actionListLayout.setVisible(true); // actionListButtons != null &&
		// !actionListButtons.isEmpty());
	}

}
