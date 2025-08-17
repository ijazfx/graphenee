package io.graphenee.vaadin.flow;

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
import com.vaadin.flow.component.dialog.DialogVariant;
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
import io.graphenee.vaadin.flow.GxAbstractEntityForm.GxEntityFormEventListener.GxEntityFormEvent;
import io.graphenee.vaadin.flow.component.DialogFactory;
import io.graphenee.vaadin.flow.component.GxDialog;
import io.graphenee.vaadin.flow.event.TRDelayClickListener;
import io.graphenee.vaadin.flow.utils.StringUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * An abstract entity form.
 *
 * @param <T> The entity type.
 */
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

	private HasComponents parent;

	private GxDialog dialog;

	@Setter
	private boolean dialogAutoClose = true;

	/**
	 * Creates a new instance of this form.
	 * 
	 * @param entityClass The entity class.
	 */
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
				saveButton = new Button("Save");
				saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
				saveButton.setDisableOnClick(true);

				saveButton.addClickListener(cl -> {
					if (entity != null) {
						try {
							validateForm();
							dataBinder.writeBean(entity);
							if (delegate != null)
								delegate.onSave(entity);
							listeners.forEach(l -> {
								l.onEvent(GxEntityFormEvent.SAVE, entity);
							});
							if (dialogAutoClose)
								dismiss();
						} catch (Exception e) {
							log.error(e.getMessage(), e);
						}
					}
					saveButton.setEnabled(true);
				});

				customizeSaveButton(saveButton);

				resetButton = new Button("Reset");
				resetButton.addClickShortcut(Key.KEY_R, KeyModifier.ALT);

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

				dismissButton = new Button("Dismiss");
				dismissButton.addClickShortcut(Key.ESCAPE);
				dismissButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

				dismissButton.addClickListener(new TRDelayClickListener<Button>() {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(ClickEvent<Button> event) {
						if (dataBinder.hasChanges()) {
							DialogFactory.confirmDialog(
									"You have unsaved changes which will be lost. Do you want to continue?", dlg -> {
										dismiss();
										if (delegate != null)
											delegate.onDismiss(entity);
										listeners.forEach(l -> {
											l.onEvent(GxEntityFormEvent.DISMISS, entity);
										});
									}).open();
						} else {
							dismiss();
							if (delegate != null)
								delegate.onDismiss(entity);
							listeners.forEach(l -> {
								l.onEvent(GxEntityFormEvent.DISMISS, entity);
							});
						}
					}
				});

				dismissButton.getElement().getStyle().set("margin-right", "auto");
				
				customizeDismissButton(dismissButton);
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

			PropertySet<T> bps = BeanPropertySet.get(entityClass, true,
					new PropertyFilterDefinition(1, Arrays.asList("java")));
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

	/**
	 * Shows the form.
	 * 
	 * @param entity The entity to show.
	 */
	public void show(T entity) {
		this.parent = null;
		showInDialog(entity);
	}

	/**
	 * Shows the form.
	 * 
	 * @param entity The entity to show.
	 * @param host   The host component.
	 */
	public void show(T entity, HasComponents host) {
		setEntity(entity);
		this.parent = host;
		host.add(this);
	}

	/**
	 * @deprecated use {@link #dismiss()} instead.
	 */
	@Deprecated
	public void closeDialog() {
		dismiss();
	}

	/**
	 * Dismisses the form.
	 */
	public void dismiss() {
		if (dialog != null) {
			dialog.close();
		} else {
			if (parent != null) {
				parent.remove(this);
			}
		}
	}

	/**
	 * Validates the form.
	 * 
	 * @throws ValidationException If the form is invalid.
	 */
	public void validateForm() throws ValidationException {
		dataBinder.validate();
	}

	private void buildFormTitle() {
		if (formTitleLabel == null) {
			formTitleLabel = new NativeLabel();
			formTitleLabel.addClassName("gx-form-title");
			// formTitleLabel.getStyle().set("text-transform", "uppercase");
			// formTitleLabel.getStyle().set("background-color",
			// "var(--lumo-primary-color-10pct)");
			// formTitleLabel.getStyle().set("font-weight", "bold");
			// formTitleLabel.getStyle().set("color", "var(--lumo-primary-color)");
			// formTitleLabel.getStyle().set("border-bottom", "none");
			// formTitleLabel.getStyle().set("border-radius", "var(--lumo-border-radius)");
			// formTitleLabel.getStyle().set("border-bottom-right-radius", "0px");
			// formTitleLabel.getStyle().set("border-bottom-left-radius", "0px");
			// formTitleLabel.getStyle().set("padding-left", "0.5rem");
			// formTitleLabel.getStyle().set("padding-right", "0.5rem");
			// formTitleLabel.getStyle().set("padding-top", "0.25rem");
			formTitleLayout = new HorizontalLayout();
			formTitleLayout.addClassName("gx-form-title-layout");
			formTitleLayout.addClassName("draggable");
			// formTitleLayout.getStyle().set("background-color",
			// "var(--lumo-primary-color-10pct)");
			// formTitleLayout.getStyle().set("border-bottom", "5px solid
			// var(--lumo-primary-color-10pct)");
			// formTitleLayout.getStyle().set("padding-left", "0.5rem");
			// formTitleLayout.getStyle().set("padding-top", "0.5rem");
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
		return StringUtils.toTitleCase(entityClass.getSimpleName()).replaceFirst("Gx", "");
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
		formLayout.setResponsiveSteps(List.of(new ResponsiveStep("320px", 1), new ResponsiveStep("600px", 2))); // , new
																												// ResponsiveStep("640px",
																												// 3)));
		formLayout.setSizeFull();
		return formLayout;
	}

	@Override
	public void expand(Component... c) {
		List.of(c).forEach(comp -> {
			setColspan(comp, Integer.MAX_VALUE);
		});
	}

	protected void shrink(Component... c) {
		List.of(c).forEach(comp -> {
			setColspan(comp, 1);
		});
	}

	protected void setColspan(Component c, int colspan) {
		Component fc = getFormComponent();
		if (fc instanceof FormLayout) {
			FormLayout form = (FormLayout) fc;
			form.setColspan(c, colspan);
		}
	}

	/**
	 * Sets whether the form is editable.
	 * 
	 * @param editable Whether the form is editable.
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
		if (saveButton != null) {
			saveButton.setEnabled(editable);
		}
	}

	/**
	 * Checks if the form is editable.
	 * 
	 * @return True if the form is editable, false otherwise.
	 */
	public boolean isEditable() {
		return editable;
	}

	/**
	 * Sets the entity.
	 * 
	 * @param entity The entity.
	 */
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
			tabs.setSelectedTab(tabs.getTabAt(0));
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
		if (formTitle != null && formTitle.length() > 50) {
			formTitleLabel.setText(formTitle.substring(0, 47) + "...");
			formTitleLabel.setTitle(formTitle);
		} else {
			formTitleLabel.setText(formTitle);
			formTitleLabel.setTitle("");
		}
		// setFormTitleVisibility(!Strings.isNullOrEmpty(formTitle));
		if (shouldFocusFirstFieldOnShow()) {
			focusFirst(this);
		}
		saveButton.setVisible(isEditable());
		resetButton.setVisible(isEditable());
	}

	protected boolean shouldFocusFirstFieldOnShow() {
		return true;
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

	/**
	 * Gets the entity.
	 * 
	 * @return The entity.
	 */
	public T getEntity() {
		return entity;
	}

	/**
	 * Shows the form in a dialog.
	 * 
	 * @param entity The entity to show.
	 * @return The dialog.
	 */
	public GxDialog showInDialog(T entity) {
		return showInDialog(entity, dialogWidth(), dialogHeight());
	}

	protected String dialogHeight() {
		return "50rem";
	}

	protected String dialogWidth() {
		return "50rem";
	}

	/**
	 * This method has been deprecated in favor of {@link #show()} and will be
	 * removed in future.
	 * 
	 * @param entity
	 * @param width
	 * @param height
	 * @return
	 */
	private GxDialog showInDialog(T entity, String width, String height) {
		setEntity(entity);
		if (dialog == null) {
			dialog = new GxDialog(GxAbstractEntityForm.this);
			dialog.addThemeVariants(DialogVariant.LUMO_NO_PADDING);
			dialog.setResizable(true);
			dialog.setModal(true);
			dialog.setCloseOnEsc(false);
			dialog.setDraggable(true);
			dialog.setResizable(true);
			dialog.setCloseOnOutsideClick(false);
		}
		if (!dialog.isOpened()) {
			dialog.setWidth(width);
			dialog.setHeight(height);
			dialog.open();
		}
		return dialog;
	}

	/**
	 * Sets the delegate.
	 * 
	 * @param delegate The delegate.
	 */
	public void setDelegate(EntityFormDelegate<T> delegate) {
		this.delegate = delegate;
	}

	private Map<Integer, Tab> tabMap = new HashMap<>();

	private void addTab(List<GxTabItem> tabItems) {
		Map<Integer, Integer> tabIndexMap = new HashMap<>();
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
			tabIndexMap.put(i, tabItem.getIndex());
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
			onTabChange(tabIndexMap.get(selectedIndex), tabs.getSelectedTab(), selectedComponent);
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

	/**
	 * A delegate for the entity form.
	 *
	 * @param <T> The entity type.
	 */
	public interface EntityFormDelegate<T> {
		/**
		 * Called when the entity is saved.
		 * 
		 * @param entity The entity.
		 */
		void onSave(T entity);

		/**
		 * Called when the form is dismissed.
		 * 
		 * @param entity The entity.
		 */
		default void onDismiss(T entity) {
		}

		/**
		 * Called when the form is reset.
		 * 
		 * @param entity The entity.
		 */
		default void onReset(T entity) {
		}
	}

	protected boolean isEntityBound() {
		return entityBound;
	}

	HashSet<GxEntityFormEventListener<T>> listeners = new HashSet<>();

	/**
	 * Registers a listener.
	 * 
	 * @param listener The listener to register.
	 */
	public void registerListener(GxEntityFormEventListener<T> listener) {
		listeners.add(listener);
	}

	/**
	 * Unregisters a listener.
	 * 
	 * @param listener The listener to unregister.
	 */
	public void unregisterListener(GxEntityFormEventListener<T> listener) {
		listeners.remove(listener);
	}

	/**
	 * An event listener for the entity form.
	 *
	 * @param <T> The entity type.
	 */
	public static interface GxEntityFormEventListener<T> {
		/**
		 * An enum that represents the events of the entity form.
		 */
		public enum GxEntityFormEvent {
			/**
			 * The save event.
			 */
			SAVE,
			/**
			 * The dismiss event.
			 */
			DISMISS,
			/**
			 * The reset event.
			 */
			RESET,
			/**
			 * The pre-bind event.
			 */
			PRE_BIND,
			/**
			 * The post-bind event.
			 */
			POST_BIND
		}

		/**
		 * Called when an event occurs.
		 * 
		 * @param event  The event.
		 * @param entity The entity.
		 */
		void onEvent(GxEntityFormEvent event, T entity);
	}

}
