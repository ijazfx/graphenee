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
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MPanel;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.ui.MNotification;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MultiSelectMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Grid.CellReference;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TableFieldFactory;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.core.util.TRCalendarUtil;
import io.graphenee.vaadin.component.ExportDataSpreadSheetComponent;
import io.graphenee.vaadin.event.TRItemClickListener;
import io.graphenee.vaadin.util.VaadinUtils;

public abstract class AbstractEntityTablePanel<T> extends MPanel {

	private static final Logger L = LoggerFactory.getLogger(AbstractEntityTablePanel.class);

	private Class<T> entityClass;
	private boolean isBuilt;
	private MTable<T> mainTable;
	private BeanItemContainer<T> mainTableContainer;
	private AbstractLayout toolbar;
	private AbstractLayout secondaryToolbar;
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

	private DownloadButton exportDataDownloadButton;

	private MVerticalLayout rootLayout;

	private boolean rootLayoutMargin = false;

	public AbstractEntityTablePanel(Class<T> entityClass) {
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

	public AbstractEntityTablePanel<T> build() {
		if (!isBuilt) {
			setSizeFull();
			setStyleName(ValoTheme.PANEL_BORDERLESS);
			setCaption(panelCaption());

			mainTable = buildMainTable();

			addButton = new MButton(FontAwesome.PLUS, localizedSingularValue("New"), event -> {
				try {
					onAddButtonClick(initializeEntity(entityClass.newInstance()));
				} catch (Exception e) {
					L.warn(e.getMessage(), e);
				}
			}).withStyleName(ValoTheme.BUTTON_PRIMARY);
			editButton = new MButton(FontAwesome.EDIT, localizedSingularValue("Modify"), event -> {

				Collection<T> items = (Collection<T>) mainTable.getValue();
				if (items.size() == 1) {
					T item = items.iterator().next();
					preEdit(item);
					openEditorForm(item);
				}
			});
			editButton.setEnabled(false);
			deleteButton = new MButton(FontAwesome.REMOVE, localizedSingularValue("Remove"), event -> {
				Collection<T> issues = (Collection<T>) mainTable.getValue();
				if (issues.size() > 0) {
					if (shouldShowDeleteConfirmation()) {
						ConfirmDialog.show(UI.getCurrent(), "Are you sure to remove selected records?", e -> {
							if (e.isConfirmed()) {
								for (Iterator<T> itemIterator = issues.iterator(); itemIterator.hasNext();) {
									T item = itemIterator.next();
									try {
										if (onDeleteEntity(item)) {
											mainTable.removeItem(item);
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
								refresh();
								deselectAll();
								mainTable.refreshRowCache();
							}
						});
					}

					else {
						for (Iterator<T> itemIterator = issues.iterator(); itemIterator.hasNext();) {
							T item = itemIterator.next();
							if (onDeleteEntity(item)) {
								mainTable.removeItem(item);
							}
						}
						refresh();
						deselectAll();
						mainTable.refreshRowCache();
						mainTable.refreshRows();
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
				return Arrays.asList(visibleProperties());
			}).withDataItems(() -> {
				Collection<Object> selectedRows = (Collection<Object>) entityTable().getValue();
				if (selectedRows.size() > 0) {
					return selectedRows;
				}
				return new ArrayList<>(mainTableContainer.getItemIds());
			});
			exportDataDownloadButton = exportDataSpreadSheetComponent.getDownloadButton();
			exportDataDownloadButton.setVisible(shouldShowExportDataButton());

			toolbar = buildToolbar();
			if (toolbar.getComponentCount() == 0)
				toolbar.setVisible(false);

			secondaryToolbar = buildSecondaryToolbar();
			if (secondaryToolbar.getComponentCount() == 0)
				secondaryToolbar.setVisible(false);

			rootLayout = new MVerticalLayout().withMargin(rootLayoutMargin);
			rootLayout.setSizeFull();
			rootLayout.addComponents(toolbar, secondaryToolbar, mainTable);
			rootLayout.setExpandRatio(mainTable, 1);
			setContent(rootLayout);

			for (Object o : mainTable.getVisibleColumns()) {
				if (o != null) {
					applyRendererForColumn(new TableColumn(mainTable, o.toString()));
				}
			}

			postBuild();
			isBuilt = true;
		}
		return this;
	}

	private MTable<T> buildMainTable() {
		MTable<T> table = new MTable<>(entityClass);
		mainTableContainer = new BeanItemContainer<>(entityClass);
		table.setContainerDataSource(mainTableContainer);
		table.setSizeFull();
		String[] visibleProperties = visibleProperties();
		if (visibleProperties != null) {
			for (String propertyId : visibleProperties()) {
				if (propertyId.contains(".")) {
					mainTableContainer.addNestedContainerProperty(propertyId);
				}
			}
			table.withProperties(visibleProperties());
		}
		if (isGridCellFilterEnabled()) {
			// gridCellFilter = new GridCellFilter(table);
			// if (visibleProperties != null) {
			// addCellFiltersForVisibleProperties(gridCellFilter,
			// visibleProperties);
			// }
		}

		if (isSelectionEnabled) {
			table.setMultiSelectMode(MultiSelectMode.DEFAULT);
			table.setSelectable(true);
		} else {
			table.setSelectable(false);
		}
		table.addItemClickListener(new TRItemClickListener() {

			@Override
			public void onItemClick(ItemClickEvent event) {
				if (event.getPropertyId() != null) {
					BeanItem<T> item = mainTableContainer.getItem(event.getItemId());
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
		table.addValueChangeListener(event -> {
			if (onSelection != null) {
				Boolean value = onSelection.apply((Collection<T>) event.getProperty().getValue());
				if (value != null && value == true) {
					onTableItemSelect(event);
				}
			} else {
				onTableItemSelect(event);
			}
		});

		//
		// table.getColumns().forEach(column -> {
		// if (column.getPropertyId() != null) {
		// column.setHeaderCaption(localizedSingularValue(column.getHeaderCaption()));
		// }
		// });
		//
		// table.setCellStyleGenerator(new CellStyleGenerator() {
		//
		// @Override
		// public String getStyle(CellReference cell) {
		// if (cell.getPropertyId() == null) {
		// return GrapheneeTheme.STYLE_V_ALIGN_CENTER;
		// }
		// String cellStyle = generateCellStyle(cell);
		// Alignment alignment =
		// alignmentForProperty(cell.getPropertyId().toString());
		//
		// if (Strings.isNullOrEmpty(cellStyle)) {
		// if (alignment.isLeft()) {
		// return GrapheneeTheme.STYLE_V_ALIGN_LEFT;
		// }
		// if (alignment.isRight()) {
		// return GrapheneeTheme.STYLE_V_ALIGN_RIGHT;
		// }
		// return GrapheneeTheme.STYLE_V_ALIGN_CENTER;
		// } else {
		// if (alignment.isLeft()) {
		// return GrapheneeTheme.STYLE_V_ALIGN_LEFT + " " + cellStyle;
		// }
		// if (alignment.isRight()) {
		// return GrapheneeTheme.STYLE_V_ALIGN_RIGHT + " " + cellStyle;
		// }
		// return GrapheneeTheme.STYLE_V_ALIGN_CENTER + " " + cellStyle;
		// }
		// }
		// });

		table.setTableFieldFactory(new TableFieldFactory() {

			@Override
			public Field<?> createField(Container container, Object itemId, Object propertyId, Component uiContext) {
				if (table.isEditable()) {
					Field<?> field = propertyField((T) itemId, propertyId != null ? propertyId.toString() : "");
					if (field != null) {
						BeanItem<Object> beanItem = new BeanItem<>(itemId);
						final Property<Object> property = beanItem.getItemProperty(propertyId);
						field.setPropertyDataSource(property);
						field.addValueChangeListener(event -> {
							if (field.isValid()) {
								onCellValueChange(event.getProperty().getValue(), (T) itemId, propertyId.toString());
							}
						});
					}
					return field;
				}
				return null;
			}
		});
		table.setEditable(isTableEditable());

		return table;
	}

	protected void onCellValueChange(Object value, T entity, String propertyId) {
	}

	protected boolean isTableEditable() {
		return false;
	}

	protected Field<?> propertyField(T itemId, String propertyId) {
		return null;
	}

	protected String generateCellStyle(CellReference cell) {
		return null;
	}

	protected void applyRendererForColumn(TableColumn column) {
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

	protected void onTableItemSelect(ValueChangeEvent event) {
		//TODO: Uncomment when using Table instead of MTable
		if (mainTable.getMultiSelectMode() == MultiSelectMode.DEFAULT) {
			Collection<T> selected = (Collection<T>) mainTable.getValue();
			editButton.setEnabled(selected.size() == 1);
			deleteButton.setEnabled(selected.size() > 0);
		}
		//		Object selected = event.getProperty().getValue();
		//		editButton.setEnabled(selected != null);
		//		deleteButton.setEnabled(selected != null);

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
		List<String> headerCaptionList = new ArrayList<>();
		for (String header : mainTable.getColumnHeaders()) {
			if (header != null) {
				headerCaptionList.add(header);
			}
		}
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

	public AbstractEntityTablePanel<T> refresh() {
		if (filter != null) {
			return refresh(filter);
		}
		UI.getCurrent().access(() -> {
			deselectAll();
			List<T> entities = fetchEntities();
			mainTableContainer.removeAllItems();
			mainTableContainer.addAll(entities);
			UI.getCurrent().push();
		});
		return this;
	}

	public <F extends Object> AbstractEntityTablePanel<T> refresh(F filter) {
		this.filter = filter;
		UI.getCurrent().access(() -> {
			deselectAll();
			List<T> entities = postFetch(fetchEntities(filter));
			mainTableContainer.removeAllItems();
			mainTableContainer.addAll(entities);
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

	protected MTable<T> entityTable() {
		return mainTable;
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

	public <R extends AbstractEntityTablePanel<T>> R withItemClick(Function<T, Boolean> onItemClick) {
		this.onItemClick = onItemClick;
		return (R) this;
	}

	public <R extends AbstractEntityTablePanel<T>> R withSelection(Function<Collection<T>, Boolean> onSelection) {
		this.onSelection = onSelection;
		return (R) this;
	}

	public AbstractEntityTablePanel<T> clearFilter() {
		filter = null;
		return this;
	}

	public AbstractEntityTablePanel<T> withToolbarVisibility(boolean visibility) {
		toolbar.setVisible(visibility);
		return this;
	}

	public AbstractEntityTablePanel<T> withSelectionEnabled(boolean isSelectionEnabled) {
		this.isSelectionEnabled = isSelectionEnabled;
		if (isSelectionEnabled) {
			mainTable.setMultiSelectMode(MultiSelectMode.DEFAULT);
			mainTable.setMultiSelect(true);
			mainTable.setSelectable(true);
		} else {
			mainTable.setSelectable(false);
		}
		return this;
	}

	public AbstractEntityTablePanel<T> withMargin(boolean margins) {
		this.rootLayoutMargin = margins;
		if (rootLayout != null)
			rootLayout.setMargin(margins);
		return this;
	}

	public AbstractEntityTablePanel<T> withDelegate(AbstractEntityListPanelDelegate delegate) {
		setDelegate(delegate);
		return this;
	}

	public void setDelegate(AbstractEntityListPanelDelegate delegate) {
		this.delegate = delegate;
	}

	public AbstractEntityTablePanel<T> withSearchForm(TRAbstractSearchForm<?> searchForm) {
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

		default void onGridItemSelect(ValueChangeEvent event) {
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
			// entityTable().clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showToolbar() {
		toolbar.setVisible(true);
	}

	public void hideToolbar() {
		toolbar.setVisible(false);
	}

	public void showSecondaryToolbar() {
		secondaryToolbar.setVisible(true);
	}

	public void hideSecondaryToolbar() {
		secondaryToolbar.setVisible(false);
	}

	public void showMargin() {
		rootLayoutMargin = true;
		rootLayout.setMargin(true);
	}

	public void hideMargin() {
		rootLayoutMargin = false;
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

	public static class TableColumn {

		private Table table;
		private String propertyId;

		TableColumn(Table table, String propertyId) {
			this.table = table;
			this.propertyId = propertyId;
		}

		public void setWidth(int width) {
			table.setColumnWidth(propertyId, width);
		}

		public void setAlignment(Align align) {
			table.setColumnAlignment(propertyId, align);
		}

		public void setCollapsed(boolean collapsed) {
			table.setColumnCollapsed(propertyId, collapsed);
		}

		public void setExpandRatio(float expandRatio) {
			table.setColumnExpandRatio(propertyId, expandRatio);
		}

		public void setFooter(String footer) {
			table.setColumnFooter(propertyId, footer);
		}

		public void setHeader(String header) {
			table.setColumnHeader(propertyId, header);
		}

		public void setIcon(Resource icon) {
			table.setColumnIcon(propertyId, icon);
		}

		public String getPropertyId() {
			return propertyId;
		}

	}

}
