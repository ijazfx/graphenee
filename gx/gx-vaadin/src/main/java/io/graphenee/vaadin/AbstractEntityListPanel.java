/*******************************************************************************
 * Copyright (c) 2016, 2017, Graphenee
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.gridutil.cell.GridCellFilter;
import org.vaadin.viritin.button.DownloadButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MPanel;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.ui.MNotification;

import com.google.common.base.Strings;
import com.graphenee.core.util.TRCalenderUtil;
import com.graphenee.gx.theme.graphenee.GrapheneeTheme;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.SelectionEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid.CellReference;
import com.vaadin.ui.Grid.CellStyleGenerator;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.UI;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.vaadin.component.ExportDataSpreadSheetComponent;
import io.graphenee.vaadin.renderer.BooleanRenderer;
import io.graphenee.vaadin.util.VaadinUtils;

public abstract class AbstractEntityListPanel<T> extends MPanel {

	private static final Logger L = LoggerFactory.getLogger(AbstractEntityListPanel.class);

	private Class<T> entityClass;
	private boolean isBuilt;
	private MGrid<T> mainGrid;
	private BeanItemContainer<T> mainGridContainer;
	private Component toolbar;
	private TRAbstractForm<T> cachedForm;
	private MButton addButton;
	private MButton editButton;
	private MButton deleteButton;
	private MButton searchButton;
	private GridCellFilter gridCellFilter;
	private Object filter;
	private Function<T, Boolean> onItemClick;

	private Function<Collection<T>, Boolean> onSelection;

	private boolean isSelectionEnabled = true;

	private AbstractEntityListPanelDelegate delegate = null;
	private TRAbstractSearchForm<?> searchForm = null;

	private ExportDataSpreadSheetComponent exportDataSpreadSheetComponent;

	public AbstractEntityListPanel(Class<T> entityClass) {
		this.entityClass = entityClass;
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

	public AbstractEntityListPanel<T> build() {
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
			for (String propertyId : visibleProperties()) {
				if (propertyId.contains(".")) {
					mainGridContainer.addNestedContainerProperty(propertyId);
				}
			}
			grid.withProperties(visibleProperties());
		}
		if (isGridCellFilterEnabled()) {
			gridCellFilter = new GridCellFilter(grid);
			if (visibleProperties != null) {
				addCellFiltersForVisibleProperties(gridCellFilter, visibleProperties);
			}
		}

		grid.getColumns().forEach(column -> {
			applyRendererForColumn(column);
		});

		grid.setSelectionMode(isSelectionEnabled ? SelectionMode.MULTI : SelectionMode.NONE);
		grid.addItemClickListener(event -> {
			if (event.getPropertyId() != null) {
				BeanItem<T> item = mainGridContainer.getItem(event.getItemId());
				if (onItemClick != null) {
					Boolean value = onItemClick.apply(item.getBean());
					if (value != null && value == true) {
						onGridItemClicked(item.getBean(), event.getPropertyId() != null ? event.getPropertyId().toString() : "");
					}
				} else {
					onGridItemClicked(item.getBean(), event.getPropertyId() != null ? event.getPropertyId().toString() : "");
				}
			}
		});
		grid.addSelectionListener(event -> {
			if (onSelection != null) {
				Boolean value = onSelection.apply((Collection<T>) event.getSelected());
				if (value != null && value == true) {
					onGridItemSelect(event);
				}
			} else {
				onGridItemSelect(event);
			}
		});

		grid.getColumns().forEach(column -> {
			if (column.getPropertyId() != null) {
				column.setHeaderCaption(localizedSingularValue(column.getHeaderCaption()));
			}
		});

		grid.setCellStyleGenerator(new CellStyleGenerator() {

			@Override
			public String getStyle(CellReference cell) {
				if (cell.getPropertyId() == null) {
					return GrapheneeTheme.STYLE_V_ALIGN_CENTER;
				}
				String cellStyle = generateCellStyle(cell);
				Alignment alignment = alignmentForProperty(cell.getPropertyId().toString());

				if (Strings.isNullOrEmpty(cellStyle)) {
					if (alignment.isLeft()) {
						return GrapheneeTheme.STYLE_V_ALIGN_LEFT;
					}
					if (alignment.isRight()) {
						return GrapheneeTheme.STYLE_V_ALIGN_RIGHT;
					}
					return GrapheneeTheme.STYLE_V_ALIGN_CENTER;
				} else {
					if (alignment.isLeft()) {
						return GrapheneeTheme.STYLE_V_ALIGN_LEFT + " " + cellStyle;
					}
					if (alignment.isRight()) {
						return GrapheneeTheme.STYLE_V_ALIGN_RIGHT + " " + cellStyle;
					}
					return GrapheneeTheme.STYLE_V_ALIGN_CENTER + " " + cellStyle;
				}
			}
		});

		return grid;
	}

	protected String generateCellStyle(CellReference cell) {
		return null;
	}

	protected void applyRendererForColumn(Column column) {
		if (column.getPropertyId() != null) {
			if (column.getPropertyId().toString().matches("(is|should|has)[A-Z].*")) {
				column.setRenderer(new BooleanRenderer(event -> {
					if (event.getPropertyId() != null) {
						onGridItemClicked((T) event.getItemId(), event.getPropertyId().toString());
					} else {
						onGridItemClicked((T) event.getItemId(), "");
					}
				}), BooleanRenderer.CHECK_BOX_CONVERTER);
			} else if ((column.getPropertyId().toString().matches("(date|since)") || column.getPropertyId().toString().matches("(date|since)[A-Z].*")
					|| column.getPropertyId().toString().matches(".*[a-z](Date|Since)"))) {
				column.setRenderer(new DateRenderer(applyDateFormatForProperty(column.getPropertyId().toString())));
			} else if ((column.getPropertyId().toString().equalsIgnoreCase("time") || column.getPropertyId().toString().matches("(time)[A-Z].*")
					|| column.getPropertyId().toString().matches(".*[a-z](Time)"))) {
				column.setRenderer(new DateRenderer(applyDateTimeFormatForProperty(column.getPropertyId().toString())));
			}
		}
	}

	protected DateFormat applyDateFormatForProperty(String propertyId) {
		return TRCalenderUtil.dateFormatter;
	}

	protected DateFormat applyDateTimeFormatForProperty(String propertyId) {
		return TRCalenderUtil.dateTimeFormatter;
	}

	protected Alignment alignmentForProperty(String propertyId) {
		if (propertyId.matches("(is|should|has)[A-Z].*")) {
			return Alignment.MIDDLE_CENTER;
		}
		if (propertyId.matches("(cost|price|sum|amount|percent)[A-Z].*")) {
			return Alignment.MIDDLE_RIGHT;
		}
		if (propertyId.matches(".*(Cost|Price|Sum|Amount|Percent)")) {
			return Alignment.MIDDLE_RIGHT;
		}
		return Alignment.MIDDLE_LEFT;
	}

	protected void onGridItemSelect(SelectionEvent event) {
		editButton.setEnabled(event.getSelected().size() == 1);
		deleteButton.setEnabled(event.getSelected().size() > 0);
		if (delegate != null) {
			delegate.onGridItemSelect(event);
		}
	}

	protected void addCellFiltersForVisibleProperties(GridCellFilter filter, String[] visibleProperties) {
		for (String visibleProperty : visibleProperties) {
			filter.setTextFilter(visibleProperty, true, false);
		}
	}

	protected boolean isGridCellFilterEnabled() {
		return true;
	}

	private Component buildToolbar() {
		MHorizontalLayout layout = new MHorizontalLayout().withStyleName("toolbar").withDefaultComponentAlignment(Alignment.BOTTOM_LEFT).withFullWidth().withMargin(false)
				.withSpacing(true);
		addButton = new MButton(FontAwesome.PLUS, localizedSingularValue("Add"), event -> {
			try {
				onAddButtonClick(initializeEntity(entityClass.newInstance()));
			} catch (Exception e) {
				L.warn(e.getMessage(), e);
			}
		}).withStyleName(ValoTheme.BUTTON_PRIMARY);
		layout.add(addButton);

		editButton = new MButton(FontAwesome.PENCIL, localizedSingularValue("Edit"), event -> {
			Collection<T> items = mainGrid.getSelectedRowsWithType();
			if (items.size() == 1) {
				T item = items.iterator().next();
				preEdit(item);
				openEditorForm(item);
			}
		});
		editButton.setEnabled(false);
		layout.add(editButton);

		deleteButton = new MButton(FontAwesome.TRASH, localizedSingularValue("Delete"), event -> {
			Collection<T> issues = mainGrid.getSelectedRowsWithType();
			if (issues.size() > 0) {
				if (shouldShowDeleteConfirmation()) {
					ConfirmDialog.show(UI.getCurrent(), "Are you sure to delete selected records?", e -> {
						if (e.isConfirmed()) {
							for (Iterator<T> itemIterator = issues.iterator(); itemIterator.hasNext();) {
								T item = itemIterator.next();
								try {
									if (onDeleteEntity(item)) {
										mainGridContainer.removeItem(item);
										if (delegate != null) {
											delegate.onDelete(item);
										}
									}
								} catch (Exception e1) {
									MNotification.tray("Delete Failed", "Delete failed because of dependencies");
								}
							}
							// refresh();
							deselectAll();
							mainGrid.refreshAllRows();
						}
					});
				}

				else {
					for (Iterator<T> itemIterator = issues.iterator(); itemIterator.hasNext();) {
						T item = itemIterator.next();
						if (onDeleteEntity(item)) {
							mainGridContainer.removeItem(item);
						}
					}
					// refresh();
					deselectAll();
					mainGrid.refreshAllRows();
				}

			}
		});
		deleteButton.setEnabled(false);
		layout.add(deleteButton);

		searchButton = new MButton(FontAwesome.SEARCH, localizedSingularValue("Search"), event -> {
			try {
				searchForm.openInModalPopup();
			} catch (Exception e) {
				L.warn(e.getMessage(), e);
			}
		});
		layout.add(searchButton);
		searchButton.setVisible(false);

		// layout.setExpandRatio(deleteButton, 1.0f);
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
				layout.setExpandRatio(deleteButton, 1.0f);
			} catch (Exception e) {
				// just in case if layout does not contain deleteButton because
				// user implemented his/her own toolbar.
			}
		}

		exportDataSpreadSheetComponent = new ExportDataSpreadSheetComponent();
		exportDataSpreadSheetComponent.withColumnsCaptions(() -> {
			return getGridHeaderCaptionList();
		});
		exportDataSpreadSheetComponent.withDataColumns(() -> {
			return Arrays.asList(visibleProperties());
		}).withDataItems(() -> {
			Collection<Object> selectedRows = entityGrid().getSelectedRows();
			if (selectedRows.size() > 0) {
				return selectedRows;
			}
			return new ArrayList<>(mainGridContainer.getItemIds());
		});
		DownloadButton exportDataDownloadButton = exportDataSpreadSheetComponent.getDownloadButton();
		exportDataDownloadButton.setVisible(shouldShowExportDataButton());
		layout.add(exportDataDownloadButton);

		// localize...
		localizeRecursively(layout);

		return layout;
	}

	private List<String> getGridHeaderCaptionList() {
		List<String> headerCaptionList = new ArrayList<>();
		mainGrid.getColumns().forEach(column -> {
			if (column.getPropertyId() != null) {
				headerCaptionList.add(column.getHeaderCaption());
			}
		});
		return headerCaptionList;
	}

	protected DownloadButton getDownloadButton() {
		if (exportDataSpreadSheetComponent != null) {
			return exportDataSpreadSheetComponent.getDownloadButton();
		}
		return null;
	}

	protected boolean shouldShowExportDataButton() {
		return false;
	}

	protected void preEdit(T item) {
	}

	protected void onAddButtonClick(T entity) {
		preEdit(entity);
		openEditorForm(entity);
	}

	protected boolean shouldShowDeleteConfirmation() {
		return false;
	}

	private void openEditorForm(T item) {
		if (cachedForm() != null) {
			// BeanItem<T> beanItem = mainGridContainer.getItem(item);
			// if (beanItem == null) {
			// cachedForm().setEntity(entityClass, item);
			// } else {
			// cachedForm().setEntity(entityClass, beanItem.getBean());
			// }
			cachedForm().setEntity(entityClass, item);
			cachedForm().setSavedHandler(entity -> {
				try {
					if (onSaveEntity(entity)) {
						if (delegate != null) {
							delegate.onSave(entity);
						}
						cachedForm().closePopup();
						// UI.getCurrent().access(() -> {
						// if (!mainGridContainer.containsId(entity)) {
						// mainGridContainer.addBean(entity);
						// }
						// mainGrid.clearSortOrder();
						// UI.getCurrent().push();
						// });
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

	public AbstractEntityListPanel<T> refresh() {
		if (filter != null) {
			return refresh(filter);
		}
		UI.getCurrent().access(() -> {
			deselectAll();
			List<T> entities = fetchEntities();
			mainGridContainer.removeAllItems();
			mainGridContainer.addAll(entities);
			UI.getCurrent().push();
		});
		return this;
	}

	public <F extends Object> AbstractEntityListPanel<T> refresh(F filter) {
		this.filter = filter;
		UI.getCurrent().access(() -> {
			deselectAll();
			List<T> entities = postFetch(fetchEntities(filter));
			mainGridContainer.removeAllItems();
			mainGridContainer.addAll(entities);
			UI.getCurrent().push();
		});
		return this;
	}

	protected List<T> postFetch(List<T> fetchedEntities) {
		return fetchedEntities;
	}

	protected abstract String panelCaption();

	protected abstract List<T> fetchEntities();

	protected <F extends Object> List<T> fetchEntities(F filter) {
		return fetchEntities();
	}

	protected abstract String[] visibleProperties();

	protected abstract TRAbstractForm<T> editorForm();

	private TRAbstractForm<T> cachedForm() {
		// if (cachedForm == null) {
		// cachedForm = editorForm();
		// }
		// return cachedForm;
		return editorForm();
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

	protected void setAddButtonEnable(boolean isEnable) {
		if (addButton != null) {
			addButton.setEnabled(isEnable);
		}
	}

	protected void setDeleteButtonEnable(boolean isEnable) {
		if (deleteButton != null) {
			deleteButton.setEnabled(isEnable);
		}
	}

	protected void addButtonsToToolbar(AbstractOrderedLayout toolbar) {
	}

	protected void onGridItemClicked(T item) {
		preEdit(item);
		openEditorForm(item);
	}

	protected void onGridItemClicked(T item, String propertyId) {
		onGridItemClicked(item);
	}

	public <R extends AbstractEntityListPanel<T>> R withItemClick(Function<T, Boolean> onItemClick) {
		this.onItemClick = onItemClick;
		return (R) this;
	}

	public <R extends AbstractEntityListPanel<T>> R withSelection(Function<Collection<T>, Boolean> onSelection) {
		this.onSelection = onSelection;
		return (R) this;
	}

	public AbstractEntityListPanel<T> clearFilter() {
		filter = null;
		return this;
	}

	public AbstractEntityListPanel<T> withToolbarVisibility(boolean visibility) {
		toolbar.setVisible(visibility);
		return this;
	}

	public AbstractEntityListPanel<T> withSelectionEnabled(boolean isSelectionEnabled) {
		this.isSelectionEnabled = isSelectionEnabled;
		mainGrid.setSelectionMode(isSelectionEnabled ? SelectionMode.MULTI : SelectionMode.NONE);
		return this;
	}

	public AbstractEntityListPanel<T> withDelegate(AbstractEntityListPanelDelegate delegate) {
		setDelegate(delegate);
		return this;
	}

	public void setDelegate(AbstractEntityListPanelDelegate delegate) {
		this.delegate = delegate;
	}

	public AbstractEntityListPanel<T> withSearchForm(TRAbstractSearchForm<?> searchForm) {
		setSearchForm(searchForm);
		return this;
	}

	public void setSearchForm(TRAbstractSearchForm<?> searchForm) {
		this.searchForm = searchForm;
		if (searchButton != null) {
			searchButton.setVisible(searchForm != null);
		}
	}

	public static interface AbstractEntityListPanelDelegate {

		default void onGridItemSelect(SelectionEvent event) {
		}

		default void onSave(Object entity) {
		}

		default void onDelete(Object entity) {
		}

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

	public void deselectAll() {
		try {
			entityGrid().deselectAll();
		} catch (Exception e) {
		}
	}

}
