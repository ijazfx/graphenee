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

import java.text.DateFormat;
import java.util.ArrayList;
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
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MPanel;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.ui.MNotification;

import com.google.common.base.Strings;
import com.vaadin.addon.contextmenu.ContextMenu;
import com.vaadin.addon.contextmenu.GridContextMenu;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.SelectionEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid.CellReference;
import com.vaadin.ui.Grid.CellStyleGenerator;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.FooterCell;
import com.vaadin.ui.Grid.FooterRow;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.UI;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.core.util.TRCalendarUtil;
import io.graphenee.gx.theme.graphenee.GrapheneeTheme;
import io.graphenee.vaadin.component.ExportDataSpreadSheetComponent;
import io.graphenee.vaadin.event.TRItemClickListener;
import io.graphenee.vaadin.renderer.BooleanRenderer;
import io.graphenee.vaadin.util.VaadinUtils;

public abstract class AbstractEntityListPanel<T> extends MPanel {

	private static final Logger L = LoggerFactory.getLogger(AbstractEntityListPanel.class);

	private Class<T> entityClass;
	private boolean isBuilt;
	private MGrid<T> mainGrid;
	private BeanItemContainer<T> mainGridContainer;
	private AbstractLayout toolbar;
	private AbstractLayout secondaryToolbar;
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

	private DownloadButton exportDataDownloadButton;

	private MVerticalLayout rootLayout;

	private MarginInfo rootLayoutMargin;

	private FooterCell statusBar;

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

			addButton = new MButton(FontAwesome.PLUS, localizedSingularValue("New"), event -> {
				try {
					onAddButtonClick(initializeEntity(entityClass.newInstance()));
				} catch (Exception e) {
					L.warn(e.getMessage(), e);
				}
			}).withStyleName(ValoTheme.BUTTON_PRIMARY);

			editButton = new MButton(FontAwesome.EDIT, localizedSingularValue("Modify"), event -> {
				Collection<T> items = mainGrid.getSelectedRowsWithType();
				if (items.size() == 1) {
					T item = items.iterator().next();
					preEdit(item);
					openEditorForm(item);
				}
			});
			editButton.setEnabled(false);
			deleteButton = new MButton(FontAwesome.REMOVE, localizedSingularValue("Remove"), event -> {
				Collection<T> issues = mainGrid.getSelectedRowsWithType();
				if (issues.size() > 0) {
					if (shouldShowDeleteConfirmation()) {
						ConfirmDialog.show(UI.getCurrent(), "Are you sure to remove selected records?", e -> {
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
										if (e1.getMessage().contains("ConstraintViolationException"))
											MNotification.tray("Operation Denied", "Record is in use therefore cannot be removed.");
										else
											MNotification.tray("Operation Failed", e1.getMessage());
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
			deleteButton.withStyleName(ValoTheme.BUTTON_DANGER);
			deleteButton.setEnabled(false);
			searchButton = new MButton(FontAwesome.SEARCH, localizedSingularValue("Search"), event -> {
				try {
					searchForm.openInModalPopup();
				} catch (Exception e) {
					L.warn(e.getMessage(), e);
				}
			});
			exportDataSpreadSheetComponent = new ExportDataSpreadSheetComponent();
			exportDataSpreadSheetComponent.withColumnsCaptions(() -> {
				return getGridHeaderCaptionList();
			});
			exportDataSpreadSheetComponent.withDataColumns(() -> {
				List<String> columnList = new ArrayList<>();
				entityGrid().getColumns().forEach(column -> {
					if (!column.isHidden()) {
						columnList.add(column.getPropertyId().toString());
					}
				});
				return columnList;
			}).withDataItems(() -> {
				Collection<Object> selectedRows = entityGrid().getSelectedRows();
				if (selectedRows.size() > 0) {
					return selectedRows;
				}
				return new ArrayList<>(mainGridContainer.getItemIds());
			});
			exportDataDownloadButton = exportDataSpreadSheetComponent.getDownloadButton();
			exportDataDownloadButton.setVisible(shouldShowExportDataButton());

			toolbar = buildToolbar();
			if (toolbar.getComponentCount() == 0)
				toolbar.setVisible(false);

			secondaryToolbar = buildSecondaryToolbar();
			if (secondaryToolbar.getComponentCount() == 0)
				secondaryToolbar.setVisible(false);

			rootLayout = new MVerticalLayout();
			showMargin();
			rootLayout.setSizeFull();
			rootLayout.addComponents(toolbar, secondaryToolbar, mainGrid);
			rootLayout.setExpandRatio(mainGrid, 1);
			setContent(rootLayout);

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
			gridCellFilter.addCellFilterChangedListener(event -> {
				if (entityGrid().getSelectedRows().size() > 0) {
					statusBar.setText(String.format("%d out of %d records selected", entityGrid().getSelectedRows().size(), mainGridContainer.size()));
				} else {
					statusBar.setText(String.format("%d records", mainGridContainer.size()));
				}
			});
			if (visibleProperties != null) {
				addCellFiltersForVisibleProperties(gridCellFilter, visibleProperties);
			}
		}

		grid.getColumns().forEach(column -> {
			column.setHidable(true);
			applyRendererForColumn(column);
		});

		grid.setSelectionMode(isSelectionEnabled ? SelectionMode.MULTI : SelectionMode.NONE);
		grid.addItemClickListener(new TRItemClickListener() {

			@Override
			public void onItemClick(ItemClickEvent event) {
				if (event.getPropertyId() != null) {
					BeanItem<T> item = mainGridContainer.getItem(event.getItemId());
					if (item != null) {
						if (onItemClick != null) {
							Boolean value = onItemClick.apply(item.getBean());
							if (value != null && value == true) {
								onGridItemClicked(item.getBean(), event.getPropertyId() != null ? event.getPropertyId().toString() : "");
							}
						} else {
							onGridItemClicked(item.getBean(), event.getPropertyId() != null ? event.getPropertyId().toString() : "");
						}
					}
				}
			}
		});
		grid.addSelectionListener(event -> {
			if (event.getSelected() != null && !event.getSelected().isEmpty()) {
				statusBar.setText(String.format("%d out of %d records selected", event.getSelected().size(), mainGridContainer.size()));
			} else {
				statusBar.setText(String.format("%d records", mainGridContainer.size()));
			}
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

		GridContextMenu contextMenu = new GridContextMenu(grid);
		contextMenu.addGridBodyContextMenuListener(event -> {
			event.getContextMenu().removeItems();
			addMenuItemsToContextMenu(contextMenu, (T) event.getItemId(), grid.getSelectedRowsWithType());
		});

		FooterRow footerRow = grid.appendFooterRow();
		String[] props = visibleProperties();
		if (props.length > 1)
			statusBar = footerRow.join((String[]) visibleProperties());
		else
			statusBar = footerRow.getCell(props[0]);
		return grid;
	}

	protected void addMenuItemsToContextMenu(ContextMenu contextMenu, T item, Collection<T> selectedItems) {
	}

	protected String generateCellStyle(CellReference cell) {
		return null;
	}

	protected void applyRendererForColumn(Column column) {
		if (column.getPropertyId() != null) {
			if (column.getPropertyId().toString().matches("(is|should|has)[A-Z].*")) {
				column.setMaximumWidth(150);
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
		return TRCalendarUtil.dateFormatter;
	}

	protected DateFormat applyDateTimeFormatForProperty(String propertyId) {
		return TRCalendarUtil.dateTimeFormatter;
	}

	protected Alignment alignmentForProperty(String propertyId) {
		if (propertyId.matches("(is|should|has)[A-Z].*")) {
			return Alignment.MIDDLE_LEFT;
		}
		if (propertyId.matches("(cost|price|sum|amount|percent)")) {
			return Alignment.MIDDLE_RIGHT;
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
		return false;
	}

	private AbstractLayout buildToolbar() {
		MHorizontalLayout layout = new MHorizontalLayout().withStyleName("toolbar").withDefaultComponentAlignment(Alignment.BOTTOM_LEFT).withFullWidth().withMargin(false)
				.withSpacing(true);

		layout.add(addButton);
		layout.add(editButton);
		layout.add(deleteButton);
		layout.add(searchButton);
		searchButton.setVisible(false);
		layout.add(exportDataDownloadButton);
		addButtonsToToolbar(layout);

		boolean addSpacer = true;
		Iterator<Component> iter = layout.iterator();
		while (iter.hasNext()) {
			Component c = iter.next();
			if (layout.getExpandRatio(c) > 0) {
				addSpacer = false;
				break;
			}
		}

		if (addSpacer) {
			MLabel spacerLabel = new MLabel().withStyleName(ValoTheme.LABEL_NO_MARGIN);
			layout.addComponent(spacerLabel);
			layout.setExpandRatio(spacerLabel, 1);
		}

		// localize...
		localizeRecursively(layout);
		VaadinUtils.applyStyleRecursively(layout, "small");

		return layout;
	}

	private AbstractLayout buildSecondaryToolbar() {
		MHorizontalLayout layout = new MHorizontalLayout().withStyleName("toolbar").withDefaultComponentAlignment(Alignment.BOTTOM_LEFT).withFullWidth().withMargin(false)
				.withSpacing(true);

		addButtonsToSecondaryToolbar(layout);

		boolean addSpacer = true;
		Iterator<Component> iter = layout.iterator();

		layout.setVisible(layout.getComponentCount() > 0);

		while (iter.hasNext()) {
			Component c = iter.next();
			if (layout.getExpandRatio(c) > 0) {
				addSpacer = false;
				break;
			}
		}

		if (addSpacer) {
			MLabel spacerLabel = new MLabel().withStyleName(ValoTheme.LABEL_NO_MARGIN);
			layout.addComponent(spacerLabel);
			layout.setExpandRatio(spacerLabel, 1);
		}

		// localize...
		localizeRecursively(layout);
		VaadinUtils.applyStyleRecursively(layout, "small");

		return layout;
	}

	private List<String> getGridHeaderCaptionList() {
		List<String> columnList = new ArrayList<>();
		entityGrid().getColumns().forEach(column -> {
			if (!column.isHidden()) {
				columnList.add(column.getHeaderCaption());
			}
		});
		return columnList;
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
		if (editorForm() != null) {
			// BeanItem<T> beanItem = mainGridContainer.getItem(item);
			// if (beanItem == null) {
			// cachedForm().setEntity(entityClass, item);
			// } else {
			// cachedForm().setEntity(entityClass, beanItem.getBean());
			// }
			editorForm().setEntity(entityClass, item);
			editorForm().setSavedHandler(entity -> {
				try {
					if (onSaveEntity(entity)) {
						if (delegate != null) {
							delegate.onSave(entity);
						}
						editorForm().closePopup();
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
			editorForm().openInModalPopup();
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
			if (entities != null) {
				mainGridContainer.addAll(entities);
			}
			statusBar.setText(String.format("%d records", mainGridContainer.size()));
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
			statusBar.setText(String.format("%d records", mainGridContainer.size()));
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

	protected void addButtonsToSecondaryToolbar(AbstractOrderedLayout toolbar) {
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

	public AbstractEntityListPanel<T> withMargin(boolean margins) {
		this.rootLayoutMargin = null;
		return this;
	}

	public AbstractEntityListPanel<T> withMargin(MarginInfo marginInfo) {
		this.rootLayoutMargin = marginInfo;
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

	public void showToolbar() {
		toolbar.setVisible(true);
	}

	public void hideToolbar() {
		toolbar.setVisible(false);
	}

	public void setSelectionMode(SelectionMode mode) {
		entityGrid().setSelectionMode(mode);
	}

	public void showSecondaryToolbar() {
		secondaryToolbar.setVisible(true);
	}

	public void hideSecondaryToolbar() {
		secondaryToolbar.setVisible(false);
	}

	public void showMargin() {
		if (rootLayoutMargin != null)
			rootLayout.setMargin(rootLayoutMargin);
		else
			rootLayout.setMargin(true);
	}

	public void hideMargin() {
		rootLayout.setMargin(false);
	}

	public MButton getAddButton() {
		return addButton;
	}

	public MButton getEditButton() {
		return editButton;
	}

	public MButton getDeleteButton() {
		return deleteButton;
	}

}
