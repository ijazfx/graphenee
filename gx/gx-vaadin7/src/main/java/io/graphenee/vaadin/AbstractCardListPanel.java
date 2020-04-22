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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MPanel;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.gx.theme.graphenee.GrapheneeTheme;
import io.graphenee.vaadin.util.VaadinUtils;

public abstract class AbstractCardListPanel<T> extends MPanel {

	private static final Logger L = LoggerFactory.getLogger(AbstractCardListPanel.class);

	private Class<T> entityClass;
	private boolean isBuilt;
	private Component toolbar;
	private TRAbstractForm<T> cachedForm;
	private MButton addButton;
	private Object filter;
	private Function<T, Boolean> onItemClick;

	private Function<Collection<T>, Boolean> onSelection;

	private boolean isSelectionEnabled = true;

	private AbstractEntityListPanelDelegate delegate = null;

	private CssLayout contentLayout;

	private boolean rootLayoutMargin = false;

	private MVerticalLayout rootLayout;

	public AbstractCardListPanel(Class<T> entityClass) {
		this.entityClass = entityClass;
		contentLayout = new CssLayout();
		contentLayout.setSizeFull();
		contentLayout.setPrimaryStyleName("card-collection");
		if (!isSpringComponent()) {
			postConstruct();
		}
	}

	@Override
	public void setPrimaryStyleName(String style) {
		contentLayout.setPrimaryStyleName(style);
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

	public AbstractCardListPanel<T> build() {
		if (!isBuilt) {
			setSizeFull();
			setStyleName(ValoTheme.PANEL_BORDERLESS);
			setCaption(panelCaption());

			rootLayout = new MVerticalLayout().withMargin(rootLayoutMargin);
			if (shouldShowToolbar()) {
				toolbar = buildToolbar();
				rootLayout.addComponent(toolbar);
			}
			rootLayout.addComponent(contentLayout);
			//			rootLayout.setExpandRatio(contentLayout, 1);
			//			rootLayout.setHeightUndefined();
			setContent(rootLayout);
			//			setHeightUndefined();

			postBuild();
			isBuilt = true;
		}
		return this;
	}

	protected boolean shouldShowToolbar() {
		return true;
	}

	protected boolean shouldShowFooter() {
		return true;
	}

	private Component buildToolbar() {
		MVerticalLayout layout = new MVerticalLayout().withDefaultComponentAlignment(Alignment.TOP_LEFT).withFullWidth().withMargin(false).withSpacing(true);
		addButton = new MButton(FontAwesome.PLUS, localizedSingularValue(addButtonCaption()), event -> {
			try {
				onAddButtonClick(initializeEntity(entityClass.newInstance()));
			} catch (Exception e) {
				L.warn(e.getMessage(), e);
			}
		}).withStyleName(ValoTheme.BUTTON_PRIMARY);
		layout.add(addButton);
		addButtonsToToolbar(layout);
		Iterator<Component> iter = layout.iterator();
		boolean shouldExpand = true;
		while (iter.hasNext()) {
			Component component = iter.next();
			if (layout.getExpandRatio(component) >= 1) {
				shouldExpand = false;
				break;
			}
		}
		if (shouldExpand) {
			try {
				layout.setExpandRatio(addButton, 1.0f);
			} catch (Exception e) {
			}
		}

		// localize...
		localizeRecursively(layout);

		return layout;
	}

	protected String addButtonCaption() {
		return "Add";
	}

	protected void preEdit(T item) {
	}

	protected void onAddButtonClick(T entity) {
		editItem(entity);
	}

	protected void setAddButtonVisibility(boolean visibility) {
		if (addButton != null) {
			addButton.setVisible(visibility);
		}
	}

	protected void editItem(T entity) {
		preEdit(entity);
		openEditorForm(entity);
	}

	protected boolean shouldShowDeleteConfirmation() {
		return false;
	}

	private void openEditorForm(T item) {
		if (cachedForm() != null) {
			cachedForm().setEntity(entityClass, item);
			cachedForm().setSavedHandler(entity -> {
				try {
					if (onSaveEntity(entity)) {
						if (delegate != null) {
							delegate.onSave(entity);
						}
						cachedForm().closePopup();
						refresh();
					}
				} catch (Exception e) {
					L.warn(e.getMessage(), e);
				}
			});
			cachedForm().openInModalPopup();
		}
	}

	protected T initializeEntity(T item) {
		return item;
	}

	protected abstract boolean onSaveEntity(T entity);

	protected abstract boolean onDeleteEntity(T entity);

	public AbstractCardListPanel<T> refresh() {
		if (filter != null) {
			return refresh(filter);
		}
		UI.getCurrent().access(() -> {
			List<T> entities = fetchEntities();
			buildCardList(entities);
			UI.getCurrent().push();
		});
		return this;
	}

	public <F extends Object> AbstractCardListPanel<T> refresh(F filter) {
		this.filter = filter;
		UI.getCurrent().access(() -> {
			List<T> entities = fetchEntities(filter);
			buildCardList(entities);
			UI.getCurrent().push();
		});
		return this;
	}

	private void buildCardList(List<T> entities) {
		if (entities.isEmpty()) {
			contentLayout.removeAllComponents();
			MPanel cardPanel = new MPanel().withStyleName(cardStyleName());
			cardPanel.setContent(new MVerticalLayout(new MLabel("No data available").withStyleName(ValoTheme.LABEL_NO_MARGIN)));
			contentLayout.addComponent(cardPanel);
		} else {
			contentLayout.removeAllComponents();
			if (entities != null) {
				entities.forEach(entity -> {
					MPanel cardPanel = new MPanel().withStyleName(cardStyleName());
					MButton editButton = new MButton(FontAwesome.PENCIL, localizedSingularValue("Edit"), event -> {
						preEdit(entity);
						openEditorForm(entity);
					});
					MButton deleteButton = new MButton(FontAwesome.TRASH, localizedSingularValue("Delete"), event -> {
						if (shouldShowDeleteConfirmation()) {
							ConfirmDialog.show(UI.getCurrent(), "Are you sure to delete selected records", e -> {
								if (e.isConfirmed()) {
									if (onDeleteEntity(entity)) {
										contentLayout.removeComponent(cardPanel);
										if (delegate != null) {
											delegate.onDelete(entity);
										}
									}
								}
							});
						} else {
							if (onDeleteEntity(entity)) {
								contentLayout.removeComponent(cardPanel);
								if (delegate != null) {
									delegate.onDelete(entity);
								}
							}
						}

					});
					AbstractCardComponent<T> cardLayout = getCardComponent(entity).withEditButton(editButton).withDeleteButton(deleteButton);
					cardPanel.setContent(cardLayout.build());
					cardPanel.setWidth(cardLayout.getCardWidth());
					contentLayout.addComponent(cardPanel);

				});
				MLabel dummyLabel = new MLabel().withHeight("-1px");
				contentLayout.addComponent(dummyLabel);
			}
		}
	}

	protected void removeCard(AbstractCardComponent<T> card) {
		Component c = card;
		while (c.getParent() != null) {
			if (contentLayout.getComponentIndex(c) != -1)
				break;
			c = c.getParent();
		}
		if (c != null) {
			contentLayout.removeComponent(c);
		}
	}

	protected abstract AbstractCardComponent<T> getCardComponent(T entity);

	protected abstract String panelCaption();

	protected abstract List<T> fetchEntities();

	protected <F extends Object> List<T> fetchEntities(F filter) {
		return fetchEntities();
	}

	protected abstract TRAbstractForm<T> editorForm();

	private TRAbstractForm<T> cachedForm() {
		if (cachedForm == null) {
			cachedForm = editorForm();
		}
		return cachedForm;
	}

	protected void postBuild() {
	}

	protected void addButtonsToToolbar(AbstractOrderedLayout toolbar) {
	}

	public <R extends AbstractCardListPanel<T>> R withItemClick(Function<T, Boolean> onItemClick) {
		this.onItemClick = onItemClick;
		return (R) this;
	}

	public <R extends AbstractCardListPanel<T>> R withSelection(Function<Collection<T>, Boolean> onSelection) {
		this.onSelection = onSelection;
		return (R) this;
	}

	public AbstractCardListPanel<T> withToolbarVisibility(boolean visibility) {
		toolbar.setVisible(visibility);
		return this;
	}

	public AbstractCardListPanel<T> withSelectionEnabled(boolean isSelectionEnabled) {
		this.isSelectionEnabled = isSelectionEnabled;
		return this;
	}

	public AbstractCardListPanel<T> withDelegate(AbstractEntityListPanelDelegate delegate) {
		this.delegate = delegate;
		return this;
	}

	public void setDelegate(AbstractEntityListPanelDelegate delegate) {
		this.delegate = delegate;
	}

	public static interface AbstractEntityListPanelDelegate {

		default void onSave(Object entity) {
		}

		default void onDelete(Object entity) {
		}
	}

	public AbstractCardListPanel<T> clearFilter() {
		filter = null;
		return this;
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

	public AbstractCardListPanel<T> withMargin(boolean margins) {
		this.rootLayoutMargin = margins;
		if (rootLayout != null)
			rootLayout.setMargin(margins);
		return this;
	}

	public void showMargin() {
		rootLayoutMargin = true;
		rootLayout.setMargin(true);
	}

	public void hideMargin() {
		rootLayoutMargin = false;
		rootLayout.setMargin(false);
	}

	public void showToolbar() {
		toolbar.setVisible(true);
	}

	public void hideToolbar() {
		toolbar.setVisible(false);
	}

	protected String cardStyleName() {
		return GrapheneeTheme.STYLE_CARD_ITEM;
	}

}
