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

import javax.annotation.PostConstruct;

import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.gridutil.cell.GridCellFilter;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MPanel;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.vaadin.event.DashboardEventBus;
import io.graphenee.vaadin.util.VaadinUtils;

public abstract class AbstractMappedEntityListPanel<T, D> extends MPanel {

	private Class<T> entityClass;
	private Class<D> destClass;
	private boolean isBuilt;
	private MGrid<T> mainGrid;
	private BeanItemContainer<T> mainGridContainer;
	private Component toolbar;
	private TRAbstractForm<D> cachedForm;
	private MButton addButton;
	private MButton editButton;
	private MButton deleteButton;
	private GridCellFilter filter;

	public AbstractMappedEntityListPanel(Class<T> entityClass) {
		this.entityClass = entityClass;
		if (!isSpringComponent()) {
			postConstruct();
		}
	}

	public AbstractMappedEntityListPanel(Class<T> sourceEntityClass, Class<D> destEntityClass) {
		this.entityClass = sourceEntityClass;
		this.destClass = destEntityClass;
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

	public AbstractMappedEntityListPanel<T, D> build() {
		if (!isBuilt) {
			setSizeFull();
			setStyleName(ValoTheme.PANEL_BORDERLESS);
			setCaption(panelCaption());

			mainGrid = buildMainGrid();
			toolbar = buildToolbar();

			MVerticalLayout layout = new MVerticalLayout();
			layout.setSizeFull();
			layout.addComponents(toolbar, mainGrid);
			layout.setExpandRatio(mainGrid, 1);
			setContent(layout);

			postBuild();
			isBuilt = true;
		}
		return this;
	}

	private MGrid<T> buildMainGrid() {
		MGrid<T> grid = new MGrid<>(entityClass);
		mainGridContainer = new BeanItemContainer<>(entityClass);
		grid.setContainerDataSource(mainGridContainer);
		grid.setSizeFull();
		String[] visibleProperties = visibleProperties();
		if (visibleProperties != null) {
			grid.withProperties(visibleProperties());
		}
		if (isGridCellFilterEnabled()) {
			filter = new GridCellFilter(grid);
			if (visibleProperties != null) {
				addCellFiltersForVisibleProperties(filter, visibleProperties);
			}
		}

		grid.setSelectionMode(SelectionMode.MULTI);
		grid.addItemClickListener(event -> {
			if (event.getPropertyId() != null) {
				BeanItem<T> item = mainGridContainer.getItem(event.getItemId());
				onGridItemClicked(item.getBean());
			}
		});
		grid.addSelectionListener(event -> {
			editButton.setEnabled(event.getSelected().size() == 1);
			deleteButton.setEnabled(event.getSelected().size() > 0);
			DashboardEventBus.sessionInstance().post(event.getSelected());
		});
		return grid;
	}

	protected void addCellFiltersForVisibleProperties(GridCellFilter filter, String[] visibleProperties) {
		for (String visibleProperty : visibleProperties) {
			filter.setTextFilter(visibleProperty, true, false);
		}
	}

	protected boolean isGridCellFilterEnabled() {
		return false;
	}

	private Component buildToolbar() {
		MHorizontalLayout layout = new MHorizontalLayout().withDefaultComponentAlignment(Alignment.BOTTOM_LEFT).withFullWidth().withMargin(false).withSpacing(true);
		addButton = new MButton(FontAwesome.PLUS, "Add", event -> {
			try {
				onAddButtonAction(destClass.newInstance());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).withStyleName(ValoTheme.BUTTON_PRIMARY).withVisible(shouldShowAddButton());
		layout.add(addButton);

		editButton = new MButton(FontAwesome.PENCIL, "Edit", event -> {
			Collection<T> items = mainGrid.getSelectedRowsWithType();
			if (items.size() == 1) {
				T item = items.iterator().next();
				onEditButtonAction(item);
			}
		});
		editButton.setEnabled(false);
		layout.add(editButton);

		deleteButton = new MButton(FontAwesome.TRASH, "Delete", event -> {
			Collection<T> issues = mainGrid.getSelectedRowsWithType();
			if (issues.size() > 0) {
				if (shouldShowDeleteConfirmation()) {

					ConfirmDialog.show(UI.getCurrent(), "Are you sure to delete selected records", e -> {
						if (e.isConfirmed()) {
							for (Iterator<T> itemIterator = issues.iterator(); itemIterator.hasNext();) {
								onDeleteEntity(itemIterator.next());
							}
							refresh();

						}
					});
				}

				else {
					for (Iterator<T> itemIterator = issues.iterator(); itemIterator.hasNext();) {
						onDeleteEntity(itemIterator.next());
					}
					refresh();
				}

			}
		});
		deleteButton.setEnabled(false);
		layout.add(deleteButton);
		layout.setExpandRatio(deleteButton, 1.0f);
		addButtonsToToolbar(layout);
		return layout;
	}

	protected boolean shouldShowDeleteConfirmation() {
		return false;
	}

	protected boolean shouldShowAddButton() {
		return true;
	}

	private void openEditorForm(D item) {
		if (cachedForm() != null) {
			cachedForm().setEntity(destClass, item);
			cachedForm().setSavedHandler(entity -> {
				try {
					onSaveEntity(entity);
					cachedForm().closePopup();
					refresh();
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			cachedForm().openInModalPopup();
		}
	}

	protected abstract void onSaveEntity(D entity);

	protected abstract void onDeleteEntity(T entity);

	public AbstractMappedEntityListPanel<T, D> refresh() {
		mainGrid.deselectAll();
		List<T> entities = fetchEntities();
		mainGridContainer.removeAllItems();
		mainGridContainer.addAll(entities);
		return this;
	}

	protected abstract String panelCaption();

	protected abstract List<T> fetchEntities();

	protected abstract String[] visibleProperties();

	protected abstract TRAbstractForm<D> editorForm();

	protected abstract D mapEntity(T t);

	private TRAbstractForm<D> cachedForm() {
		if (cachedForm == null) {
			cachedForm = editorForm();
		}
		return cachedForm;
	}

	protected void postBuild() {
	}

	protected MGrid<T> entityGrid() {
		return mainGrid;
	}

	protected void setAddButtonVisibility(boolean visibility) {
		if (addButton != null) {
			addButton.setVisible(visibility);
		}
	}

	protected void setEditButtonVisibility(boolean visibility) {
		if (editButton != null) {
			editButton.setVisible(visibility);
		}
	}

	protected void setDeleteButtonVisibility(boolean visibility) {
		if (deleteButton != null) {
			deleteButton.setVisible(visibility);
		}
	}

	protected void addButtonsToToolbar(AbstractOrderedLayout toolbar) {
	}

	protected void onAddButtonAction(D item) {
		openEditorForm(item);
	}

	protected void onEditButtonAction(T item) {
		openEditorForm(mapEntity(item));
	}

	protected void onGridItemClicked(T item) {
		openEditorForm(mapEntity(item));
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
