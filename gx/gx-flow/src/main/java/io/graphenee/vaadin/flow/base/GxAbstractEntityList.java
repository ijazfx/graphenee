package io.graphenee.vaadin.flow.base;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.CellStyle;
import org.claspina.confirmdialog.ButtonOption;
import org.claspina.confirmdialog.ConfirmDialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.gatanaso.MultiselectComboBox;

import com.google.common.eventbus.EventBus;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.ShortcutRegistration;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datepicker.DatePicker.DatePickerI18n;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.grid.dnd.GridDragEndEvent;
import com.vaadin.flow.component.grid.dnd.GridDragStartEvent;
import com.vaadin.flow.component.grid.dnd.GridDropEvent;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexDirection;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.binder.BeanPropertySet;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.PropertyDefinition;
import com.vaadin.flow.data.binder.PropertySet;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.InMemoryDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.function.ValueProvider;

import io.graphenee.core.model.GxAuthenticatedUser;
import io.graphenee.core.model.GxDashboardUser;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.util.TRCalendarUtil;
import io.graphenee.util.callback.TRParamCallback;
import io.graphenee.util.callback.TRVoidCallback;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm.EntityFormDelegate;
import io.graphenee.vaadin.flow.base.GxAbstractEntityList.GxEntityListEventListner.GxEntityListEvent;
import io.graphenee.vaadin.flow.component.DialogVariant;
import io.graphenee.vaadin.flow.component.GxDialog;
import io.graphenee.vaadin.flow.component.GxExportDataComponent;
import io.graphenee.vaadin.flow.component.GxExportDataComponent.GxExportDataComponentDelegate;
import io.graphenee.vaadin.flow.event.TRDelayClickListener;
import io.graphenee.vaadin.flow.event.TRDelayEventListener;
import io.graphenee.vaadin.flow.renderer.GxDateRenderer;
import io.graphenee.vaadin.flow.renderer.GxNumberToDateRenderer;
import io.graphenee.vaadin.flow.utils.DashboardUtils;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CssImport(value = "./styles/gx-common.css", themeFor = "vaadin-grid")
public abstract class GxAbstractEntityList<T> extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	@Autowired
	EventBus coreEventBus;

	private SplitLayout mainLayout;
	private GxDialog dialog;
	private Grid<T> dataGrid;
	private Class<T> entityClass;
	private Map<T, GxAbstractEntityForm<T>> formCache = new HashMap<>();

	private boolean isBuilt = false;

	private boolean editable = true;

	private boolean dragAndDropEnabled = false;

	private boolean rowDraggable = false;

	private MenuItem addMenuItem;

	private MenuItem deleteMenuItem;

	private MenuItem columnsDialogMenuItem;

	private MenuItem exportDataMenuItem;

	private MenuBar crudMenuBar;

	private MenuBar customMenuBar;

	private MenuBar columnMenuBar;

	private Function<Collection<T>, Boolean> onSelection;

	private HorizontalLayout menuBarLayout;

	private VerticalLayout formLayout;

	private HeaderRow headerRow;

	private List<T> items;

	private Map<String, Checkbox> hidingColumnMap = new HashMap<>();
	private Map<String, Boolean> changesMap = new HashMap<>();
	private Map<String, AbstractField<?, ?>> editorComponentMap = new HashMap<>();

	private T searchEntity;

	private NumberFormat defaultNumberFormat;

	@Getter
	@Setter
	private TRParamCallback<T> onSingleItemSelect;

	private Binder<T> searchBinder;

	private ShortcutRegistration addMenuItemShortcut;

	private ShortcutRegistration deleteMenuItemShortcut;

	private Text totalCountFooterText;
	private HorizontalLayout footerTextLayout;

	private String className;

	@Autowired
	GxDataService gxDataService;

	public GxAbstractEntityList(Class<T> entityClass) {
		this.className = entityClass.getName();
		this.entityClass = entityClass;
		this.searchBinder = new Binder<>(entityClass);
		setSizeFull();
		setMargin(false);
		setPadding(false);
		setSpacing(false);
		addClassName("gx-entity-list");
	}

	protected T initializeSearchEntity() {
		try {
			return entityClass.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			return null;
		}
	}

	protected T getSearchEntity() {
		try {
			if (searchEntity == null) {
				searchEntity = initializeSearchEntity();
				searchBinder.setBean(searchEntity);
			}
			return searchEntity;
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	synchronized private GxAbstractEntityList<T> build() {
		if (!isBuilt) {
			dataGrid = dataGrid(entityClass);
			dataGrid.setSizeFull();
			dataGrid.addClassName("gx-grid");
			((GridMultiSelectionModel<?>) dataGrid.setSelectionMode(SelectionMode.MULTI)).setSelectionColumnFrozen(true);

			DataProvider<T, ?> dataProvider = dataProvider(entityClass);
			if (dataProvider != null) {
				dataGrid.setDataProvider(dataProvider);
			}

			dataGrid.setDropFilter(this::onDropFilter);

			dataGrid.addDragStartListener(event -> {
				onDragStart(event);
			});

			dataGrid.addDragEndListener(event -> {
				onDragEnd(event);
			});

			dataGrid.addDropListener(event -> {
				onDrop(event);
			});

			dataGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
			dataGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

			mainLayout = new SplitLayout();
			mainLayout.setSizeFull();
			mainLayout.addClassName("hide-splitter");

			crudMenuBar = new MenuBar();
			columnMenuBar = new MenuBar();
			customMenuBar = new MenuBar();

			menuBarLayout = new HorizontalLayout();
			menuBarLayout.setSpacing(true);
			menuBarLayout.setPadding(true);
			menuBarLayout.setWidthFull();
			menuBarLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

			columnMenuBar.getElement().getStyle().set("margin-left", "auto");
			menuBarLayout.add(crudMenuBar, customMenuBar);
			decorateToolbarLayout(menuBarLayout);
			menuBarLayout.add(columnMenuBar);

			add(menuBarLayout);
			crudMenuBar.addThemeVariants(MenuBarVariant.LUMO_ICON);
			customMenuBar.addThemeVariants(MenuBarVariant.LUMO_ICON);
			columnMenuBar.addThemeVariants(MenuBarVariant.LUMO_ICON);

			addMenuItem = crudMenuBar.addItem(VaadinIcon.PLUS.create());
			customizeAddMenuItem(addMenuItem);

			deleteMenuItem = crudMenuBar.addItem(VaadinIcon.TRASH.create());
			customizeDeleteMenuItem(deleteMenuItem);

			exportDataMenuItem = columnMenuBar.addItem(VaadinIcon.DOWNLOAD.create());
			exportDataMenuItem.setVisible(shouldShowExportDataMenu());
			exportDataMenuItem.addClickListener(new TRDelayEventListener<ClickEvent<MenuItem>>() {

				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(ClickEvent<MenuItem> event) {
					Set<String> propNameSet = new HashSet<>();
					for (String propName : preferenceProperties()) {
						propNameSet.add(propName);
					}
					final Observable<T> observable = Observable.create(emitter -> {
						exportData(emitter);
					});
					GxExportDataComponent<T> exportDataSpreadSheetComponent = new GxExportDataComponent<T>();
					exportDataSpreadSheetComponent.withColumnsCaptions(() -> {
						List<String> columnList = new ArrayList<>();
						entityGrid().getColumns().forEach(column -> {
							if (column.isVisible() && propNameSet.contains(column.getKey())) {
								columnList.add(column.getKey());
							}
						});
						return columnList;
					});
					exportDataSpreadSheetComponent.withDataColumns(() -> {
						List<String> columnList = new ArrayList<>();
						entityGrid().getColumns().forEach(column -> {
							if (column.isVisible() && propNameSet.contains(column.getKey())) {
								columnList.add(column.getKey());
							}
						});
						return columnList;
					}).withDataProvider(observable);
					exportDataSpreadSheetComponent.withDelegate(exportDataDelegate());

					exportDataSpreadSheetComponent.prepareDownload();
				}
			});

			decorateMenuBar(customMenuBar);

			columnsDialogMenuItem = columnMenuBar.addItem(VaadinIcon.MENU.create());
			decorateColumnMenuBar(columnMenuBar);

			H2 menuHeading = new H2("Column Menu");
			menuHeading.getElement().getStyle().set("padding-top", "0px");
			menuHeading.getElement().getStyle().set("margin-top", "0px");

			FormLayout dialogLayout = new FormLayout();

			dialogLayout.add(menuHeading, 2);
			dialogLayout.add(new Hr(), 2);

			Dialog menuDialog = new Dialog();
			menuDialog.setWidth("40%");
			menuDialog.getElement().getStyle().set("padding-bottom", "0px");
			menuDialog.getElement().getStyle().set("margin-bottom", "0px");
			menuDialog.setModal(false);
			menuDialog.add(dialogLayout);

			columnsDialogMenuItem.addClickListener(new TRDelayEventListener<ClickEvent<MenuItem>>() {

				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(ClickEvent<MenuItem> event) {
					menuDialog.open();
				}
			});

			deleteMenuItem.setEnabled(false);

			FormLayout searchForm = new FormLayout();
			searchForm.setResponsiveSteps(new ResponsiveStep("100px", 6));

			decorateSearchForm(searchForm, searchBinder);

			VerticalLayout searchFormLayout = new VerticalLayout();
			searchFormLayout.getElement().getStyle().set("padding-top", "0px");
			searchFormLayout.add(searchForm);
			add(searchFormLayout);

			List<Column<T>> columns = new ArrayList<>();
			List<String> userPreferences = new ArrayList<>();
			if (availableProperties() != null && availableProperties().length > 0) {
				PropertySet<T> propertySet = BeanPropertySet.get(entityClass);
				Column<T> editColumn = dataGrid.addComponentColumn(source -> {
					Button rowEditButton = new Button(VaadinIcon.EDIT.create());
					rowEditButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY_INLINE);
					customizeEditButton(rowEditButton, source);
					return rowEditButton;
				});
				editColumn.setKey("__gxEditColumn");
				editColumn.setWidth("50px");
				editColumn.setTextAlign(ColumnTextAlign.CENTER);
				editColumn.setResizable(false);
				editColumn.setFlexGrow(0);
				editColumn.setFrozen(true);
				columns.add(editColumn);
				for (int i = 0; i < preferenceProperties().length; i++) {
					userPreferences.add(preferenceProperties()[i]);
				}
				List<Column<T>> remainingColumns = new ArrayList<>();
				List<Column<T>> userColumns = new ArrayList<>(preferenceProperties().length);
				for (int i = 0; i < availableProperties().length; i++) {
					String propertyName = availableProperties()[i];
					Column<T> column = dataGrid.getColumnByKey(propertyName);
					PropertyDefinition<T, Object> propertyDefinition;
					try {
						propertyDefinition = (PropertyDefinition<T, Object>) propertySet.getProperty(propertyName).get();
						Renderer<T> renderer = defaultRendererForProperty(propertyName, propertyDefinition);

						if (renderer != null) {
							if (column != null) {
								dataGrid.removeColumn(column);
							}
							column = dataGrid.addColumn(renderer);
							column.setKey(propertyName);
						} else {
							if (column == null)
								column = dataGrid.addColumn(propertyName);
						}
						configureDefaults(propertyName, column, propertyDefinition);
						if (i == 0 || i == (preferenceProperties().length - 1)) {
							column.setAutoWidth(true);
						}

						if (isGridFilterEnabled() && !(propertyDefinition.getType().equals(List.class) || propertyDefinition.getType().equals(Set.class))) {
							addFilteredColumn(column, propertyDefinition);
						}

						if (isGridInlineEditingEnabled()) {
							addInlineEditColumn(column, propertyDefinition);
						}

						decorateColumn(propertyName, column);

						boolean isAvailable = false;
						try {
							isAvailable = userPreferences.contains(propertyName);
						} catch (Exception e) {
							isAvailable = true;
						}

						column.setVisible(isAvailable);

						// if (isAvailable) {
						// } else {
						// 	column.setVisible(false);
						// }

						Checkbox propertyCheck = new Checkbox(propertyDefinition.getCaption(), isAvailable);

						propertyCheck.addClickListener(listener -> {
							Boolean checked = listener.getSource().getValue();
							listener.getSource().setValue(checked);
							Column<T> columnByKey = dataGrid.getColumnByKey(propertyName);
							columnByKey.setVisible(checked);
							List<String> availableProps = Arrays.asList(availableProperties());
							int index = availableProps.indexOf(propertyName);
							if (checked && !userPreferences.contains(propertyName)) {
								userPreferences.add(index, propertyName);
								// columns.remove(columnByKey);
								// columns.add(index, columnByKey);
								// dataGrid.getElement().executeJs("this._swapColumnOrders($0, $1)", columns.get(index).getElement(), columns.get(columns.size() - 1).getElement());
								// refresh();
							} else {
								userPreferences.remove(propertyName);
							}
							changesMap.putIfAbsent(propertyName, !checked);
						});
						propertyCheck.getElement().getStyle().set("width", "5%");
						dialogLayout.add(propertyCheck, -1);
						hidingColumnMap.put(propertyName, propertyCheck);

					} catch (Exception ex) {
						log.warn(propertyName + " error: " + ex.getMessage());
					}
					if (column != null) {
						if (column.isVisible()) {
							userColumns.add(column);
						} else {
							remainingColumns.add(column);
						}
					}
				}

				for (int i = 0; i < userPreferences.size(); i++) {
					String prop = userPreferences.get(i);
					Column<T> column = userColumns.stream().filter(c -> c.getKey().equals(prop)).findFirst().orElse(null);
					if (column != null) {
						columns.add(column);
					}
				}
				columns.addAll(remainingColumns);
				dataGrid.setColumnOrder(columns);
				decorateGrid(dataGrid);
				Column<T> endColumn = dataGrid.addComponentColumn(source -> new Span());
				endColumn.setKey("__gxLastColumn");
				endColumn.setAutoWidth(true);
			}

			dialogLayout.add(new Hr(), 2);

			Button applyButton = new Button("Apply");
			applyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			applyButton.addClickListener(new TRDelayClickListener<Button>() {

				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(ClickEvent<Button> event) {
					loggedInUser().setPreference(className, userPreferences.stream().collect(Collectors.joining(",")));
					coreEventBus.post(loggedInUser().getUser());
					// gxDataService.save(loggedInUser().getUser());
					menuDialog.close();
					UI.getCurrent().getPage().reload();
				}
			});

			Button dismissButton = new Button("DISMISS");
			dismissButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
			dismissButton.addClickListener(new TRDelayClickListener<Button>() {

				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(ClickEvent<Button> event) {
					changesMap.keySet().forEach(key -> {
						setColumnVisibility(key, changesMap.get(key));
					});
					menuDialog.close();
					changesMap.clear();
				}
			});
			HorizontalLayout buttonLayout = new HorizontalLayout(applyButton, dismissButton);
			buttonLayout.setPadding(false);
			buttonLayout.setSpacing(false);
			buttonLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
			dialogLayout.add(buttonLayout, 2);

			dataGrid.getColumns().stream().filter(c -> c.getKey() != null && !c.getKey().matches("__gx.*Column")).forEach(col -> col.setVisible(false));

			for (String key : preferenceProperties()) {
				Column<T> columnByKey = dataGrid.getColumnByKey(key);
				if (columnByKey != null) {
					columnByKey.setVisible(true);
					columnByKey.setSortable(true);
				}
			}

			mainLayout.addToPrimary(dataGrid);
			add(mainLayout);
			footerTextLayout = new HorizontalLayout();
			totalCountFooterText = new Text("No Records");

			footerTextLayout.add(totalCountFooterText);
			footerTextLayout.addClassName("footer-layout");

			if (!shouldShowFormInDialog()) {
				formLayout = new VerticalLayout();
				formLayout.setMargin(false);
				formLayout.setPadding(false);
				mainLayout.addToSecondary(formLayout);
				mainLayout.getSecondaryComponent().setVisible(false);
				mainLayout.setSplitterPosition(defaultSplitterPosition());
			} else {
				mainLayout.setSplitterPosition(100);
			}

			dataGrid.addSelectionListener(sl -> {
				int selected = sl.getAllSelectedItems().size();
				deleteMenuItem.setEnabled(selected > 0);
				if (onSelection != null) {
					Boolean value = onSelection.apply(sl.getAllSelectedItems());
					if (value != null && value) {
						onGridItemSelect(sl);
					}
				} else {
					onGridItemSelect(sl);
				}
				if (onSingleItemSelect != null) {
					if (selected == 1) {
						onSingleItemSelect.execute(sl.getAllSelectedItems().iterator().next());
					} else {
						onSingleItemSelect.execute(null);
					}
				}
			});

			dataGrid.addItemClickListener(cl -> onGridItemClicked(cl));

			dataGrid.addItemDoubleClickListener(cl -> onGridItemDoubleClicked(cl));

			for (String p : availableProperties()) {
				setColumnVisibility(p, false);
			}

			for (String p : preferenceProperties()) {
				setColumnVisibility(p, true);
			}

			postBuild();

			if (shouldDisplayGridFooter()) {
				add(footerTextLayout);
			}

			enableShortcuts();
			isBuilt = true;

			dataGrid.setColumnReorderingAllowed(true);
			dataGrid.addColumnReorderListener(listener -> {
				String orderedColumns = listener.getColumns().stream().filter(c -> !c.getKey().matches("__gx.*Column") && c.isVisible()).map(c -> c.getKey())
						.collect(Collectors.joining(","));
				loggedInUser().setPreference(className, orderedColumns);
				coreEventBus.post(loggedInUser().getUser());
			});
		}
		return this;
	}

	protected void decorateColumnMenuBar(MenuBar columnMenuBar) {
	}

	protected GxExportDataComponentDelegate<T> exportDataDelegate() {
		return null;
	}

	protected boolean shouldDisplayGridFooter() {
		return true;
	}

	protected boolean shouldAllowColumnReordering() {
		return false;
	}

	protected void enableShortcuts() {
		addMenuItemShortcut = addMenuItem.addClickShortcut(Key.KEY_N, KeyModifier.ALT);
		deleteMenuItemShortcut = deleteMenuItem.addClickShortcut(Key.DELETE, KeyModifier.ALT);
	}

	protected void disableShortcuts() {
		addMenuItemShortcut.remove();
		deleteMenuItemShortcut.remove();
	}

	protected boolean isGridInlineEditingEnabled() {
		return false;
	}

	protected boolean isAuditLogEnabled() {
		return false;
	}

	protected boolean shouldShowExportDataMenu() {
		return true;
	}

	private void addInlineEditColumn(Column<T> column, PropertyDefinition<T, Object> propertyDefinition) {
		AbstractField<?, ?> editorComponent = editorComponentMap.get(column.getKey());
		if (editorComponent == null) {
			editorComponent = defaultInlineEditorForProperty(column.getKey(), propertyDefinition);
			if (editorComponent != null) {
				dataGrid.getEditor().getBinder().forField(editorComponent).bind(column.getKey());
				column.setEditorComponent(editorComponent);
				editorComponentMap.put(column.getKey(), editorComponent);
			}
		}

	}

	protected void onGridItemDoubleClicked(ItemDoubleClickEvent<T> icl) {
		if (isGridInlineEditingEnabled()) {
			String propertyName = icl.getColumn().getKey();
			if (propertyName != null) {
				// AbstractField<?, ?> editorComponent = editorComponentMap.get(propertyName);
				dataGrid.getEditor().editItem(icl.getItem());
				//				if (editorComponent instanceof Focusable) {
				//					((Focusable<?>) editorComponent).focus();
				//				}
			}
		}
	}

	private AbstractField<?, ?> defaultInlineEditorForProperty(String propertyName, PropertyDefinition<T, Object> propertyDefinition) {
		if (propertyDefinition.getSetter().isEmpty()) {
			return null;
		}

		AbstractField<?, ?> c = inlineEditorForProperty(propertyName, propertyDefinition);
		if (c == null) {
			if (propertyName.matches("date.*|.*Date")) {
				if (propertyDefinition.getType().equals(Long.class)) {
					c = createDatePicker();
				} else if (c == null && propertyDefinition.getType().equals(Timestamp.class)) {
					c = createDatePicker();
				}
			}
			if (c == null && propertyName.matches("dateTime.*|.*DateTime")) {
				if (propertyDefinition.getType().equals(Long.class)) {
					c = createDateTimePicker();
				}
			}
			if (c == null && propertyDefinition.getType().equals(Timestamp.class)) {
				c = createDateTimePicker();
			}
			if (c == null && propertyDefinition.getType().equals(Date.class)) {
				c = createDatePicker();
			}
			if (c == null && propertyDefinition.getType().equals(Boolean.class)) {
				c = new Checkbox();
			}
			if (c == null && propertyDefinition.getType().equals(Integer.class)) {
				c = new IntegerField();
			}
			if (c == null && propertyDefinition.getType().equals(Long.class)) {
				c = new IntegerField();
			}
			if (c == null && propertyDefinition.getType().equals(Float.class)) {
				c = new NumberField();
			}
			if (c == null && propertyDefinition.getType().equals(Double.class)) {
				c = new NumberField();
			}
			if (c == null && propertyDefinition.getType().equals(Boolean.class)) {
				c = new Checkbox();
			}
			if (c == null) {
				c = new TextField();
			}
		}
		c.getElement().getStyle().set("width", "100%");
		// c.getElement().setProperty("clearButtonVisible", true);
		// c.getElement().setProperty("autoselect", true);
		c.getElement().setProperty("placeholder", propertyDefinition.getCaption() == null ? "" : propertyDefinition.getCaption());
		return c;
	}

	protected DateTimePicker createDateTimePicker() {
		DateTimePicker p = new DateTimePicker();
		DatePickerI18n i = new DatePickerI18n();
		i.setDateFormat(TRCalendarUtil.getCustomDateTimeFormatter().toPattern());
		p.setDatePickerI18n(i);
		return p;
	}

	protected DatePicker createDatePicker() {
		DatePicker p = new DatePicker();
		DatePickerI18n i = new DatePickerI18n();
		i.setDateFormat(TRCalendarUtil.getCustomDateFormatter().toPattern());
		p.setI18n(i);
		return p;
	}

	protected AbstractField<?, ?> inlineEditorForProperty(String propertyName, PropertyDefinition<T, Object> propertyDefinition) {
		return null;
	}

	protected void onGridItemClicked(ItemClickEvent<T> icl) {
	}

	protected void exportData(ObservableEmitter<T> emitter) {
		Stream<T> data = null;
		if (!entityGrid().getSelectedItems().isEmpty()) {
			data = entityGrid().getSelectedItems().stream();
		} else {
			data = getData();
		}
		if (data != null) {
			data.forEach(d -> {
				if (emitter.isDisposed()) {
					return;
				}
				emitter.onNext(d);
			});
		}
		emitter.onComplete();
	}

	protected void decorateSearchForm(FormLayout searchForm, Binder<T> searchBinder) {
	}

	protected void decorateToolbarLayout(HorizontalLayout toolbarLayout) {
	}

	protected void onDrop(GridDropEvent<T> event) {
	}

	protected void onDragEnd(GridDragEndEvent<T> event) {
	}

	protected void onDragStart(GridDragStartEvent<T> event) {
	}

	protected boolean onDropFilter(T item) {
		return true;
	}

	protected void setColumnVisibility(String propertyName, boolean isVisible) {
		Checkbox cb = hidingColumnMap.get(propertyName);
		if (cb != null) {
			cb.setValue(isVisible);
			Column<T> columnByKey = entityGrid().getColumnByKey(propertyName);
			columnByKey.setVisible(isVisible);
		}
	}

	protected Grid<T> dataGrid(Class<T> entityClass) {
		Grid<T> dataGrid = new Grid<>(entityClass, true);
		if (isGridInlineEditingEnabled()) {
			Binder<T> editBinder = new Binder<>(entityClass);
			dataGrid.getEditor().setBinder(editBinder);
			dataGrid.getEditor().setBuffered(false);
			editBinder.addValueChangeListener(l -> {
				dataGrid.getEditor().save();
				onGridInlineEditorSave(dataGrid.getEditor().getItem());
			});
		}
		List<Column<T>> removeList = new ArrayList<>(dataGrid.getColumns());
		for (String key : preferenceProperties()) {
			Column<T> column = dataGrid.getColumnByKey(key);
			if (column != null) {
				column.setVisible(true);
				column.setSortable(true);
				removeList.remove(column);
			}
		}
		removeList.forEach(c -> dataGrid.removeColumn(c));
		return dataGrid;
	}

	protected DataProvider<T, ?> dataProvider(Class<T> entityClass) {
		items = new ArrayList<>();
		return DataProvider.ofCollection(items);
	}

	protected void customizeAddMenuItem(MenuItem addMenuItem) {
		addMenuItem.addClickListener(new TRDelayClickListener<MenuItem>() {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(ClickEvent<MenuItem> event) {
				try {
					openForm(entityClass.getDeclaredConstructor().newInstance());
				} catch (Exception e) {
					Notification.show(e.getMessage(), 3000, Position.BOTTOM_CENTER);
				}
			}
		});

	}

	protected void customizeEditButton(Button editButton, T entity) {
		editButton.addClickListener(new TRDelayClickListener<Button>() {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(ClickEvent<Button> event) {
				openForm(entity);
			}
		});
	}

	protected void customizeDeleteMenuItem(MenuItem deleteMenuItem) {
		deleteMenuItem.addClickListener(new TRDelayClickListener<MenuItem>() {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(ClickEvent<MenuItem> event) {
				if (shouldShowDeleteConfirmation()) {
					ConfirmDialog.createQuestion().withCaption("Confirmation").withMessage("Are you sure to delete selected record(s)?").withOkButton(() -> {
						try {
							onDelete(dataGrid.getSelectedItems());
							if (isAuditLogEnabled()) {
								auditLog(DashboardUtils.getLoggedInUser(), DashboardUtils.getRemoteAddress(), "DELETE", entityClass.getSimpleName(), dataGrid.getSelectedItems());
							}
							listeners.forEach(l -> {
								l.onEvent(GxEntityListEvent.DELETE, dataGrid.getSelectedItems());
							});
							refresh();
							deleteMenuItem.setEnabled(false);
							dataGrid.deselectAll();
						} catch (Exception e) {
							log.warn(e.getMessage(), e);
							Notification.show(e.getMessage(), 3000, Position.BOTTOM_CENTER);
						}
					}, ButtonOption.focus(), ButtonOption.caption("YES")).withCancelButton(ButtonOption.caption("NO")).open();
				} else {
					try {
						onDelete(dataGrid.getSelectedItems());
						if (isAuditLogEnabled()) {
							auditLog(DashboardUtils.getLoggedInUser(), DashboardUtils.getRemoteAddress(), "DELETE", entityClass.getSimpleName(), dataGrid.getSelectedItems());
						}
						listeners.forEach(l -> {
							l.onEvent(GxEntityListEvent.DELETE, dataGrid.getSelectedItems());
						});
						refresh();
					} catch (Exception e) {
						log.warn(e.getMessage(), e);
						Notification.show(e.getMessage(), 3000, Position.BOTTOM_CENTER);
					}
				}
			}
		});

	}

	@SuppressWarnings("unchecked")
	private void addFilteredColumn(Column<T> column, PropertyDefinition<T, Object> propertyDefinition) {
		if (searchEntity == null) {
			searchEntity = getSearchEntity();
		}
		if (column.getKey().matches("(extension|download)"))
			return;
		AbstractField<?, ?> columnFilter = defaultColumnFilterForProperty(column.getKey(), propertyDefinition);
		columnFilter.addValueChangeListener(event -> {
			if (entityGrid().getDataProvider() instanceof InMemoryDataProvider) {
				InMemoryDataProvider<T> dataProvider = (InMemoryDataProvider<T>) entityGrid().getDataProvider();
				dataProvider.addFilter(fe -> {
					if (columnFilter.isEmpty()) {
						return true;
					}
					Object filterValue = columnFilter.getValue();
					Object columnValue = propertyDefinition.getGetter().apply(fe);
					boolean matched = filterValue.equals(columnValue);
					if (!matched) {
						String fv = filterValue.toString().trim();
						if (columnValue != null) {
							String cv = columnValue.toString().trim();
							matched = fv.equalsIgnoreCase(cv);
							if (!matched) {
								try {
									Pattern p = Pattern.compile(fv, Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
									matched = p.matcher(cv).find();
								} catch (Exception e) {

								}
							}
						}
					}
					return matched;
				});
				dataProvider.refreshAll();
			} else if (entityGrid().getDataProvider() instanceof ConfigurableFilterDataProvider) {
				ConfigurableFilterDataProvider<T, Void, Object> dataProvider = (ConfigurableFilterDataProvider<T, Void, Object>) entityGrid().getDataProvider();
				propertyDefinition.getSetter().ifPresent(setter -> {
					if (event.getValue() == null || event.getValue().toString().isBlank()) {
						setter.accept(searchEntity, null);
					} else {
						Object o = event.getValue();
						setter.accept(searchEntity, convertValue(o, column.getKey(), propertyDefinition.getType()));
					}
					dataProvider.setFilter(searchEntity);
				});
			}
		});

		if (headerRow == null) {
			headerRow = dataGrid.appendHeaderRow();
		}

		headerRow.getCell(column).setComponent(columnFilter);
	}

	private Object convertValue(Object o, String key, Class<Object> type) {
		try {
			if (o instanceof LocalDate) {
				Date d = java.sql.Date.valueOf((LocalDate) o);
				if (d.getClass().equals(type))
					return d;
				if (type.equals(Long.class))
					return d.getTime();
				if (type.equals(Timestamp.class))
					return new Timestamp(d.getTime());
			}
			if (o instanceof LocalDateTime) {
				Timestamp d = Timestamp.valueOf((LocalDateTime) o);
				if (d.getClass().equals(type))
					return d;
				if (type.equals(Long.class))
					return d.getTime();
				if (type.equals(Date.class))
					return new Date(d.getTime());
			}
			if (o instanceof Boolean) {
				Boolean b = (Boolean) o;
				if (b.getClass().equals(type))
					return b;
			}
			if (o instanceof String) {
				String v = (String) o;
				if (type.equals(String.class))
					return v;
				if (type.equals(Integer.class))
					return Integer.valueOf(v);
				if (type.equals(Long.class))
					return Long.valueOf(v);
				if (type.equals(Double.class))
					return Double.valueOf(v);
				if (type.equals(Boolean.class))
					return Boolean.valueOf(v);
			}
			if (o instanceof Number) {
				Number n = (Number) o;
				if (type.equals(Integer.class))
					return n.intValue();
				if (type.equals(Long.class))
					return n.longValue();
				if (type.equals(Double.class))
					return n.doubleValue();
				if (type.equals(Float.class))
					return n.floatValue();
			}
			return o;
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
			return null;
		}
	}

	private AbstractField<?, ?> defaultColumnFilterForProperty(String propertyName, PropertyDefinition<T, Object> propertyDefinition) {
		AbstractField<?, ?> filter = null;
		if (propertyName.matches("dateTime.*|.*DateTime")) {
			if (propertyDefinition.getType().equals(Long.class)) {
				filter = createDateTimePicker();
			}
		}
		if (filter == null && propertyName.matches("date.*|.*Date")) {
			if (propertyDefinition.getType().equals(Long.class)) {
				filter = createDatePicker();
			} else if (filter == null && propertyDefinition.getType().equals(Timestamp.class)) {
				filter = createDatePicker();
			}
		}
		if (filter == null && propertyDefinition.getType().equals(Timestamp.class)) {
			filter = createDateTimePicker();
		}
		if (filter == null && propertyDefinition.getType().equals(Date.class)) {
			filter = createDatePicker();
		}
		if (filter == null && propertyDefinition.getType().equals(Boolean.class)) {
			ComboBox<Boolean> booleanComboBox = new ComboBox<Boolean>();
			booleanComboBox.setItems(true, false);
			booleanComboBox.setItemLabelGenerator(v -> {
				return v == null ? "" : v == true ? "Checked" : "Unchecked";
			});
			filter = booleanComboBox;
		}
		if (filter == null && propertyDefinition.getType().getSuperclass().equals(Number.class)) {
			filter = new NumberField();
		}
		if (filter == null) {
			filter = new TextField();
		}
		filter.getElement().getStyle().set("width", "100%");
		filter.getElement().setProperty("clearButtonVisible", true);
		// filter.getElement().setProperty("placeholder", propertyDefinition.getCaption() == null ? "" : propertyDefinition.getCaption());
		return columnFilterForProperty(propertyName, propertyDefinition, filter);
	}

	/**
	 * @deprecated use {@link #columnFilterForProperty(String, PropertyDefinition, AbstractField)} instead.
	 */
	protected AbstractField<?, ?> columnFilterForProperty(String propertyName, PropertyDefinition<T, Object> propertyDefinition) {
		return null;
	}

	protected AbstractField<?, ?> columnFilterForProperty(String propertyName, PropertyDefinition<T, Object> propertyDefinition, AbstractField<?, ?> defaultFilter) {
		AbstractField<?, ?> customFilter = columnFilterForProperty(propertyName, propertyDefinition);
		if (customFilter != null)
			return customFilter;
		return defaultFilter;
	}

	protected void decorateMenuBar(MenuBar menuBar) {
	}

	protected void decorateGrid(Grid<T> dataGrid) {

	}

	@SuppressWarnings("unchecked")
	private Renderer<T> defaultRendererForProperty(String propertyName, PropertyDefinition<T, ?> propertyDefinition) {
		Renderer<T> renderer = rendererForProperty(propertyName, propertyDefinition);
		if (renderer == null) {
			if (propertyName.matches("dateTime.*|.*DateTime")) {
				if (propertyDefinition.getType().getSuperclass().equals(Number.class)) {
					renderer = new GxNumberToDateRenderer<>((ValueProvider<T, Number>) propertyDefinition.getGetter(), GxNumberToDateRenderer.GxDateResolution.DateTime);
				} else if (renderer == null && propertyDefinition.getType().equals(Timestamp.class)) {
					renderer = new GxDateRenderer<>((ValueProvider<T, Date>) propertyDefinition.getGetter(), GxDateRenderer.GxDateResolution.DateTime);
				}
			}
			if (renderer == null && propertyName.matches("date.*|.*Date")) {
				if (propertyDefinition.getType().getSuperclass().equals(Number.class)) {
					renderer = new GxNumberToDateRenderer<>((ValueProvider<T, Number>) propertyDefinition.getGetter(), GxNumberToDateRenderer.GxDateResolution.Date);
				} else if (renderer == null && propertyDefinition.getType().equals(Timestamp.class)) {
					renderer = new GxDateRenderer<>((ValueProvider<T, Date>) propertyDefinition.getGetter(), GxDateRenderer.GxDateResolution.Date);
				}
			}
			if (renderer == null && propertyDefinition.getType().equals(Timestamp.class)) {
				renderer = new GxDateRenderer<>((ValueProvider<T, Date>) propertyDefinition.getGetter(), GxDateRenderer.GxDateResolution.DateTime);
			}
			if (renderer == null && (propertyDefinition.getType().equals(List.class) || propertyDefinition.getType().equals(Set.class))) {
				renderer = new ComponentRenderer<>(s -> {
					MultiselectComboBox<Object> c = new MultiselectComboBox<Object>();
					Collection<Object> bag = (Collection<Object>) propertyDefinition.getGetter().apply((T) s);
					Set<Object> value = new HashSet<>(bag);
					c.setItems(value);
					c.setValue(value);
					c.setWidthFull();
					c.setReadOnly(true);
					return c;
				});
			}
			if (renderer == null && propertyDefinition.getType().equals(Date.class)) {
				renderer = new GxDateRenderer<>((ValueProvider<T, Date>) propertyDefinition.getGetter(), GxDateRenderer.GxDateResolution.Date);
			}
			if (renderer == null && propertyDefinition.getType().equals(Boolean.class)) {
				renderer = new ComponentRenderer<>(s -> {
					Checkbox c = new Checkbox();
					Boolean value = (Boolean) propertyDefinition.getGetter().apply((T) s);
					c.setValue(value);
					c.setReadOnly(true);
					return c;
				});
			}
			if (renderer == null && propertyDefinition.getType().getSuperclass().equals(Number.class)) {
				NumberFormat numberFormat = numberFormatForProperty(propertyName, propertyDefinition);
				renderer = new NumberRenderer<>((ValueProvider<T, Number>) propertyDefinition.getGetter(), numberFormat);
			}
		}
		return renderer;
	}

	protected NumberFormat numberFormatForProperty(String propertyName, PropertyDefinition<T, ?> propertyDefinition) {
		if (defaultNumberFormat == null) {
			defaultNumberFormat = DecimalFormat.getNumberInstance();
			defaultNumberFormat.setMaximumFractionDigits(2);
			defaultNumberFormat.setGroupingUsed(true);

		}
		return defaultNumberFormat;
	}

	protected Renderer<T> rendererForProperty(String propertyName, PropertyDefinition<T, ?> propertyDefinition) {
		return null;
	}

	protected void configureDefaults(String propertyName, Column<T> column, PropertyDefinition<T, ?> propertyDefinition) {
		column.setId(propertyName);
		Span header = new Span(propertyDefinition.getCaption());
		header.getStyle().set("white-space", "normal");
		column.setHeader(header);
		column.setResizable(true);
		column.setFlexGrow(0);
		column.setWidth("8rem");
		if (propertyDefinition != null) {
			if (propertyDefinition.getType().getSuperclass() != null && propertyDefinition.getType().getSuperclass().equals(Number.class)) {
				column.setTextAlign(ColumnTextAlign.END);
			}
			if (propertyDefinition.getType() != null && propertyDefinition.getType().equals(String.class)) {
				column.setTextAlign(ColumnTextAlign.START);
			}
		}
	}

	protected void decorateColumn(String propertyName, Column<T> column) {
	}

	protected void decorateCell(String propertyName, CellStyle cellStyle) {
	}

	protected void postBuild() {
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setDragAndDropEnabled(boolean enabled) {
		this.dragAndDropEnabled = enabled;
	}

	public boolean isDragAndDropEnabled() {
		return this.dragAndDropEnabled;
	}

	public void setRowDraggable(boolean draggable) {
		this.rowDraggable = draggable;
	}

	public boolean isRowDraggable() {
		return this.rowDraggable;
	}

	public void shouldShowToolbar(boolean show) {
		if (menuBarLayout != null) {
			menuBarLayout.setVisible(show);
		}
	}

	/**
	 * Default value is 0.0 which is 0% for primary container and 100% for
	 * secondary container.
	 * 
	 * @return
	 */
	protected double defaultSplitterPosition() {
		return 0.0;
	}

	private void openForm(T entity) {
		preEdit(entity);
		if (isGridInlineEditingEnabled()) {
			entityGrid().getEditor().editItem(entity);
		} else {
			GxAbstractEntityForm<T> entityForm = cachedForm(entity);
			if (entityForm != null) {
				if (!shouldShowFormInDialog()) {
					formLayout.removeAll();
					formLayout.add(entityForm);
					mainLayout.getSecondaryComponent().setVisible(true);
				} else {
					disableShortcuts();
					dialog = entityForm.showInDialog(entity);
					dialog.addOpenedChangeListener(l -> {
						enableShortcuts();
					});
				}
			} else {
				if (mainLayout.getSecondaryComponent() != null)
					mainLayout.getSecondaryComponent().setVisible(false);
			}
		}
	}

	protected String dialogWidth() {
		return "90%";
	}

	protected String dialogHeight() {
		return "90%";
	}

	protected void preEdit(T entity) {
	}

	protected abstract Stream<T> getData();

	protected String[] availableProperties() {
		return visibleProperties();
	}

	protected abstract String[] visibleProperties();

	private String[] preferenceProperties() {
		Set<String> apset = Stream.of(availableProperties()).collect(Collectors.toSet());
		try {
			String preferences = loggedInUser().getPreference(className);
			String[] props = preferences.split(",");
			props = Stream.of(props).filter(p -> apset.contains(p)).collect(Collectors.toList()).toArray(new String[] {});
			return props;
		} catch (Exception e) {
			return availableProperties();
		}
	}

	public GxDashboardUser loggedInUser() {
		return DashboardUtils.getLoggedInUser();
	}

	public void refresh() {
		build();
		entityGrid().deselectAll();
		crudMenuBar.setVisible(isEditable());
		dataGrid.getColumns().stream().filter(c -> c.getKey() != null && c.getKey().equals("__gxEditColumn")).forEach(c -> c.setVisible(isEditable()));
		if (isDragAndDropEnabled()) {
			dataGrid.setRowsDraggable(isRowDraggable());
			dataGrid.setDropMode(GridDropMode.ON_TOP_OR_BETWEEN);
		} else {
			dataGrid.setRowsDraggable(false);
			dataGrid.setDropMode(null);
		}
		if (!shouldShowFormInDialog()) {
			if (mainLayout.getSecondaryComponent() != null)
				mainLayout.getSecondaryComponent().setVisible(false);
		}
		DataProvider<T, ?> dataProvider = dataGrid.getDataProvider();
		if (dataProvider instanceof InMemoryDataProvider) {
			items.clear();
			items.addAll(getData().collect(Collectors.toList()));
			updateTotalCountFooter(items.size());
			dataProvider.refreshAll();
		} else {
			dataProvider.refreshAll();
		}
		if (entityGrid() instanceof TreeGrid) {
			((TreeGrid<T>) entityGrid()).expand(entityGrid().getSelectedItems());
		}
	}

	protected final void updateTotalCountFooter(int count) {
		if (shouldDisplayGridFooter()) {
			if (count == 0) {
				totalCountFooterText.setText("No records");
			} else if (count == 1) {
				totalCountFooterText.setText("1 record");
			} else {
				totalCountFooterText.setText(DecimalFormat.getNumberInstance().format(count) + " records");
			}
		}
	}

	protected abstract GxAbstractEntityForm<T> getEntityForm(T entity);

	/**
	 * TODO: Need to improve caching of form.
	 * 
	 * @param entity
	 * @return
	 */
	private GxAbstractEntityForm<T> cachedForm(T entity) {
		GxAbstractEntityForm<T> entityForm = formCache.get(entity);
		if (entityForm == null) {
			entityForm = getEntityForm(entity);
			if (entityForm != null) {
				formCache.put(entity, entityForm);
			}
		}
		if (entityForm != null) {
			entityForm.setEditable(isEditable());

			EntityFormDelegate<T> delegate = new EntityFormDelegate<T>() {

				@Override
				public void onSave(T entity) {
					try {
						GxAbstractEntityList.this.onSave(entity);
						if (isAuditLogEnabled()) {
							try {
								auditLog(DashboardUtils.getLoggedInUser(), DashboardUtils.getRemoteAddress(), "SAVE", entityClass.getSimpleName(), List.of(entity));
							} catch (Exception ex) {

							}
						}
						listeners.forEach(l -> {
							l.onEvent(GxEntityListEvent.SAVE, List.of(entity));
						});
						if (!shouldShowFormInDialog()) {
							mainLayout.getSecondaryComponent().setVisible(false);
						} else {
							dialog.close();
						}
						refresh();
					} catch (Exception e) {
						log.warn(e.getMessage(), e);
						Notification.show(e.getMessage(), 3000, Position.BOTTOM_CENTER);
					}
					refresh();
				}

				@Override
				public void onDismiss(T entity) {
					if (!shouldShowFormInDialog()) {
						mainLayout.getSecondaryComponent().setVisible(false);
					} else {
						dialog.close();
					}
				}
			};
			entityForm.setDelegate(delegate);
		}
		return entityForm;
	}

	protected void auditLog(GxAuthenticatedUser user, String remoteAddress, String auditEvent, String auditEntity, Collection<T> entities) {
		log.warn(this + " - Override auditLog(...) method to log this event.");
	}

	protected boolean shouldShowFormInDialog() {
		return true;
	}

	protected boolean shouldShowDeleteConfirmation() {
		return true;
	}

	protected boolean isGridFilterEnabled() {
		return true;
	}

	protected void onGridInlineEditorSave(T entity) {
		this.onSave(entity);
	}

	protected abstract void onSave(T entity);

	protected abstract void onDelete(Collection<T> entities);

	public Grid<T> entityGrid() {
		return dataGrid;
	}

	protected void onGridItemSelect(SelectionEvent<Grid<T>, T> event) {
	}

	@SuppressWarnings("unchecked")
	public <R extends GxAbstractEntityList<T>> R withSelection(Function<Collection<T>, Boolean> onSelection) {
		this.onSelection = onSelection;
		return (R) this;
	}

	public void hideToolbar() {
		menuBarLayout.setVisible(false);
	}

	public void showToolbar() {
		menuBarLayout.setVisible(true);
	}

	public void showSecondaryComponent(Component component) {
		if (formLayout == null) {
			formLayout = new VerticalLayout();
			mainLayout.addToSecondary(formLayout);
		}
		formLayout.removeAll();
		formLayout.add(component);
		mainLayout.setSplitterPosition(defaultSplitterPosition());
		mainLayout.getSecondaryComponent().setVisible(true);
	}

	public void hideSecondaryComponent() {
		mainLayout.getSecondaryComponent().setVisible(false);
		if (formLayout != null) {
			formLayout.removeAll();
		}
	}

	public boolean isSecondaryComponentVisible() {
		return mainLayout != null && mainLayout.getSecondaryComponent() != null && mainLayout.getSecondaryComponent().isVisible();
	}

	public Dialog showInDialog(TRVoidCallback... callback) {
		Button dlgDismissButton = new Button("DISMISS");
		dlgDismissButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
		HorizontalLayout dlgFooter = new HorizontalLayout(dlgDismissButton);
		dlgFooter.setJustifyContentMode(JustifyContentMode.END);
		dlgFooter.setWidthFull();
		FlexLayout layout = new FlexLayout();
		layout.setSizeFull();
		layout.setFlexDirection(FlexDirection.COLUMN);
		layout.add(GxAbstractEntityList.this, dlgFooter);
		layout.setFlexGrow(2, GxAbstractEntityList.this);
		GxDialog dlg = new GxDialog(layout);
		dlg.addThemeVariants(DialogVariant.NO_PADDING);
		dlg.setId("dlg" + UUID.randomUUID().toString().replace("-", ""));
		dlg.setWidth(dialogWidth());
		dlg.setHeight(dialogHeight());
		dlg.setDraggable(true);
		dlg.setResizable(true);
		dlg.open();
		//		dlgDismissButton.addClickShortcut(Key.ESCAPE);

		dlgDismissButton.addClickListener(new TRDelayClickListener<Button>() {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(ClickEvent<Button> event) {
				try {
					Stream.of(callback).forEach(cb -> cb.execute());
					dlg.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		return dlg;
	}

	public void dismissDialog() {
		if (dialog != null) {
			dialog.close();
		}
	}

	public void editItem(T item) {
		openForm(item);
	}

	HashSet<GxEntityListEventListner<T>> listeners = new HashSet<>();

	public void registerListener(GxEntityListEventListner<T> listener) {
		listeners.add(listener);
	}

	public void unregisterListener(GxEntityListEventListner<T> listener) {
		listeners.remove(listener);
	}

	public static interface GxEntityListEventListner<T> {
		public enum GxEntityListEvent {
			SAVE,
			DELETE
		}

		void onEvent(GxEntityListEvent event, Collection<T> entity);
	}

}
