package io.graphenee.vaadin.flow.base;

import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanPropertySet;
import com.vaadin.flow.data.binder.PropertyDefinition;
import com.vaadin.flow.data.binder.PropertySet;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.InMemoryDataProvider;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.ValueProvider;

import org.apache.commons.lang3.StringUtils;
import org.claspina.confirmdialog.ButtonOption;
import org.claspina.confirmdialog.ConfirmDialog;

import io.graphenee.core.util.TRCalendarUtil;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm.EntityFormDelegate;
import io.graphenee.vaadin.flow.renderer.GxDateRenderer;
import io.graphenee.vaadin.flow.renderer.GxLongToDateRenderer;
import io.graphenee.vaadin.flow.renderer.GxLongToDateTimeRenderer;
import io.graphenee.vaadin.flow.renderer.GxTimestampRenderer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class GxAbstractEntityList<T> extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    private SplitLayout mainLayout;
    private Dialog dialog;
    private Grid<T> dataGrid;
    private Class<T> entityClass;
    private Map<T, GxAbstractEntityForm<T>> formCache = new HashMap<>();

    private boolean isBuilt = false;

    private boolean editable = true;

    private MenuItem addMenuItem;

    private MenuItem editMenuItem;

    private MenuItem deleteMenuItem;

    private MenuItem columnsMenuItem;

    private MenuBar crudMenuBar;

    private MenuBar customMenuBar;

    private MenuBar columnMenuBar;

    private Function<Collection<T>, Boolean> onSelection;

    private HorizontalLayout menuBarLayout;

    private VerticalLayout formLayout;

    private HeaderRow headerRow;

    private List<T> items;

    private Column<T> columnByKey;

    public GxAbstractEntityList(Class<T> entityClass) {
        this.entityClass = entityClass;
        setSizeFull();
        setMargin(false);
        setPadding(false);
        setSpacing(false);
        addClassName("gx-abstract-entity-list");
    }

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

            dataGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);

            mainLayout = new SplitLayout();
            mainLayout.setSizeFull();

            crudMenuBar = new MenuBar();
            columnMenuBar = new MenuBar();
            customMenuBar = new MenuBar();

            menuBarLayout = new HorizontalLayout();
            menuBarLayout.setSpacing(false);
            menuBarLayout.setPadding(true);
            menuBarLayout.setWidthFull();

            customMenuBar.getElement().getStyle().set("margin-right", "auto");
            menuBarLayout.add(crudMenuBar, customMenuBar, columnMenuBar);

            add(menuBarLayout);
            crudMenuBar.addThemeVariants(MenuBarVariant.LUMO_ICON);
            customMenuBar.addThemeVariants(MenuBarVariant.LUMO_ICON);
            columnMenuBar.addThemeVariants(MenuBarVariant.LUMO_ICON);

            addMenuItem = crudMenuBar.addItem("Add");
            customizeAddMenuItem(addMenuItem);

            editMenuItem = crudMenuBar.addItem("Edit");
            customizeEditMenuItem(editMenuItem);

            deleteMenuItem = crudMenuBar.addItem("Delete");
            customizeDeleteMenuItem(deleteMenuItem);

            decorateMenuBar(customMenuBar);

            columnsMenuItem = columnMenuBar.addItem(VaadinIcon.MENU.create());

            editMenuItem.setEnabled(false);
            deleteMenuItem.setEnabled(false);

            List<Column<T>> columns = new ArrayList<>();
            if (visibleProperties() != null && visibleProperties().length > 0) {
                PropertySet<T> propertySet = BeanPropertySet.get(entityClass);
                for (int i = 0; i < visibleProperties().length; i++) {
                    String propertyName = visibleProperties()[i];
                    Column<T> column = dataGrid.getColumnByKey(propertyName);
                    PropertyDefinition<T, ?> propertyDefinition;
                    try {
                        propertyDefinition = propertySet.getProperty(propertyName).get();
                        Renderer<T> renderer = defaultRendererForProperty(propertyName, propertyDefinition);
                        if (renderer != null) {
                            if (column != null)
                                dataGrid.removeColumn(column);
                            column = dataGrid.addColumn(renderer);
                            column.setKey(propertyName);
                        } else {
                            if (column == null)
                                column = dataGrid.addColumn(propertyName);
                        }
                        configureDefaults(propertyName, column, propertyDefinition);
                        if (i == 0 || i == visibleProperties().length - 1) {
                            column.setFlexGrow(1);
                            column.setAutoWidth(true);
                        }
                        if (isGridFilterEnabled()) {
                            addFilteredColumn(column, propertyDefinition);
                        }
                        decorateColumn(propertyName, column);
                        SubMenu columnsSubMenu = columnsMenuItem.getSubMenu();
                        MenuItem columnMenuItem = columnsSubMenu.addItem(propertyDefinition.getCaption(), cl -> {
                            boolean checked = cl.getSource().isChecked();
                            cl.getSource().setChecked(checked);
                            columnByKey = dataGrid.getColumnByKey(propertyName);
                            columnByKey.setVisible(checked);
                        });
                        columnMenuItem.setCheckable(true);
                        columnMenuItem.setChecked(true);
                    } catch (Exception ex) {
                        log.warn(propertyName + " error: " + ex.getMessage());
                    }
                    if (column != null) {
                        columns.add(column);
                    }
                }
                dataGrid.setColumnOrder(columns);
                Column<T> endColumn = dataGrid.addComponentColumn(source -> new Span());
                endColumn.setResizable(false);
            }

            mainLayout.addToPrimary(dataGrid);

            add(mainLayout);

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
                editMenuItem.setEnabled(selected == 1);
                deleteMenuItem.setEnabled(selected > 0);
                if (onSelection != null) {
                    Boolean value = onSelection.apply(sl.getAllSelectedItems());
                    if (value != null && value) {
                        onGridItemSelect(sl);
                    }
                } else {
                    onGridItemSelect(sl);
                }
            });

            dataGrid.addItemClickListener(icl -> {
                openForm(icl.getItem());
            });

            decorateGrid(dataGrid);

            postBuild();
            isBuilt = true;
        }
        return this;
    }

    protected Grid<T> dataGrid(Class<T> entityClass) {
        Grid<T> dataGrid = new Grid<>(entityClass, true);
        List<Column<T>> removeList = new ArrayList<>(dataGrid.getColumns());
        for (String key : visibleProperties()) {
            Column<T> column = dataGrid.getColumnByKey(key);
            if (column != null) {
                column.setVisible(true);
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
        addMenuItem.addClickListener(cl -> {
            try {
                openForm(entityClass.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                Notification.show(e.getMessage(), 3000, Position.BOTTOM_CENTER);
            }
        });
    }

    protected void customizeEditMenuItem(MenuItem editMenuItem) {
        editMenuItem.addClickListener(cl -> {
            if (dataGrid.getSelectedItems().size() == 1) {
                T entity = dataGrid.getSelectedItems().iterator().next();
                openForm(entity);
            }
        });
    }

    protected void customizeDeleteMenuItem(MenuItem deleteMenuItem) {
        deleteMenuItem.addClickListener(cl -> {
            try {
                if (shouldShowDeleteConfirmation()) {
                    ConfirmDialog.createQuestion().withCaption("Confirmation").withMessage("Are you sure to delete selected record(s)?").withOkButton(() -> {
                        onDelete(dataGrid.getSelectedItems());
                        refresh();
                        editMenuItem.setEnabled(false);
                        deleteMenuItem.setEnabled(false);
                        dataGrid.deselectAll();
                    }, ButtonOption.focus(), ButtonOption.caption("YES")).withCancelButton(ButtonOption.caption("NO")).open();
                } else {
                    onDelete(dataGrid.getSelectedItems());
                    refresh();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Notification.show(e.getMessage(), 3000, Position.BOTTOM_CENTER);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void addFilteredColumn(Column<T> column, PropertyDefinition<T, ?> propertyDefinition) {
        if (dataGrid.getDataProvider() instanceof InMemoryDataProvider) {
            InMemoryDataProvider<T> dataProvider = (InMemoryDataProvider<T>) dataGrid.getDataProvider();
            TextField filterTF = new TextField();
            filterTF.setClearButtonVisible(true);
            filterTF.addValueChangeListener(event -> {
                dataProvider.addFilter(fe -> {
                    Object value = propertyDefinition.getGetter().apply(fe);
                    if (value != null) {
                        return StringUtils.containsIgnoreCase(value.toString(), filterTF.getValue());
                    }
                    return false;
                });
                dataProvider.refreshAll();
            });

            if (headerRow == null) {
                headerRow = dataGrid.appendHeaderRow();
            }

            headerRow.getCell(column).setComponent(filterTF);
            filterTF.setValueChangeMode(ValueChangeMode.EAGER);
            filterTF.setPlaceholder(propertyDefinition.getCaption());
        }
    }

    protected void decorateMenuBar(MenuBar menuBar) {
    }

    protected void decorateGrid(Grid<T> dataGrid) {
    }

    @SuppressWarnings("unchecked")
    private Renderer<T> defaultRendererForProperty(String propertyName, PropertyDefinition<T, ?> propertyDefinition) {
        Renderer<T> renderer = rendererForProperty(propertyName, propertyDefinition);
        if (renderer == null) {
            if (propertyName.matches("date.*|.*Date")) {
                if (propertyDefinition.getType().equals(Long.class)) {
                    renderer = new GxLongToDateRenderer<>((ValueProvider<T, Long>) propertyDefinition.getGetter(), TRCalendarUtil.dateFormatter.toLocalizedPattern());
                } else if (renderer == null && propertyDefinition.getType().equals(Timestamp.class)) {
                    renderer = new GxTimestampRenderer<>((ValueProvider<T, Timestamp>) propertyDefinition.getGetter(), TRCalendarUtil.dateFormatter.toLocalizedPattern());
                }
            }
            if (renderer == null && propertyName.matches("dateTime.*|.*DateTime")) {
                if (propertyDefinition.getType().equals(Long.class)) {
                    renderer = new GxLongToDateTimeRenderer<>((ValueProvider<T, Long>) propertyDefinition.getGetter(), TRCalendarUtil.dateTimeFormatter.toLocalizedPattern());
                }
            }
            if (renderer == null && propertyName.matches("time.*|.*Time")) {
                if (propertyDefinition.getType().equals(Long.class)) {
                    renderer = new GxLongToDateTimeRenderer<>((ValueProvider<T, Long>) propertyDefinition.getGetter(), TRCalendarUtil.timeFormatter.toLocalizedPattern());
                } else if (renderer == null && propertyDefinition.getType().equals(Timestamp.class)) {
                    renderer = new GxTimestampRenderer<>((ValueProvider<T, Timestamp>) propertyDefinition.getGetter(), TRCalendarUtil.timeFormatter.toLocalizedPattern());
                }
            }
            if (renderer == null && propertyDefinition.getType().equals(Timestamp.class)) {
                renderer = new GxTimestampRenderer<>((ValueProvider<T, Timestamp>) propertyDefinition.getGetter(), TRCalendarUtil.dateTimeFormatter.toLocalizedPattern());
            }
            if (renderer == null && propertyDefinition.getType().equals(Date.class)) {
                renderer = new GxDateRenderer<>((ValueProvider<T, Date>) propertyDefinition.getGetter(), TRCalendarUtil.dateFormatter.toLocalizedPattern());
            }
            if (renderer == null && propertyDefinition.getType().equals(Boolean.class)) {
                renderer = TemplateRenderer.<T> of("<vaadin-checkbox checked=[[item.value]] disabled=true />").withProperty("value", propertyDefinition.getGetter());
            }
            if (renderer == null && propertyDefinition.getType().getSuperclass().equals(Number.class)) {
                DecimalFormat numberFormat = new DecimalFormat();
                numberFormat.setRoundingMode(RoundingMode.CEILING);
                numberFormat.setGroupingUsed(true);
                numberFormat.setGroupingSize(3);
                renderer = new NumberRenderer<>((ValueProvider<T, Number>) propertyDefinition.getGetter(), numberFormat);
            }
        }
        return renderer;
    }

    protected Renderer<T> rendererForProperty(String propertyName, PropertyDefinition<T, ?> propertyDefinition) {
        return null;
    }

    protected void configureDefaults(String propertyName, Column<T> column, PropertyDefinition<T, ?> propertyDefinition) {
        column.setId(propertyName);
        column.setHeader(propertyDefinition.getCaption());
        column.setResizable(true);
        if (propertyDefinition != null) {
            if (propertyDefinition.getType().getSuperclass().equals(Number.class)) {
                column.setTextAlign(ColumnTextAlign.END);
            }
            if (propertyDefinition.getType().equals(String.class)) {
                column.setTextAlign(ColumnTextAlign.START);
            }
        }
    }

    protected void decorateColumn(String propertyName, Column<T> column) {
    }

    protected void postBuild() {
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isEditable() {
        return editable;
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
        GxAbstractEntityForm<T> entityForm = cachedForm(entity);
        if (entityForm != null) {
            if (!shouldShowFormInDialog()) {
                formLayout.removeAll();
                formLayout.add(entityForm);
                mainLayout.getSecondaryComponent().setVisible(true);
            } else {
                dialog = entityForm.showInDialog(entity);
            }
        } else {
            if (mainLayout.getSecondaryComponent() != null)
                mainLayout.getSecondaryComponent().setVisible(false);
        }
    }

    protected void preEdit(T entity) {
    }

    protected abstract Stream<T> getData();

    protected abstract String[] visibleProperties();

    public void refresh() {
        build();
        crudMenuBar.setVisible(isEditable());
        if (!shouldShowFormInDialog()) {
            if (mainLayout.getSecondaryComponent() != null)
                mainLayout.getSecondaryComponent().setVisible(false);
        }
        DataProvider<T, ?> dataProvider = dataGrid.getDataProvider();
        if (dataProvider instanceof InMemoryDataProvider) {
            items.clear();
            items.addAll(getData().collect(Collectors.toList()));
            dataProvider.refreshAll();
        } else {
            dataProvider.refreshAll();
        }
    }

    protected abstract GxAbstractEntityForm<T> getEntityForm(T entity);

    /**
     * TODO: Need to improve caching of form.
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
            entityForm.setEntity(entity);
            EntityFormDelegate<T> delegate = new EntityFormDelegate<T>() {

                @Override
                public void onSave(T entity) {
                    GxAbstractEntityList.this.onSave(entity);
                    if (!shouldShowFormInDialog()) {
                        mainLayout.getSecondaryComponent().setVisible(false);
                    } else {
                        dialog.close();
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

    protected boolean shouldShowFormInDialog() {
        return false;
    }

    protected boolean shouldShowDeleteConfirmation() {
        return true;
    }

    protected boolean isGridFilterEnabled() {
        return true;
    }

    protected abstract void onSave(T entity);

    protected abstract void onDelete(Collection<T> entities);

    public GxAbstractEntityList<T> withSearchForm(GxAbstractSearchForm<?> searchForm) {
        setSearchForm(searchForm);
        return this;
    }

    public void setSearchForm(GxAbstractSearchForm<?> searchForm) {
        MenuItem searchMenuItem = customMenuBar.addItem("Search");
        searchMenuItem.add(new Icon(VaadinIcon.SEARCH));
        MenuItem clearSearchMenuItem = customMenuBar.addItem("Clear");
        clearSearchMenuItem.add(new Icon(VaadinIcon.CLOSE));
        clearSearchMenuItem.setEnabled(false);

        searchMenuItem.addClickListener(cl -> {
            try {
                searchForm.showInDialog();
                clearSearchMenuItem.setEnabled(true);
                searchForm.getDismissButton().addClickListener(l -> {
                    clearSearchMenuItem.setEnabled(false);
                });
            } catch (Exception e) {
                Notification.show(e.getMessage(), 3000, Position.BOTTOM_CENTER);
            }
        });

        clearSearchMenuItem.addClickListener(cl -> {
            try {
                clearSearchMenuItem.setEnabled(false);
                onClearSearchFilter();
            } catch (Exception e) {
                Notification.show(e.getMessage(), 3000, Position.BOTTOM_CENTER);
            }
        });
    }

    protected void onClearSearchFilter() {
    }

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
        mainLayout.setSplitterPosition(20.0);
        mainLayout.getSecondaryComponent().setVisible(true);
    }

    public void hideSecondaryComponent() {
        mainLayout.setSplitterPosition(0.0);
        if (formLayout != null) {
            formLayout.removeAll();
        }
    }

    public Dialog showInDialog() {
        dialog = new Dialog(GxAbstractEntityList.this);
        dialog.setMaxHeight("90%");
        dialog.setMaxWidth("90%");
        dialog.setModal(true);
        dialog.setCloseOnEsc(true);
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.setSizeFull();
        dialog.open();
        return dialog;
    }

    public void dismissDialog() {
        if (dialog != null) {
            dialog.close();
        }
    }

}
