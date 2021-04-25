package io.graphenee.vaadin.flow.base;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;

import lombok.extern.log4j.Log4j;

@Log4j
public abstract class GxAbstractSearchForm<T> extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    private Component entityForm;
    private Component toolbar;
    private Button searchButton;
    private Button resetButton;
    private Button dismissButton;

    private Binder<T> dataBinder;
    private Class<T> entityClass;
    private T entity;

    private SearchFormDelegate<T> delegate;

    private Dialog dialog = null;

    private boolean editable = true;

    private boolean isBuilt = false;

    public GxAbstractSearchForm(Class<T> entityClass) {
        this.entityClass = entityClass;
        setSizeFull();
        setMargin(false);
        setPadding(false);
        addClassName("gx-abstract-search-form");
    }

    synchronized public GxAbstractSearchForm<T> build() {
        if (!isBuilt) {
            entityForm = getFormComponent();

            if (entityForm instanceof HasComponents) {
                decorateForm((HasComponents) entityForm);
            }

            add(entityForm);

            toolbar = getToolbarComponent();

            if (toolbar instanceof HasComponents) {
                HasComponents c = (HasComponents) toolbar;
                decorateToolbar(c);
                searchButton = new Button("SEARCH");
                searchButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                searchButton.addClickListener(cl -> {
                    try {
                        dataBinder.writeBean(entity);
                        if (dialog != null) {
                            dialog.close();
                        }
                        if (delegate != null)
                            delegate.onSearch(entity);
                    } catch (Exception e) {
                        if (e.getMessage() != null)
                            Notification.show(e.getMessage(), 3000, Position.BOTTOM_CENTER);
                        e.printStackTrace();
                    }
                });

                resetButton = new Button("RESET");
                resetButton.addClickListener(cl -> {
                    try {
                        dataBinder.readBean(entity);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Notification.show(e.getMessage(), 3000, Position.BOTTOM_CENTER);
                    }
                });

                dismissButton = new Button("DISMISS");
                dismissButton.addClickShortcut(Key.ESCAPE);
                dismissButton.addClickListener(cl -> {
                    dataBinder.readBean(entity);
                    if (dialog != null) {
                        dialog.close();
                    }
                });

                Span spacer = new Span();

                c.add(searchButton, spacer, resetButton, dismissButton);
                if (c instanceof FlexComponent) {
                    FlexComponent<?> fc = (FlexComponent<?>) c;
                    fc.setFlexGrow(2.0, spacer);
                }

            }

            add(toolbar);

            dataBinder = new Binder<>(entityClass, true);
            bindFields(dataBinder);
            try {
                dataBinder.bindInstanceFields(GxAbstractSearchForm.this);
            } catch (Exception ex) {
                log.warn(ex.getMessage());
            }

            postBuild();
            isBuilt = true;
        }
        return this;
    }

    protected void postBuild() {
    }

    protected abstract void decorateForm(HasComponents entityForm);

    protected void decorateToolbar(HasComponents toolbar) {
    }

    protected void bindFields(Binder<T> dataBinder) {
    }

    protected Component getToolbarComponent() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setClassName("gx-footer");
        toolbar.getStyle().set("border-radius", "var(--lumo-border-radius)");
        toolbar.setWidthFull();
        toolbar.setPadding(false);
        return toolbar;
    }

    protected Component getFormComponent() {
        FormLayout formLayout = new FormLayout();
        formLayout.setSizeFull();
        return formLayout;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        if (searchButton != null) {
            searchButton.setEnabled(editable);
        }
    }

    public boolean isEditable() {
        return editable;
    }

    public Button getDismissButton() {
        return dismissButton;
    }

    public void setEntity(T entity) {
        build();
        this.entity = entity;
        dataBinder.readBean(entity);
    }

    public Dialog showInDialog() {
        dialog = new Dialog(GxAbstractSearchForm.this);
        dialog.setMaxHeight("90%");
        dialog.setMaxWidth("90%");
        dialog.setModal(true);
        dialog.setCloseOnEsc(true);
        dialog.setDraggable(true);
        dialog.setSizeFull();
        dialog.open();
        return dialog;
    }

    public void setSearchFormDelegate(SearchFormDelegate<T> delegate) {
        this.delegate = delegate;
    }

    public interface SearchFormDelegate<T> {
        void onSearch(T entity);
    }

}
