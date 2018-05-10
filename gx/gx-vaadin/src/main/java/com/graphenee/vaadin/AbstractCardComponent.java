package com.graphenee.vaadin;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.graphenee.vaadin.util.VaadinUtils;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public abstract class AbstractCardComponent<T> extends MVerticalLayout {

	private boolean isBuilt;
	private T entity;
	private MHorizontalLayout toolBar;

	public AbstractCardComponent(T entity) {
		this.setEntity(entity);
	}

	public AbstractCardComponent<T> build() {
		if (!isBuilt) {
			setSizeFull();
			MVerticalLayout cardLayout = new MVerticalLayout().withMargin(false).withSpacing(false);
			addComponentToLayout(cardLayout);
			addComponent(cardLayout);
			postBuild();
			isBuilt = true;
		}
		return this;
	}

	@PostConstruct
	private void postConstruct() {
		postInitialize();
	}

	protected void postInitialize() {
	}

	protected void postBuild() {
	}

	protected void addComponentToLayout(MVerticalLayout layout) {
		addComponentToLayout(layout, getEntity());
	}

	protected void addComponentToLayout(MVerticalLayout layout, T item) {
	}

	protected boolean shouldShowFooter() {
		return true;
	}

	public AbstractCardComponent<T> buildFooter(T item) {
		HorizontalLayout footer = new HorizontalLayout();
		HorizontalLayout toolbar = getToolbar(entity);
		addButtonsToFooter(toolbar);
		if (shouldShowDeleteButton() || shouldShowEditButton() || toolbar.getComponentCount() > 1) {
			footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
		}
		footer.addStyleName("popup-footer");
		footer.setWidth(100.0f, Unit.PERCENTAGE);
		footer.addComponentAsFirst(toolbar);
		footer.setComponentAlignment(toolbar, Alignment.MIDDLE_RIGHT);
		addComponent(footer);
		return this;
	}

	public HorizontalLayout getToolbar(T entity) {
		toolBar = new MHorizontalLayout().withDefaultComponentAlignment(Alignment.MIDDLE_LEFT).withFullWidth();
		MLabel blankLabel = new MLabel("").withWidth("1px");
		toolBar.addComponentAsFirst(blankLabel);
		toolBar.setExpandRatio(blankLabel, 1);
		return toolBar;
	}

	protected void addButtonsToFooter(HorizontalLayout footerToolbar) {
	}

	public AbstractCardComponent<T> withDeleteButton(Button deleteButton) {
		if (shouldShowDeleteButton()) {
			if (toolBar != null && deleteButton != null) {
				deleteButton.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
				deleteButton.addStyleName(ValoTheme.BUTTON_SMALL);
				toolBar.addComponent(deleteButton);
			}
		}
		return this;
	}

	public AbstractCardComponent<T> withEditButton(Button editButton) {
		if (shouldShowEditButton()) {
			if (toolBar != null && editButton != null) {
				editButton.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
				editButton.addStyleName(ValoTheme.BUTTON_SMALL);
				toolBar.addComponent(editButton);
			}
		}
		return this;
	}

	protected boolean shouldShowDeleteButton() {
		return true;
	}

	protected boolean shouldShowEditButton() {
		return true;
	}

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	protected String localizedSingularValue(String key) {
		return VaadinUtils.localizedSingularValue(key);
	}

	protected String localizedPluralValue(String key) {
		return VaadinUtils.localizedSingularValue(key);
	}

	protected void localizeRecursively(Component component) {
		VaadinUtils.localizeRecursively(component);
	}

	protected String localizedSingularValue(Locale locale, String key) {
		return VaadinUtils.localizedSingularValue(key);
	}

	protected String localizedPluralValue(Locale locale, String key) {
		return VaadinUtils.localizedSingularValue(key);
	}

	protected void localizeRecursively(Locale locale, Component component) {
		VaadinUtils.localizeRecursively(component);
	}
}
