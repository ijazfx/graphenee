package io.graphenee.vaadin.flow.base;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
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
import com.vaadin.flow.data.binder.BeanPropertySet;
import com.vaadin.flow.data.binder.PropertyDefinition;
import com.vaadin.flow.data.binder.PropertySet;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.function.ValueProvider;

import org.claspina.confirmdialog.ButtonOption;
import org.claspina.confirmdialog.ConfirmDialog;

import io.graphenee.core.util.TRCalendarUtil;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm.EntityFormDelegate;
import io.graphenee.vaadin.flow.renderer.GxDateRenderer;
import io.graphenee.vaadin.flow.renderer.GxLongToDateRenderer;
import io.graphenee.vaadin.flow.renderer.GxLongToDateTimeRenderer;
import io.graphenee.vaadin.flow.renderer.GxTimestampRenderer;

@CssImport("./styles/gx-list.css")
public abstract class GxAbstractEntityList<T> extends Div {

    private static final long serialVersionUID = 1L;

    private SplitLayout mainLayout;
    private Dialog dialog;
    private Grid<T> dataGrid;
    private Class<T> entityClass;
    private Map<T, GxAbstractEntityForm<T>> formCache = new HashMap<>();
    private GxAbstractSearchForm<?> searchForm = null;
    private CallbackDataProvider<T, Void> dataProvider;

    private boolean isBuilt = false;

    private boolean editable = true;

    private MenuItem addMenuItem;

    private MenuItem editMenuItem;

    private MenuItem deleteMenuItem;

    private MenuItem searchMenuItem;

    private MenuItem clearSearchMenuItem;

    private MenuBar menuBar;

    private Function<Collection<T>, Boolean> onSelection;

    private HorizontalLayout menuBarLayout;

    private VerticalLayout formLayout;

    public GxAbstractEntityList(Class<T> entityClass) {
        this.entityClass = entityClass;
        setClassName("gx-list");
        setSizeFull();
    }

    synchronized private GxAbstractEntityList<T> build() {
        if (!isBuilt) {
            dataGrid = new Grid<>(entityClass, false);
            // dataGrid.setPageSize(20);
            dataGrid.setClassName("gx-grid");
            ((GridMultiSelectionModel<?>) dataGrid.setSelectionMode(SelectionMode.MULTI)).setSelectionColumnFrozen(true);
            decorateGrid(dataGrid);

            //    filterText.addValueChangeListener(e -> updateList());
            dataProvider = DataProvider.fromCallbacks(query -> getPagedData(query.getOffset(), query.getLimit()), query -> getTotalCount());
            dataGrid.setDataProvider(dataProvider);
            dataGrid.getColumns().forEach(column -> {
                column.setVisible(false);
            });

            dataGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

            if (visibleProperties() != null && visibleProperties().length > 0) {
                PropertySet<T> propertySet = BeanPropertySet.get(entityClass);
                for (int i = 0; i < visibleProperties().length; i++) {
                    String propertyName = visibleProperties()[i];
                    Column<T> column;
                    PropertyDefinition<T, ?> propertyDefinition = propertySet.getProperty(propertyName).get();
                    Renderer<T> renderer = defaultRendererForProperty(propertyName, propertyDefinition);
                    if (renderer != null) {
                        column = dataGrid.addColumn(renderer);
                    } else {
                        column = dataGrid.addColumn(propertyName);
                    }
                    configureDefaults(propertyName, column, propertyDefinition);
                    if (i == 0) {
                        column.setFrozen(true);
                        column.setFlexGrow(0);
                        column.setWidth("20em");
                        column.setAutoWidth(false);
                    }
                    decorateColumn(propertyName, column);
                }
                dataGrid.addComponentColumn(source -> new Span());
            }

            mainLayout = new SplitLayout();
            mainLayout.setSizeFull();

            menuBar = new MenuBar();
            menuBar.setWidthFull();
            menuBarLayout = new HorizontalLayout();
            menuBarLayout.setPadding(true);
            menuBarLayout.add(menuBar);

            add(menuBarLayout);
            menuBar.addThemeVariants(MenuBarVariant.MATERIAL_OUTLINED);

            addMenuItem = menuBar.addItem("Add");
            editMenuItem = menuBar.addItem("Edit");
            deleteMenuItem = menuBar.addItem("Delete");
            searchMenuItem = menuBar.addItem("Search");
            searchMenuItem.add(new Icon(VaadinIcon.SEARCH));
            clearSearchMenuItem = menuBar.addItem("Clear");
            clearSearchMenuItem.add(new Icon(VaadinIcon.CLOSE));
            setClearSearchFilterEnable(false);

            editMenuItem.setEnabled(false);
            deleteMenuItem.setEnabled(false);

            searchMenuItem.setVisible(false);
            clearSearchMenuItem.setVisible(false);

            addMenuItem.addClickListener(cl -> {
                try {
                    openForm(entityClass.getDeclaredConstructor().newInstance());
                } catch (Exception e) {
                    Notification.show(e.getMessage(), 3000, Position.BOTTOM_CENTER);
                }
            });

            editMenuItem.addClickListener(cl -> {
                if (dataGrid.getSelectedItems().size() == 1) {
                    T entity = dataGrid.getSelectedItems().iterator().next();
                    openForm(entity);
                }
            });

            deleteMenuItem.addClickListener(cl -> {
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
            });

            searchMenuItem.addClickListener(cl -> {
                try {
                    searchForm.showInDialog();
                } catch (Exception e) {
                    Notification.show(e.getMessage(), 3000, Position.BOTTOM_CENTER);
                }
            });

            clearSearchMenuItem.addClickListener(cl -> {
                try {
                    onClearSearchFilter();
                    refresh();
                } catch (Exception e) {
                    Notification.show(e.getMessage(), 3000, Position.BOTTOM_CENTER);
                }
            });

            mainLayout.addToPrimary(dataGrid);
            decorateMenuBar(menuBar);

            add(mainLayout);

            if (!shouldShowFormInDialog()) {
                formLayout = new VerticalLayout();
                formLayout.setPadding(true);
                mainLayout.addToSecondary(formLayout);
                mainLayout.getSecondaryComponent().setVisible(false);
                mainLayout.setSplitterPosition(defaultSplitterPosition());
            } else {
                mainLayout.setSplitterPosition(100);
            }

            dataGrid.addSelectionListener(sl -> {
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

            postBuild();
            isBuilt = true;
        }
        return this;
    }

    protected void decorateMenuBar(MenuBar menuBar) {
    }

    protected void decorateGrid(Grid<T> dataGrid) {
    }

    @SuppressWarnings("unchecked")
    private Renderer<T> defaultRendererForProperty(String propertyName, PropertyDefinition<T, ?> propertyDefinition) {
        Renderer<T> renderer = rendererForProperty(propertyName, propertyDefinition);
        if (renderer == null) {
            if (propertyDefinition.getType().equals(Timestamp.class)) {
                renderer = new GxTimestampRenderer<>((ValueProvider<T, Timestamp>) propertyDefinition.getGetter(), TRCalendarUtil.dateTimeFormatter.toLocalizedPattern());
            }
            if (propertyDefinition.getType().equals(Date.class)) {
                renderer = new GxDateRenderer<>((ValueProvider<T, Date>) propertyDefinition.getGetter(), TRCalendarUtil.dateFormatter.toLocalizedPattern());
            }
            if (propertyName.matches("date.*|.*Date")) {
                if (propertyDefinition.getType().equals(Long.class)) {
                    renderer = new GxLongToDateRenderer<>((ValueProvider<T, Long>) propertyDefinition.getGetter(), TRCalendarUtil.dateFormatter.toLocalizedPattern());
                }
            }
            if (propertyName.matches("dateTime.*|.*DateTime")) {
                if (propertyDefinition.getType().equals(Long.class)) {
                    renderer = new GxLongToDateTimeRenderer<>((ValueProvider<T, Long>) propertyDefinition.getGetter(), TRCalendarUtil.dateTimeFormatter.toLocalizedPattern());
                }
            }
            if (propertyName.matches("time.*|.*Time")) {
                if (propertyDefinition.getType().equals(Long.class)) {
                    renderer = new GxLongToDateTimeRenderer<>((ValueProvider<T, Long>) propertyDefinition.getGetter(), TRCalendarUtil.timeFormatter.toLocalizedPattern());
                }
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
                column.setFlexGrow(0);
                column.setTextAlign(ColumnTextAlign.END);
                column.setAutoWidth(true);
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
        if (addMenuItem != null) {
            addMenuItem.setEnabled(editable);
        }
        if (editMenuItem != null) {
            editMenuItem.setEnabled(editable);
        }
        if (deleteMenuItem != null) {
            deleteMenuItem.setEnabled(editable);
        }
    }

    public boolean isEditable() {
        return editable;
    }

    public void shouldShowToolbar(boolean toolbar) {
        if (menuBar != null) {
            menuBar.setVisible(toolbar);
        }
    }

    /**
     * Default value is 20.0 which is 20% for primary container and 60% for
     * secondary container.
     * 
     * @return
     */
    protected double defaultSplitterPosition() {
        return 20.0;
    }

    private void openForm(T entity) {
        preEdit(entity);
        GxAbstractEntityForm<T> entityForm = cachedForm(entity);
        if (entityForm != null) {
            entityForm.setEditable(isEditable());
            if (!shouldShowFormInDialog()) {
                formLayout.removeAll();
                formLayout.add(entityForm);
                mainLayout.getSecondaryComponent().setVisible(true);
            } else {
                dialog = entityForm.showInDialog(entity);
            }
        } else {
            mainLayout.getSecondaryComponent().setVisible(false);
        }
    }

    // public void updateList() {
    //     filterText.addValueChangeListener(
    //             event -> dataGrid.getDataProvider().addFilter(filterableEntity -> StringUtils.containsIgnoreCase(filterableValue.apply(filterableEntity), filterTF.getValue())));

    //     Column<T> column = dataGrid.addColumn(new TextRenderer<>(itemLabelGenerator)).setHeader(header);
    //     dataGrid.getCell(column).setComponent(filterText);
    //     filterText.setValueChangeMode(ValueChangeMode.EAGER);
    //     filterText.setSizeFull();
    //     filterText.setPlaceholder(filterPlaceholder);
    //     // grid.setItems(service.findAll(filterText.getValue()));
    // }

    protected void preEdit(T entity) {
    }

    protected abstract int getTotalCount();

    private Stream<T> getPagedData(int offset, int limit) {
        int pageNumber = offset / limit;
        int remainder = offset % limit == 0 ? 0 : offset - (pageNumber * limit);
        int pageSize = limit;
        Stream<T> stream = getData(pageNumber, pageSize);
        if (remainder != 0) {
            Stream<T> nextStream = getData(pageNumber + 1, pageSize);
            stream = Stream.concat(stream, nextStream).skip(remainder).limit(limit);
        }
        return stream;
    }

    protected abstract Stream<T> getData(int pageNumber, int pageSize);

    protected abstract String[] visibleProperties();

    public void refresh() {
        build();
        if (!shouldShowFormInDialog()) {
            mainLayout.getSecondaryComponent().setVisible(false);
        }
        if (dataProvider != null) {
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
            entityForm.setEntity(entity);
            entityForm.setDelegate(new EntityFormDelegate<T>() {

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
            });
        }
        return entityForm;
    }

    protected boolean shouldShowFormInDialog() {
        return false;
    }

    protected void setDefaultColumnAlignment(String propertyName, Column<T> column) {
        if (propertyName.matches("(is|should|has)[A-Z].*")) {
            column.setTextAlign(ColumnTextAlign.START);
        }
        if (propertyName.matches("(quantity|capacity|time|weight|code|ratio|days|cycle|cost|price|sum|amount|percent|day)")) {
            column.setTextAlign(ColumnTextAlign.END);
        }
        if (propertyName.matches("(quantity|capacity|time|weight|code|ratio|days|cost|price|sum|amount|percent|day)[A-Z].*")) {
            column.setTextAlign(ColumnTextAlign.END);
        }
        if (propertyName.matches(".*(Quantity|Capacity|Time|Weight|Ratio|Days|Cycle|Cost|Price|Sum|Amount|percent|Day)")) {
            column.setTextAlign(ColumnTextAlign.END);
        }
    }

    protected boolean shouldShowDeleteConfirmation() {
        return true;
    }

    protected abstract void onSave(T entity);

    protected abstract void onDelete(Collection<T> entities);

    protected Component getPrimaryToolbarComponent() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setPadding(false);
        return toolbar;
    }

    protected Component getSecondaryToolbarComponent() {
        HorizontalLayout toolbar = new HorizontalLayout();
        return toolbar;
    }

    public GxAbstractEntityList<T> withSearchForm(GxAbstractSearchForm<?> searchForm) {
        setSearchForm(searchForm);
        return this;
    }

    public void setSearchForm(GxAbstractSearchForm<?> searchForm) {
        this.searchForm = searchForm;
        if (searchMenuItem != null) {
            searchMenuItem.setVisible(searchForm != null);
        }
        if (clearSearchMenuItem != null) {
            clearSearchMenuItem.setVisible(searchForm != null);
            clearSearchMenuItem.setEnabled(false);
        }
    }

    protected void onClearSearchFilter() {
        setClearSearchFilterEnable(false);
    }

    public void setClearSearchFilterEnable(Boolean value) {
        clearSearchMenuItem.setEnabled(value);
    }

    protected Grid<T> entityGrid() {
        return dataGrid;
    }

    protected void onGridItemSelect(SelectionEvent<Grid<T>, T> event) {
        int selected = event.getAllSelectedItems().size();
        editMenuItem.setEnabled(selected == 1);
        deleteMenuItem.setEnabled(selected > 0);
    }

    @SuppressWarnings("unchecked")
    public <R extends GxAbstractEntityList<T>> R withSelection(Function<Collection<T>, Boolean> onSelection) {
        this.onSelection = onSelection;
        return (R) this;
    }

    protected void setAddMenuItemVisibility(boolean visibility) {
        if (addMenuItem != null) {
            addMenuItem.setVisible(visibility);
        }
    }

    protected void setEditMenuItemVisibility(boolean visibility) {
        if (editMenuItem != null) {
            editMenuItem.setVisible(visibility);
        }
    }

    protected void setDeleteMenuItemVisibility(boolean visibility) {
        if (deleteMenuItem != null) {
            deleteMenuItem.setVisible(visibility);
        }
    }

    protected void hideMenuBar() {
        if (menuBarLayout != null) {
            menuBarLayout.setVisible(false);
        }
    }
}
