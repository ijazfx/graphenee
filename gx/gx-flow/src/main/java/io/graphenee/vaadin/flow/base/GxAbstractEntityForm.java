package io.graphenee.vaadin.flow.base;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

import io.graphenee.core.util.KeyValueWrapper;
import io.graphenee.vaadin.flow.component.DialogVariant;
import io.graphenee.vaadin.flow.component.GxDialog;
import lombok.Setter;

public abstract class GxAbstractEntityForm<T> extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private HorizontalLayout formTitleLayout;
	private Label formTitleLabel;
	private Component entityForm;
	private Component toolbar;
	private Button saveButton;
	private Button resetButton;
	private Button dismissButton;

	private Binder<T> dataBinder;
	private Class<T> entityClass;
	private T entity;
	private Tabs tabs;

	private boolean entityBound = false;

	private boolean isBuilt = false;

	private EntityFormDelegate<T> delegate;

	private boolean editable = true;

	private GxDialog dialog = null;

	@Setter
	private Boolean dialogAutoClose = true;

	public GxAbstractEntityForm(Class<T> entityClass) {
		this.entityClass = entityClass;
		setSizeFull();
		setMargin(false);
		setPadding(false);
		setSpacing(false);
	}

	private synchronized GxAbstractEntityForm<T> build() {
		buildFormTitle();
		if (!isBuilt) {
			entityForm = getFormComponent();
			if (entityForm instanceof HasComponents) {
				decorateForm((HasComponents) entityForm);
			}

			toolbar = getToolbarComponent();

			if (toolbar instanceof HasComponents) {
				HasComponents c = (HasComponents) toolbar;
				decorateToolbar(c);
				saveButton = new Button("SAVE");
				saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
				saveButton.addClickListener(cl -> {
					if (entity != null) {
						try {
							validateForm();
							if (delegate != null)
								delegate.onSave(entity);
							if (dialog != null && dialogAutoClose) {
								dialog.close();
							}
						} catch (Exception e) {
							// e.printStackTrace();
							// Notification.show(e.getMessage(), 3000, Position.BOTTOM_CENTER);
						}
					}
				});

				customizeSaveButton(saveButton);

				resetButton = new Button("RESET");
				resetButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
				resetButton.addClickListener(cl -> {
					dataBinder.readBean(entity);
					if (delegate != null)
						delegate.onReset(entity);
				});

				dismissButton = new Button("DISMISS");
				dismissButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
				dismissButton.addClickListener(cl -> {
					if (dialog != null) {
						dialog.close();
					}
					if (delegate != null)
						delegate.onDismiss(entity);
				});

				resetButton.getElement().getStyle().set("margin-left", "auto");
				c.add(saveButton, resetButton, dismissButton);

			}

			Scroller scroller = new Scroller();
			scroller.setSizeFull();
			VerticalLayout entityFormLayout = new VerticalLayout(entityForm);
			entityFormLayout.setSizeFull();
			entityFormLayout.setMargin(false);
			entityFormLayout.setSpacing(false);
			entityFormLayout.setPadding(true);
			scroller.setContent(entityFormLayout);

			List<GxTabItem> tabItems = new ArrayList<>();
			addTabsToForm(tabItems);
			tabItems.add(GxTabItem.create(0, defaultTabTitle(), scroller));

			if (tabItems.size() >= 2) {
				addTab(tabItems);
			} else {
				add(scroller);
			}

			add(toolbar);

			dataBinder = new Binder<>(entityClass, true);
			bindFields(dataBinder);
			dataBinder.bindInstanceFields(GxAbstractEntityForm.this);
			postBuild();

			isBuilt = true;
		}
		return this;

	}

	public void validateForm() throws ValidationException {
		dataBinder.validate();
		dataBinder.writeBean(entity);
	}

	private void buildFormTitle() {
		if (formTitleLabel == null) {
			formTitleLabel = new Label();
			formTitleLabel.getStyle().set("background-color", "var(--lumo-primary-color-10pct)");
			formTitleLabel.getStyle().set("font-weight", "bold");
			formTitleLabel.getStyle().set("color", "var(--lumo-primary-color)");
			formTitleLabel.getStyle().set("border-bottom", "none");
			formTitleLabel.getStyle().set("border-radius", "var(--lumo-border-radius)");
			formTitleLabel.getStyle().set("border-bottom-right-radius", "0px");
			formTitleLabel.getStyle().set("border-bottom-left-radius", "0px");
			formTitleLabel.getStyle().set("padding-left", "0.5em");
			formTitleLabel.getStyle().set("padding-right", "0.5em");
			formTitleLabel.getStyle().set("padding-top", "0.25em");
			formTitleLayout = new HorizontalLayout();
			formTitleLayout.getStyle().set("border-bottom", "2px solid var(--lumo-primary-color-10pct)");
			formTitleLayout.getStyle().set("padding-left", "0.5em");
			formTitleLayout.getStyle().set("padding-top", "0.5em");
			formTitleLayout.setWidthFull();
			formTitleLayout.add(formTitleLabel);
			addComponentAsFirst(formTitleLayout);
		}
	}

	protected String defaultTabTitle() {
		return "Details";
	}

	protected void customizeSaveButton(Button saveButton) {
	}

	protected String formTitle() {
		return null;
	}

	protected String formTitleProperty() {
		return null;
	}

	protected void preBinding(T entity) {
	}

	protected void postBinding(T entity) {
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
		toolbar.getStyle().set("border-radius", "var(--lumo-border-radius)");
		toolbar.getStyle().set("border-top-right-radius", "0px");
		toolbar.getStyle().set("border-top-left-radius", "0px");
		toolbar.getStyle().set("background-color", "#F8F8F8");
		toolbar.setWidthFull();
		toolbar.setPadding(true);
		toolbar.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
		return toolbar;
	}

	protected Component getFormComponent() {
		FormLayout formLayout = new FormLayout();
		formLayout.setSizeFull();
		return formLayout;
	}

	protected void setColspan(Component c, int colspan) {
		Component fc = getFormComponent();
		if (fc instanceof FormLayout) {
			FormLayout form = (FormLayout) fc;
			form.setColspan(c, colspan);
		}
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEntity(T entity) {
		this.entity = entity;
		build();
		entityBound = false;
		preBinding(entity);
		dataBinder.readBean(entity);
		entityBound = true;
		if (tabs != null) {
			tabs.setSelectedTab((Tab) tabs.getComponentAt(0));
		}
		postBinding(entity);
		String formTitle = formTitle();
		if (formTitle == null) {
			String keyPath = formTitleProperty();
			if (keyPath != null && !keyPath.isBlank()) {
				formTitle = new KeyValueWrapper(entity).stringForKeyPath(keyPath);
			}
		}
		formTitleLabel.setText(formTitle);
		if (shouldFocusFirstFieldOnShow()) {
			focusFirst(this);
		}
		saveButton.setVisible(isEditable());
		resetButton.setVisible(isEditable());
	}

	protected boolean shouldFocusFirstFieldOnShow() {
		return false;
	}

	private boolean focusFirst(Component c) {
		if (c instanceof Focusable) {
			((Focusable<?>) c).focus();
			return true;
		}
		List<Component> children = c.getChildren().collect(Collectors.toList());
		for (Component child : children) {
			if (focusFirst(child))
				return true;
		}
		return false;
	}

	public T getEntity() {
		return entity;
	}

	public GxDialog showInDialog(T entity) {
		return showInDialog(entity, dialogWidth(), dialogHeight());
	}

	protected String dialogHeight() {
		return "800px";
	}

	protected String dialogWidth() {
		return "600px";
	}

	public GxDialog showInDialog(T entity, String width, String height) {
		setEntity(entity);
		dialog = new GxDialog(GxAbstractEntityForm.this);
		dialog.addThemeVariants(DialogVariant.NO_PADDING);
		dialog.setWidth(width);
		dialog.setHeight(height);
		dialog.setResizable(true);
		dialog.setModal(true);
		dialog.setCloseOnEsc(true);
		dialog.setDraggable(true);
		dialog.setResizable(true);
		dialog.open();
		return dialog;
	}

	public void closeDialog() {
		if (dialog != null) {
			dialog.close();
		}
	}

	public void setDelegate(EntityFormDelegate<T> delegate) {
		this.delegate = delegate;
	}

	private void addTab(List<GxTabItem> tabItems) {
		Div selectedTab = new Div();
		selectedTab.setSizeFull();
		selectedTab.getStyle().set("overflow-x", "hidden");
		tabs = new Tabs();
		tabs.setWidthFull();
		Component[] tabComponents = new Component[tabItems.size()];

		tabItems.sort(Comparator.comparing(GxTabItem::getIndex));
		for (int i = 0; i < tabItems.size(); i++) {
			GxTabItem tabItem = tabItems.get(i);
			Tab tab = new Tab(tabItem.getLabel());
			tabs.add(tab);
			Component component = tabItem.getComponent();
			if (i != 0) {
				component.getElement().getStyle().set("padding", "10px");
			}
			tabComponents[i] = component;
			if (i == 0) {
				selectedTab.add(tabComponents[i]);
			}
		}

		tabs.addSelectedChangeListener(event -> {
			Integer selectedIndex = tabs.getSelectedIndex();
			Component selectedComponent = tabComponents[selectedIndex];
			selectedTab.removeAll();
			selectedTab.add(selectedComponent);
			onTabChange(tabs.getSelectedIndex(), tabs.getSelectedTab(), selectedComponent);
		});

		add(tabs, selectedTab);
	}

	protected void addTabsToForm(List<GxTabItem> tabItems) {
	}

	protected void onTabChange(Integer index, Tab tab, Component component) {
	}

	protected void setTabEnabled(Integer index, Boolean value) {
		Component c = tabs.getComponentAt(index);
		if (c instanceof Tab) {
			Tab tab = (Tab) c;
			tab.setEnabled(value);
		}
	}

	public interface EntityFormDelegate<T> {
		void onSave(T entity);

		default void onDismiss(T entity) {
		}

		default void onReset(T entity) {
		}
	}

	protected boolean isEntityBound() {
		return entityBound;
	}

}
