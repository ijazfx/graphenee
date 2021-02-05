package io.graphenee.vaadin.flow.base;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.binder.BeanPropertySet;
import com.vaadin.flow.data.binder.PropertyDefinition;
import com.vaadin.flow.data.binder.PropertySet;
import com.vaadin.flow.data.provider.hierarchy.AbstractBackEndHierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery;
import com.vaadin.flow.data.renderer.Renderer;
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
public abstract class GxAbstractEntityTreeList<T> extends Div {

    private static final long serialVersionUID = 1L;

    private SplitLayout mainLayout;
    private Class<T> entityClass;
    private GxTreeGrid<T> treeGrid;
    private Map<T, GxAbstractEntityForm<T>> formCache = new HashMap<>();
    private GxAbstractSearchForm<?> searchForm = null;
    private HierarchicalDataProvider<T, Void> dataProvider;

    private boolean isBuilt = false;

    private Dialog dialog;
    private boolean editable = true;

    private MenuItem addMenuItem;

    private MenuItem editMenuItem;

    private MenuItem deleteMenuItem;

    private MenuItem searchMenuItem;

    private MenuItem clearSearchMenuItem;

    private MenuBar menuBar;

    private VerticalLayout formLayout;

    public GxAbstractEntityTreeList(Class<T> entityClass) {
        this.entityClass = entityClass;
        setClassName("gx-list");
        setSizeFull();
    }

    synchronized private GxAbstractEntityTreeList<T> build() {
        if (!isBuilt) {
            treeGrid = new GxTreeGrid<>(entityClass, false);
            treeGrid.setClassName("gx-grid");
            ((GridMultiSelectionModel<?>) treeGrid.setSelectionMode(SelectionMode.MULTI)).setSelectionColumnFrozen(true);
            decorateGrid(treeGrid);

            dataProvider = new AbstractBackEndHierarchicalDataProvider<T, Void>() {
                private static final long serialVersionUID = 1L;

                @Override
                public int getChildCount(HierarchicalQuery<T, Void> query) {
                    return GxAbstractEntityTreeList.this.getChildCount(query.getParent());
                }

                @Override
                public boolean hasChildren(T item) {
                    return GxAbstractEntityTreeList.this.hasChildren(item);
                }

                @Override
                protected Stream<T> fetchChildrenFromBackEnd(HierarchicalQuery<T, Void> query) {
                    return getPagedData(query.getParent(), query.getOffset(), query.getLimit());
                }

            };

            treeGrid.setDataProvider(dataProvider);

            treeGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

            if (visibleProperties() != null && visibleProperties().length > 0) {
                PropertySet<T> propertySet = BeanPropertySet.get(entityClass);
                for (String propertyName : visibleProperties()) {

                    Column<T> column;
                    PropertyDefinition<T, ?> propertyDefinition = propertySet.getProperty(propertyName).get();
                    Renderer<T> renderer = defaultRendererForProperty(propertyName, propertyDefinition);
                    if (renderer != null) {
                        column = treeGrid.addColumn(renderer);
                    } else {
                        column = treeGrid.addColumn(propertyName);
                        column.setResizable(true);

                    }
                    configureDefaults(propertyName, column, propertyDefinition);
                    decorateColumn(propertyName, column);
                }
                treeGrid.setHierarchyColumn(visibleProperties()[0]);
            }

            mainLayout = new SplitLayout();
            mainLayout.setSizeFull();

            menuBar = new MenuBar();
            menuBar.setWidthFull();
            HorizontalLayout menuBarLayout = new HorizontalLayout();
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
                if (treeGrid.getSelectedItems().size() == 1) {
                    T entity = treeGrid.getSelectedItems().iterator().next();
                    openForm(entity);
                }
            });

            deleteMenuItem.addClickListener(cl -> {
                if (shouldShowDeleteConfirmation()) {
                    ConfirmDialog.createQuestion().withCaption("Confirmation").withMessage("Are you sure to delete selected record(s)?").withOkButton(() -> {
                        onDelete(treeGrid.getSelectedItems());
                        refresh();
                        editMenuItem.setEnabled(false);
                        deleteMenuItem.setEnabled(false);
                        treeGrid.deselectAll();
                    }, ButtonOption.focus(), ButtonOption.caption("YES")).withCancelButton(ButtonOption.caption("NO")).open();
                } else {
                    onDelete(treeGrid.getSelectedItems());
                    refresh();
                }
                refresh();
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

            mainLayout.addToPrimary(treeGrid);
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

            treeGrid.addSelectionListener(sl -> {
                int selected = sl.getAllSelectedItems().size();
                editMenuItem.setEnabled(selected == 1);
                deleteMenuItem.setEnabled(selected > 0);
            });

            treeGrid.addItemClickListener(icl -> {
                openForm(icl.getItem());
            });

            postBuild();
            isBuilt = true;
        }
        return this;
    }

    protected void decorateMenuBar(MenuBar menuBar) {
    }

    protected void decorateGrid(TreeGrid<T> treeGrid) {
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

    protected void decorateColumn(String propertyName, Column<T> column) {
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

    protected abstract int getChildCount(T parent);

    protected abstract boolean hasChildren(T parent);

    private Stream<T> getPagedData(T parent, int offset, int limit) {
        int pageNumber = offset / limit;
        int remainder = offset % limit == 0 ? 0 : offset - (pageNumber * limit);
        int pageSize = limit;
        Stream<T> stream = getData(parent, pageNumber, pageSize);
        if (remainder != 0) {
            Stream<T> nextStream = getData(parent, pageNumber + 1, pageSize);
            stream = Stream.concat(stream, nextStream).skip(remainder).limit(limit);
        }
        return stream;
    }

    protected abstract Stream<T> getData(T parent, int pageNumber, int pageSize);

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
     * Default value is 40.0 which is 40% for primary container and 60% for
     * secondary container.
     * 
     * @return
     */
    protected double defaultSplitterPosition() {
        return 40.0;
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

    protected void preEdit(T entity) {
    }

    protected abstract String[] visibleProperties();

    protected abstract GxAbstractEntityForm<T> getEntityForm(T entity);

    public void refresh() {
        build();
        if (!shouldShowFormInDialog()) {
            mainLayout.getSecondaryComponent().setVisible(false);
        }
        if (dataProvider != null) {
            dataProvider.refreshAll();
        }
    }

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
                    GxAbstractEntityTreeList.this.onSave(entity);
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

    public GxAbstractEntityTreeList<T> withSearchForm(GxAbstractSearchForm<?> searchForm) {
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

}
