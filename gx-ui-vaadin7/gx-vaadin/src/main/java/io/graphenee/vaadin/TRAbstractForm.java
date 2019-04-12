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

import org.vaadin.viritin.button.MButton;

import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public abstract class TRAbstractForm<T> extends TRAbstractBaseForm<T> {

	private Image entityImage;
	private Component detailsForm;
	private MButton dismissButton;

	protected boolean isBuilt;
	private Component cachedComponent;
	private boolean footerVisibility = true;
	private HorizontalLayout footer;

	public TRAbstractForm() {
		setHeight(100.0f, Unit.PERCENTAGE);
		setEagerValidation(eagerValidationEnabled());
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

	public TRAbstractForm<T> build() {
		if (!isBuilt) {
			setCaption(formTitle());

			postBuild();
			isBuilt = true;
		}
		return this;
	}

	protected void postBuild() {
	}

	protected abstract boolean eagerValidationEnabled();

	private Component buildFooter() {
		if (footer == null) {
			footer = new HorizontalLayout();
			HorizontalLayout toolbar = getToolbar();
			dismissButton = new MButton("Dismiss").withListener(event -> {
				onDismissButtonClick();
			}).withVisible(shouldShowDismissButton());
			toolbar.addComponent(dismissButton);
			footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
			footer.addStyleName("popup-footer");
			footer.setWidth(100.0f, Unit.PERCENTAGE);
			footer.addComponent(toolbar);
			footer.setComponentAlignment(toolbar, Alignment.MIDDLE_RIGHT);
			footer.setVisible(shouldShowFooter());
		}
		return footer;
	}

	protected void onDismissButtonClick() {
		closePopup();
	}

	protected boolean shouldShowDismissButton() {
		return true;
	}

	protected Component getFormComponent() {
		return this.getFormComponent(getEntity());
	}

	protected Component getFormComponent(T entity) {
		HorizontalLayout details = new HorizontalLayout();
		details.setWidth(100.0f, Unit.PERCENTAGE);
		details.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
		details.setMargin(true);
		details.setSpacing(true);

		if (entityImage != null) {
			Image coverImage = new Image(entityImage.getCaption(), entityImage.getSource());
			coverImage.addStyleName("cover");
			details.addComponent(coverImage);
		}

		detailsForm = buildDetailsForm();
		details.addComponent(detailsForm);
		details.setExpandRatio(detailsForm, 1);

		return details;
	}

	private Component buildDetailsForm() {
		FormLayout form = new FormLayout();
		form.setStyleName(ValoTheme.FORMLAYOUT_LIGHT);
		form.setSpacing(false);
		form.setMargin(false);
		addFieldsToForm(form);
		return form;
	}

	protected void addFieldsToForm(FormLayout form) {
		addFieldsToForm(form, getEntity());
	}

	protected void addFieldsToForm(FormLayout form, T entity) {
	}

	protected abstract String formTitle();

	public void setEntityImage(Image entityImage) {
		this.entityImage = entityImage;
	};

	@Override
	protected Component createContent() {
		VerticalLayout content = new VerticalLayout();
		content.setSizeFull();
		Panel detailsWrapper = new Panel(getFormComponent());
		detailsWrapper.setSizeFull();
		detailsWrapper.addStyleName(ValoTheme.PANEL_BORDERLESS);
		detailsWrapper.addStyleName("scroll-divider");
		content.addComponent(detailsWrapper);
		content.setExpandRatio(detailsWrapper, 1f);
		content.addComponent(buildFooter());
		return content;
	}

	@Override
	public Window openInModalPopup() {
		if (formTitle() != null)
			setModalWindowTitle(formTitle());
		Window window = super.openInModalPopup();
		window.setResizable(isPopupResizable());
		window.setResizeLazy(true);
		window.setClosable(isPopupClosable());
		window.setModal(isPopupModal());
		window.setWidth(popupWidth());
		window.setHeight(popupHeight());
		window.setStyleName("popupWindow");
		Responsive.makeResponsive(window);
		return window;
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
		return "600px";
	}

	protected String popupHeight() {
		return "500px";
	}

	public void hideFooter() {
		footerVisibility = false;
		if (footer != null)
			footer.setVisible(false);
	}

	public void showFooter() {
		footerVisibility = true;
		if (footer != null)
			footer.setVisible(true);
	}

	protected boolean shouldShowFooter() {
		return footerVisibility;
	}

}
