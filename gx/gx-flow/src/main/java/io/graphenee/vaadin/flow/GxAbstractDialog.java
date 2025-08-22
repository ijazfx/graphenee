package io.graphenee.vaadin.flow;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

import io.graphenee.vaadin.flow.GxAbstractDialog.GxDialogEventListener.GxDialogEvent;
import io.graphenee.vaadin.flow.component.GxDialog;
import io.graphenee.vaadin.flow.event.TRDelayClickListener;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * An abstract entity form.
 *
 * @param <T> The entity type.
 */
@Slf4j
public abstract class GxAbstractDialog extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private HorizontalLayout dialogTitleLayout;
	private NativeLabel dialogTitleLabel;
	private Component layoutComponent;
	private Component toolbar;
	private Button dismissButton;

	private Tabs tabs;

	private boolean isBuilt = false;

	private GxDialogDelegate delegate;

	private HasComponents parent;

	private GxDialog dialog;

	@Setter
	private boolean dialogAutoClose = true;

	/**
	 * Creates a new instance of this dialog.
	 */
	public GxAbstractDialog() {
		setSizeFull();
		addClassName("gx-entity-form");
		setMargin(false);
		setPadding(false);
		setSpacing(false);
	}

	private synchronized GxAbstractDialog build() {
		if (!isBuilt) {
			buildFormTitle();
			layoutComponent = getLayoutComponent();
			if (layoutComponent instanceof HasComponents) {
				decorateLayout((HasComponents) layoutComponent);
			}

			toolbar = getToolbarComponent();

			if (toolbar instanceof HasComponents) {
				HasComponents c = (HasComponents) toolbar;
				decorateToolbar(c);

				dismissButton = new Button("Dismiss");
				dismissButton.addClickShortcut(Key.ESCAPE);
				dismissButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

				dismissButton.addClickListener(new TRDelayClickListener<Button>() {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(ClickEvent<Button> event) {
						dismiss();
						if (delegate != null)
							delegate.onDismiss();
						listeners.forEach(l -> {
							l.onEvent(GxDialogEvent.DISMISS);
						});
					}
				});

				dismissButton.getElement().getStyle().set("margin-right", "auto");

				customizeDismissButton(dismissButton);
				c.add(dismissButton);

			}

			Scroller scroller = new Scroller();
			scroller.setSizeFull();
			VerticalLayout entityFormLayout = new VerticalLayout(layoutComponent);
			entityFormLayout.setSizeFull();
			entityFormLayout.setMargin(false);
			entityFormLayout.setSpacing(false);
			entityFormLayout.setPadding(false);
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
	public void show() {
		this.parent = null;
		showInDialog();
	}

	/**
	 * Shows the form.
	 * 
	 * @param entity The entity to show.
	 * @param host   The host component.
	 */
	public void show(HasComponents host) {
		initializeDialog();
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

	private void buildFormTitle() {
		if (dialogTitleLabel == null) {
			dialogTitleLabel = new NativeLabel();
			dialogTitleLabel.addClassName("gx-form-title");
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
			dialogTitleLayout = new HorizontalLayout();
			dialogTitleLayout.addClassName("gx-form-title-layout");
			dialogTitleLayout.addClassName("draggable");
			// formTitleLayout.getStyle().set("background-color",
			// "var(--lumo-primary-color-10pct)");
			// formTitleLayout.getStyle().set("border-bottom", "5px solid
			// var(--lumo-primary-color-10pct)");
			// formTitleLayout.getStyle().set("padding-left", "0.5rem");
			// formTitleLayout.getStyle().set("padding-top", "0.5rem");
			dialogTitleLayout.setWidthFull();
			dialogTitleLayout.add(dialogTitleLabel);
			addComponentAsFirst(dialogTitleLayout);

		}
	}

	protected void setFormTitleVisibility(boolean isVisible) {
		dialogTitleLayout.getChildren().forEach(c -> c.setVisible(isVisible));
		dialogTitleLayout.getStyle().set("padding-top", isVisible ? "0.5rem" : "0.0rem");
	}

	protected String defaultTabTitle() {
		return "Details";
	}

	protected void customizeDismissButton(Button dismissButton) {
	}

	private String dialogTitle;

	public void setDialogTitle(String dialogTitle) {
		this.dialogTitle = dialogTitle;
	}

	public String getDialogTitle() {
		return Strings.isNullOrEmpty(dialogTitle) ? "Dialog" : dialogTitle;
	}

	protected void postBuild() {
	}

	protected abstract void decorateLayout(HasComponents layout);

	protected void decorateToolbar(HasComponents toolbar) {

	}

	protected Component getToolbarComponent() {
		HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.setWidthFull();
		toolbar.addClassName("gx-form-footer");
		// toolbar.getStyle().set("border-radius", "var(--lumo-border-radius)");
		// toolbar.getStyle().set("border-top-right-radius", "0px");
		// toolbar.getStyle().set("border-top-left-radius", "0px");
		// toolbar.getStyle().set("background-color", "#F8F8F8");
		// toolbar.setWidthFull();
		// toolbar.setPadding(true);
		// toolbar.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
		return toolbar;
	}

	protected Component getLayoutComponent() {
		FlexLayout layoutComponent = new FlexLayout();
		layoutComponent.setSizeFull();
		return layoutComponent;
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
		Component fc = getLayoutComponent();
		if (fc instanceof FormLayout) {
			FormLayout form = (FormLayout) fc;
			form.setColspan(c, colspan);
		}
	}

	/**
	 * Initializes this dialog, sets title and focus on the tab if any.
	 */
	public void initializeDialog() {
		build();
		if (tabs != null) {
			tabs.setSelectedTab(tabs.getTabAt(0));
		}
		String formTitle = getDialogTitle();
		if (formTitle != null && formTitle.length() > 50) {
			dialogTitleLabel.setText(formTitle.substring(0, 47) + "...");
			dialogTitleLabel.setTitle(formTitle);
		} else {
			dialogTitleLabel.setText(formTitle);
			dialogTitleLabel.setTitle("");
		}
		// setFormTitleVisibility(!Strings.isNullOrEmpty(formTitle));
		if (shouldFocusFirstFieldOnShow()) {
			focusFirst(this);
		}
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
	 * Shows this component in a dialog.
	 * 
	 * @return The dialog.
	 */
	public GxDialog showInDialog() {
		return showInDialog(dialogWidth(), dialogHeight());
	}

	protected String dialogHeight() {
		return "100%";
	}

	protected String dialogWidth() {
		return "100%";
	}

	/**
	 * This method has been deprecated in favor of {@link #show()} and will be
	 * removed in future.
	 * 
	 * @param width
	 * @param height
	 * @return
	 */
	private GxDialog showInDialog(String width, String height) {
		initializeDialog();
		if (dialog == null) {
			dialog = new GxDialog(GxAbstractDialog.this);
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
	public void setDelegate(GxDialogDelegate delegate) {
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
	 * A delegate for the dialog
	 */
	public interface GxDialogDelegate {
		/**
		 * Called when the form is dismissed.
		 * 
		 * @param entity The entity.
		 */
		default void onDismiss() {
		}
	}

	HashSet<GxDialogEventListener> listeners = new HashSet<>();

	/**
	 * Registers a listener.
	 * 
	 * @param listener The listener to register.
	 */
	public void registerListener(GxDialogEventListener listener) {
		listeners.add(listener);
	}

	/**
	 * Unregisters a listener.
	 * 
	 * @param listener The listener to unregister.
	 */
	public void unregisterListener(GxDialogEventListener listener) {
		listeners.remove(listener);
	}

	/**
	 * An event listener for the entity form.
	 *
	 * @param <T> The entity type.
	 */
	public static interface GxDialogEventListener {
		/**
		 * An enum that represents the events of the entity form.
		 */
		public enum GxDialogEvent {
			/**
			 * The dismiss event.
			 */
			DISMISS,
		}

		/**
		 * Called when an event occurs.
		 * 
		 * @param event  The event.
		 * @param entity The entity.
		 */
		void onEvent(GxDialogEvent event);
	}

}
