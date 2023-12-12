package io.graphenee.vaadin.flow.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.data.binder.BeanPropertySet;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.PropertyFilterDefinition;
import com.vaadin.flow.data.binder.PropertySet;
import com.vaadin.flow.data.binder.ValidationException;

import io.graphenee.util.KeyValueWrapper;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm.GxEntityFormEventListener.GxEntityFormEvent;
import io.graphenee.vaadin.flow.component.DialogVariant;
import io.graphenee.vaadin.flow.component.GxDialog;
import io.graphenee.vaadin.flow.event.TRDelayClickListener;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class GxAbstractEntityForm<T> extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private HorizontalLayout formTitleLayout;
	private NativeLabel formTitleLabel;
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
				saveButton.addClickShortcut(Key.KEY_S, KeyModifier.ALT);
				saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
				saveButton.setDisableOnClick(true);

				saveButton.addClickListener(cl -> {
					if (entity != null) {
						try {
							validateForm();
							if (delegate != null)
								delegate.onSave(entity);
							listeners.forEach(l -> {
								l.onEvent(GxEntityFormEvent.SAVE, entity);
							});
							if (dialog != null && dialogAutoClose) {
								dialog.close();
							}
						} catch (Exception e) {
							// e.printStackTrace();
							// Notification.show(e.getMessage(), 3000, Position.BOTTOM_CENTER);
						}
					}
					saveButton.setEnabled(true);
				});

				customizeSaveButton(saveButton);

				resetButton = new Button("RESET");
				resetButton.addClickShortcut(Key.KEY_R, KeyModifier.ALT);
				resetButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

				resetButton.addClickListener(new TRDelayClickListener<Button>() {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(ClickEvent<Button> event) {
						dataBinder.readBean(entity);
						if (delegate != null)
							delegate.onReset(entity);
						listeners.forEach(l -> {
							l.onEvent(GxEntityFormEvent.RESET, entity);
						});
					}
				});
				customizeResetButton(resetButton);

				dismissButton = new Button("DISMISS");
				// dismissButton.addClickShortcut(Key.ESCAPE);
				dismissButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

				dismissButton.addClickListener(new TRDelayClickListener<Button>() {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(ClickEvent<Button> event) {
						if (dialog != null) {
							dialog.close();
						}
						if (delegate != null)
							delegate.onDismiss(entity);
						listeners.forEach(l -> {
							l.onEvent(GxEntityFormEvent.DISMISS, entity);
						});
					}
				});

				saveButton.getElement().getStyle().set("margin-left", "auto");
				customizeDismissButton(dismissButton);
				c.add(dismissButton, resetButton, saveButton);

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

			PropertySet<T> bps = BeanPropertySet.get(entityClass, true, new PropertyFilterDefinition(1, Arrays.asList("java")));
			dataBinder = Binder.withPropertySet(bps);
			bindFields(dataBinder);

			try {
				dataBinder.bindInstanceFields(GxAbstractEntityForm.this);
			} catch (Exception ex) {
				log.warn(ex.getMessage());
			}
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
			formTitleLabel = new NativeLabel();
			formTitleLabel.getStyle().set("background-color", "var(--lumo-primary-color-10pct)");
			formTitleLabel.getStyle().set("font-weight", "bold");
			formTitleLabel.getStyle().set("color", "var(--lumo-primary-color)");
			formTitleLabel.getStyle().set("border-bottom", "none");
			formTitleLabel.getStyle().set("border-radius", "var(--lumo-border-radius)");
			formTitleLabel.getStyle().set("border-bottom-right-radius", "0px");
			formTitleLabel.getStyle().set("border-bottom-left-radius", "0px");
			formTitleLabel.getStyle().set("padding-left", "0.5rem");
			formTitleLabel.getStyle().set("padding-right", "0.5rem");
			formTitleLabel.getStyle().set("padding-top", "0.25rem");
			formTitleLayout = new HorizontalLayout();
			formTitleLayout.addClassName("draggable");
			formTitleLayout.getStyle().set("background-color", "var(--lumo-primary-color-10pct)");
			formTitleLayout.getStyle().set("border-bottom", "5px solid var(--lumo-primary-color-10pct)");
			formTitleLayout.getStyle().set("padding-left", "0.5rem");
			formTitleLayout.getStyle().set("padding-top", "0.5rem");
			formTitleLayout.setWidthFull();
			formTitleLayout.add(formTitleLabel);
			addComponentAsFirst(formTitleLayout);
		}
	}

	protected void setFormTitleVisibility(boolean isVisible) {
		formTitleLayout.getChildren().forEach(c -> c.setVisible(isVisible));
		formTitleLayout.getStyle().set("padding-top", isVisible ? "0.5rem" : "0.0rem");
	}

	protected String defaultTabTitle() {
		return "Details";
	}

	protected void customizeSaveButton(Button saveButton) {
	}

	protected void customizeDismissButton(Button dismissButton) {
	}

	protected void customizeResetButton(Button resetButton) {
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
		formLayout.setResponsiveSteps(List.of(new ResponsiveStep("320px", 1), new ResponsiveStep("600px", 2))); //, new ResponsiveStep("640px", 3)));
		formLayout.setSizeFull();
		return formLayout;
	}

	protected void expand(Component c) {
		setColspan(c, Integer.MAX_VALUE);
	}

	protected void shrink(Component c) {
		setColspan(c, 1);
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
		listeners.forEach(l -> {
			l.onEvent(GxEntityFormEvent.PRE_BIND, entity);
		});
		dataBinder.readBean(entity);
		entityBound = true;
		if (tabs != null) {
			tabs.setSelectedTab((Tab) tabs.getTabAt(0));
		}
		postBinding(entity);
		listeners.forEach(l -> {
			l.onEvent(GxEntityFormEvent.POST_BIND, entity);
		});
		String formTitle = formTitle();
		if (formTitle == null) {
			String keyPath = formTitleProperty();
			if (keyPath != null && !keyPath.isBlank()) {
				formTitle = new KeyValueWrapper(entity).stringForKeyPath(keyPath);
			}
		}
		if (formTitle == null || formTitle.isBlank()) {
			formTitle = "Entry Form";
		}
		if (formTitle.length() > 50) {
			formTitleLabel.setText(formTitle.substring(0, 47) + "...");
			formTitleLabel.setTitle(formTitle);
		} else {
			formTitleLabel.setText(formTitle);
			formTitleLabel.setTitle("");
		}
		//		setFormTitleVisibility(!Strings.isNullOrEmpty(formTitle));
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
		return "800px";
	}

	public GxDialog showInDialog(T entity, String width, String height) {
		setEntity(entity);
		if (dialog == null) {
			dialog = new GxDialog(GxAbstractEntityForm.this);
			dialog.addThemeVariants(DialogVariant.NO_PADDING);
			dialog.setResizable(true);
			dialog.setModal(isModal());
			dialog.setCloseOnEsc(false);
			dialog.setDraggable(true);
			dialog.setResizable(true);
			dialog.setCloseOnOutsideClick(!isModal());
		}
		if (!dialog.isOpened()) {
			dialog.setWidth(width);
			dialog.setHeight(height);
			dialog.open();
		}
		return dialog;
	}

	/**
	 * If returned false, the dialog will close when clicked outside the form.
	 * Default value is true.
	 * 
	 * @return
	 */
	protected boolean isModal() {
		return true;
	}

	public void closeDialog() {
		if (dialog != null) {
			dialog.close();
		}
	}

	public void setDelegate(EntityFormDelegate<T> delegate) {
		this.delegate = delegate;
	}

	private Map<Integer, Tab> tabMap = new HashMap<>();

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
			tabMap.put(tabItem.getIndex(), tab);
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

	protected void selectTab(Integer tabIndex) {
		Tab tab = tabMap.get(tabIndex);
		if (tab != null)
			tabs.setSelectedTab(tab);
	}

	protected void setTabEnabled(Integer index, Boolean value) {
		Component c = tabs.getTabAt(index);
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

	HashSet<GxEntityFormEventListener<T>> listeners = new HashSet<>();

	public void registerListener(GxEntityFormEventListener<T> listener) {
		listeners.add(listener);
	}

	public void unregisterListener(GxEntityFormEventListener<T> listener) {
		listeners.remove(listener);
	}

	public static interface GxEntityFormEventListener<T> {
		public enum GxEntityFormEvent {
			SAVE,
			DISMISS,
			RESET,
			PRE_BIND,
			POST_BIND
		}

		void onEvent(GxEntityFormEvent event, T entity);
	}

}
