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

import java.util.Iterator;

import javax.annotation.PostConstruct;

import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MPanel;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractSingleComponentContainer;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public abstract class TRAbstractPanel extends MPanel {

	protected boolean isBuilt;
	private Component layout;
	private Component content;
	protected Window window;

	public TRAbstractPanel() {
		setCaption(panelTitle());
		setHeight(100.0f, Unit.PERCENTAGE);
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

	protected void postInitialize() {
	}

	protected void postBuild() {
	}

	protected void refresh() {
	}

	public TRAbstractPanel build() {
		if (!isBuilt) {
			setSizeFull();
			setStyleName(ValoTheme.PANEL_BORDERLESS);
			setCaption(null);

			content = createContent();

			MVerticalLayout layout = new MVerticalLayout().withMargin(false).withSpacing(false);
			layout.setSizeFull();
			layout.addComponents(content);
			setContent(layout);

			postBuild();
			isBuilt = true;
		}
		return this;
	}

	private Component buildFooter() {
		MHorizontalLayout layout = new MHorizontalLayout().withFullWidth().withMargin(false).withSpacing(true);
		layout.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
		layout.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
		addButtonsToFooter(layout);
		for (int i = 0; i < layout.getComponentCount(); i++) {
			layout.setComponentAlignment(layout.getComponent(i), footerAlignment());
			// layout.setExpandRatio(layout.getComponent(i), 1);
		}
		MButton dismissButton = new MButton("Dismiss").withListener(event -> {
			closePopup();
		}).withVisible(shouldShowDismissButton());
		layout.add(dismissButton);
		return layout;
	}

	public Alignment footerAlignment() {
		return Alignment.MIDDLE_LEFT;
	}

	protected boolean shouldShowDismissButton() {
		return true;
	}

	protected abstract void addButtonsToFooter(MHorizontalLayout layout);

	protected abstract String panelTitle();

	protected Component createContent() {
		MVerticalLayout content = new MVerticalLayout().withMargin(false).withSpacing(false);
		content.setSizeFull();
		MVerticalLayout contentLayout = new MVerticalLayout().withMargin(false).withSpacing(false).withFullWidth();
		contentLayout.setSizeFull();
		addComponentsToContentLayout(contentLayout);
		Panel detailsWrapper = new Panel(contentLayout);
		detailsWrapper.setSizeFull();
		detailsWrapper.addStyleName(ValoTheme.PANEL_BORDERLESS);
		detailsWrapper.addStyleName("scroll-divider");
		content.addComponent(detailsWrapper);
		content.setExpandRatio(detailsWrapper, 1f);
		content.addComponent(buildFooter());
		return content;
	}

	protected abstract void addComponentsToContentLayout(MVerticalLayout layout);

	public Window openInModalPopup() {
		build();
		window = new Window(panelTitle(), this);
		window.setStyleName("gx-popup");
		UI.getCurrent().addWindow(window);

		window.setModal(isPopupModal());
		window.setClosable(isPopupClosable());
		window.setResizable(isPopupResizable());
		window.setWidth(popupWidth());
		window.setHeight(popupHeight());
		window.setStyleName("popupWindow");
		window.addCloseShortcut(KeyCode.ESCAPE, null);
		//
		//		Responsive.makeResponsive(window);
		window.setCaption(panelTitle());
		focusFirst();
		return window;
	}

	public void closePopup() {
		if (window != null) {
			UI.getCurrent().removeWindow(window);
		}
	}

	public void focusFirst() {
		customFindFieldAndFocus(getContent());
	}

	private boolean customFindFieldAndFocus(Component compositionRoot) {
		if (compositionRoot instanceof AbstractComponentContainer) {
			AbstractComponentContainer cc = (AbstractComponentContainer) compositionRoot;
			Iterator<Component> iterator = cc.iterator();
			while (iterator.hasNext()) {
				Component component = iterator.next();
				if (component instanceof AbstractTextField) {
					AbstractTextField abstractTextField = (AbstractTextField) component;
					abstractTextField.selectAll();
					return true;
				}
				if (component instanceof AbstractField) {
					AbstractField abstractField = (AbstractField) component;
					abstractField.focus();
					return true;
				}
				if (component instanceof AbstractSingleComponentContainer) {
					AbstractSingleComponentContainer container = (AbstractSingleComponentContainer) component;
					if (customFindFieldAndFocus(container.getContent())) {
						return true;
					}
				}
				if (component instanceof AbstractComponentContainer) {
					if (customFindFieldAndFocus(component)) {
						return true;
					}
				}
			}
		} else if (compositionRoot instanceof AbstractSingleComponentContainer) {
			AbstractSingleComponentContainer container = (AbstractSingleComponentContainer) compositionRoot;
			if (customFindFieldAndFocus(container.getContent())) {
				return true;
			}
		}
		return false;
	}

	protected boolean isPopupModal() {
		return true;
	}

	protected boolean isPopupClosable() {
		return true;
	}

	protected boolean isPopupResizable() {
		return true;
	}

	protected String popupWidth() {
		return "400px";
	}

	protected String popupHeight() {
		return "150px";
	}

}
