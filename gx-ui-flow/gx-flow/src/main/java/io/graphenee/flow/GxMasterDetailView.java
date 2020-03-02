package io.graphenee.flow;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Binder.BindingBuilder;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.converter.StringToDoubleConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;

import io.graphenee.core.callback.TRParamCallback;
import io.graphenee.flow.GxMasterDetailView.FormConfigurator.FormPosition;

@CssImport("styles/master-detail-view.css")
public abstract class GxMasterDetailView<T> extends Div implements AfterNavigationObserver {

	private static final long serialVersionUID = 1L;

	private Grid<T> mainGrid;
	private GridConfigurator<T> gc;
	private FormConfigurator<T> fc;

	private Button cancel = new Button("Cancel");
	private Button save = new Button("Save");

	private Binder<T> binder;

	private T bean;

	public GxMasterDetailView(Class<T> entityClass) {
		gc = new GridConfigurator<T>(entityClass);
		fc = gc.formConfigurator();
		setClassName("master-detail-view");
		configure(gc);
		configure(fc);

		// Configure Grid
		mainGrid = new Grid<>(entityClass);
		mainGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
		mainGrid.setHeightFull();

		// Show visible properties only
		String[] props = gc.getVisibleProperties();
		if (props != null && props.length > 0) {
			mainGrid.getColumns().forEach(column -> {
				column.setVisible(false);
			});
		}

		for (int i = 0; i < props.length; i++) {
			String prop = props[i];
			String header = gc.getPropertyCaption(prop);
			if (header == null)
				header = prop;
			Column<T> column = mainGrid.getColumnByKey(prop);
			column.setHeader(header);
			if (column != null)
				column.setVisible(true);
		}

		// Create/Configure Form
		Component gridComponent = createGridComponent();
		Component formComponent = createFormComponent();

		if (fc.getPosition() == FormPosition.POPUP) {
			VerticalLayout layout = new VerticalLayout();
			layout.setSizeFull();
			layout.add(gridComponent);
			add(layout);
			mainGrid.asSingleSelect().addValueChangeListener(event -> {
				setEntity(event.getValue());
			});
		} else {
			SplitLayout splitLayout = new SplitLayout();
			splitLayout.setSizeFull();
			if (fc.getPosition() == FormPosition.START) {
				splitLayout.addToPrimary(formComponent);
				splitLayout.addToSecondary(gridComponent);
			} else {
				splitLayout.addToPrimary(gridComponent);
				splitLayout.addToSecondary(formComponent);
			}
			add(splitLayout);
			mainGrid.asSingleSelect().addValueChangeListener(event -> setEntity(event.getValue()));
		}

	}

	private Component createFormComponent() {
		Div editorDiv = new Div();
		editorDiv.setClassName("editor-layout");
		GxFormLayout form = GxFormLayout.builder().expandFields(true).build();
		editorDiv.add(form);
		binder = new Binder<>(gc.getEntityClass());
		for (String prop : fc.getEditableProperties()) {
			Component component = componentForProperty(prop, binder);
			String caption = fc.getPropertyCaption(prop);
			if (component != null) {
				form.addFormItem(component, caption);
			}
		}
		createButtonLayout(editorDiv);
		return editorDiv;
	}

	protected Component componentForProperty(String propertyName, Binder<T> binder) {
		try {
			PropertyConfigurator pc = fc.propertyConfigurator(propertyName);
			Component component = pc.getComponent();
			Converter converter = pc.getCoverter();
			boolean required = pc.isRequired();
			Collection<Validator> validators = pc.getValidators();
			BindingBuilder builder = null;
			if (component != null && component instanceof AbstractField) {
				builder = binder.forField((AbstractField) component);
			} else {
				Field f = gc.getEntityClass().getDeclaredField(propertyName);
				if (f.getType().equals(Boolean.class)) {
					Checkbox c = new Checkbox();
					builder = binder.forField(c);
					component = c;
				} else if (f.getType().equals(Double.class)) {
					TextField c = new TextField();
					builder = binder.forField(c).withConverter(new StringToDoubleConverter(""));
					component = c;
				} else if (f.getType().equals(Number.class)) {
					TextField c = new TextField();
					builder = binder.forField(c).withConverter(new StringToIntegerConverter(""));
					component = c;
				} else {
					TextField c = new TextField();
					builder = binder.forField(c);
					component = c;
				}
			}
			if (builder != null) {
				if (converter != null) {
					builder.withConverter(converter);
				}
				if (validators != null) {
					for (Validator v : validators) {
						builder.withValidator(v);
					}
				}
				if (pc.isRequired()) {
					builder.asRequired();
				}
				builder.bind(propertyName);
			}
			if (component instanceof TextField) {
				((TextField) component).setRequired(required);
				((TextField) component).setRequiredIndicatorVisible(required);
			}
			return component;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private void createButtonLayout(Div editorDiv) {
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setClassName("button-layout");
		buttonLayout.setWidthFull();
		buttonLayout.setSpacing(true);
		cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		cancel.setText(fc.getCancelCaption());
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.setText(fc.getSaveCaption());
		buttonLayout.add(cancel, save);
		editorDiv.add(buttonLayout);

		cancel.addClickListener(e -> {
			mainGrid.asSingleSelect().clear();
			try {
				if (fc.onCancel != null) {
					fc.onCancel.execute(bean);
				}
			} catch (Exception ex) {
			}
		});

		save.addClickListener(e -> {
			binder.writeBeanIfValid(bean);
			try {
				if (fc.onSave != null) {
					fc.onSave.execute(bean);
				}
				mainGrid.getDataProvider().refreshItem(bean);
			} catch (Exception ex) {
			}
		});
	}

	private Component createGridComponent() {
		Div wrapper = new Div();
		wrapper.setClassName("wrapper");
		wrapper.setWidthFull();
		wrapper.add(mainGrid);
		return wrapper;
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		mainGrid.setItems(fetchEntities());
	}

	protected Collection<T> fetchEntities() {
		return Collections.emptyList();
	}

	protected void onDelete(T bean) {
	}

	private void setEntity(T bean) {
		this.bean = bean;
		bindEntity(bean);
	}

	protected void bindEntity(T bean) {
		preBinding(bean);
		binder.readBean(bean);
		postBinding(bean);
	}

	protected void preBinding(T bean) {
	}

	protected void postBinding(T bean) {
	}

	protected void configure(GridConfigurator<T> gc) {
	}

	protected void configure(FormConfigurator<T> fc) {
	}

	public static class GridConfigurator<T> {
		private String caption;
		private String[] visibleProperties;
		private String[] editableProperties;
		private Component defaultComponent;
		private FormConfigurator<T> formConfigurator;
		private Map<String, PropertyConfigurator> propertyConfiguratorMap = new HashMap<>();
		private Class<T> entityClass;

		public GridConfigurator(Class<T> entityClass) {
			this.entityClass = entityClass;
		}

		public Class<T> getEntityClass() {
			return entityClass;
		}

		public GridConfigurator<T> caption(String caption) {
			this.caption = caption;
			return this;
		}

		public String getCaption() {
			return caption;
		}

		public GridConfigurator<T> visible(String... propertyName) {
			this.visibleProperties = propertyName;
			return this;
		}

		public String[] getVisibleProperties() {
			return visibleProperties != null ? visibleProperties : new String[] {};
		}

		public GridConfigurator<T> editable(String... propertyName) {
			this.editableProperties = propertyName;
			return this;
		}

		public String[] getEditableProperties() {
			return editableProperties != null ? editableProperties : new String[] {};
		}

		public GridConfigurator<T> defaultComponent(Component component) {
			this.defaultComponent = component;
			return this;
		}

		public Component getDefaultComponent() {
			return defaultComponent;
		}

		public GridConfigurator<T> propertyComponent(String propertyName, Component component) {
			propertyConfigurator(propertyName).component(component);
			return this;
		}

		public Component getPropertyComponent(String propertyName) {
			Component component = propertyConfigurator(propertyName).getComponent();
			if (component != null)
				return component;
			return getDefaultComponent();
		}

		public GridConfigurator<T> propertyCaption(String propertyName, String caption) {
			propertyConfigurator(propertyName).caption(caption);
			return this;
		}

		public String getPropertyCaption(String propertyName) {
			return propertyConfigurator(propertyName).getCaption();
		}

		public GridConfigurator<T> formCaption(String caption) {
			formConfigurator().caption(caption);
			return this;
		}

		public FormConfigurator<T> formConfigurator() {
			if (formConfigurator == null) {
				synchronized (this) {
					if (formConfigurator == null) {
						formConfigurator = new FormConfigurator<>();
					}
				}
			}
			return formConfigurator;
		}

		public PropertyConfigurator propertyConfigurator(String propertyName) {
			if (!propertyConfiguratorMap.containsKey(propertyName)) {
				synchronized (this) {
					if (!propertyConfiguratorMap.containsKey(propertyName)) {
						PropertyConfigurator c = new PropertyConfigurator(propertyName);
						propertyConfiguratorMap.put(propertyName, c);
					}
				}
			}
			return propertyConfiguratorMap.get(propertyName);
		}

	}

	public static class FormConfigurator<T> {

		public static enum FormPosition {
			START,
			END,
			POPUP
		}

		private String caption;
		private String[] editableProperties;
		private String[] requiredProperties;
		private Component defaultComponent;
		private String saveCaption;
		private String cancelCaption;
		private TRParamCallback<T> onSave;
		private TRParamCallback<T> onCancel;
		private FormPosition position = FormPosition.END;
		Map<String, PropertyConfigurator> propertyConfiguratorMap = new HashMap<>();

		public PropertyConfigurator propertyConfigurator(String propertyName) {
			if (!propertyConfiguratorMap.containsKey(propertyName)) {
				synchronized (this) {
					if (!propertyConfiguratorMap.containsKey(propertyName)) {
						PropertyConfigurator c = new PropertyConfigurator(propertyName);
						propertyConfiguratorMap.put(propertyName, c);
					}
				}
			}
			return propertyConfiguratorMap.get(propertyName);
		}

		public FormConfigurator<T> caption(String caption) {
			this.caption = caption;
			return this;
		}

		public String getCaption() {
			return this.caption;
		}

		public FormConfigurator<T> defaultComponent(Component component) {
			this.defaultComponent = component;
			return this;
		}

		public Component getDefaultComponent() {
			return defaultComponent;
		}

		public FormConfigurator<T> editable(String... propertyName) {
			this.editableProperties = propertyName;
			return this;
		}

		public String[] getEditableProperties() {
			return editableProperties != null ? editableProperties : new String[] {};
		}

		public FormConfigurator<T> required(String... propertyName) {
			this.requiredProperties = propertyName;
			for (String required : requiredProperties) {
				propertyConfigurator(required).required(true);
			}
			return this;
		}

		public String[] getRequiredProperties() {
			return requiredProperties != null ? requiredProperties : new String[] {};
		}

		public FormConfigurator<T> propertyComponent(String propertyName, Component component) {
			propertyConfigurator(propertyName).component(component);
			return this;
		}

		public Component getPropertyComponent(String propertyName) {
			Component component = propertyConfigurator(propertyName).getComponent();
			if (component != null)
				return component;
			return getDefaultComponent();
		}

		public FormConfigurator<T> propertyCaption(String propertyName, String caption) {
			propertyConfigurator(propertyName).caption(caption);
			return this;
		}

		public String getPropertyCaption(String propertyName) {
			return propertyConfigurator(propertyName).getCaption();
		}

		public FormConfigurator<T> onSave(TRParamCallback<T> onSave) {
			this.onSave = onSave;
			return this;
		}

		public FormConfigurator<T> onCancel(TRParamCallback<T> onCancel) {
			this.onCancel = onCancel;
			return this;
		}

		public FormConfigurator<T> saveCaption(String caption) {
			this.saveCaption = caption;
			return this;
		}

		public FormConfigurator<T> cancelCaption(String caption) {
			this.cancelCaption = caption;
			return this;
		}

		public String getSaveCaption() {
			return saveCaption != null ? saveCaption : "Save";
		}

		public String getCancelCaption() {
			return cancelCaption != null ? cancelCaption : (position == FormPosition.POPUP ? "Dismiss" : "Cancel");
		}

		public FormConfigurator<T> position(FormPosition position) {
			this.position = position;
			return this;
		}

		public FormPosition getPosition() {
			return this.position;
		}

	}

	public static class PropertyConfigurator {
		private String propertyName;
		private String caption;
		private boolean required = false;
		private boolean visible = true;
		private Component component;
		private Converter converter;
		private Set<Validator> validators = new HashSet<>();

		public PropertyConfigurator(String propertyName) {
			this.propertyName = propertyName;
		}

		public String getPropertyName() {
			return this.propertyName;
		}

		public PropertyConfigurator caption(String caption) {
			this.caption = caption;
			return this;
		}

		public String getCaption() {
			return this.caption;
		}

		public PropertyConfigurator required(Boolean required) {
			this.required = required;
			return this;
		}

		public boolean isRequired() {
			return this.required;
		}

		public PropertyConfigurator visible(Boolean visible) {
			this.visible = visible;
			return this;
		}

		public boolean isVisible() {
			return this.visible;
		}

		public PropertyConfigurator converter(Converter converter) {
			this.converter = converter;
			return this;
		}

		public Converter getCoverter() {
			return this.converter;
		}

		public PropertyConfigurator addValidator(Validator validator) {
			validators.add(validator);
			return this;
		}

		public PropertyConfigurator removeValidator(Validator validator) {
			validators.remove(validator);
			return this;
		}

		public Collection<Validator> getValidators() {
			return this.validators;
		}

		public PropertyConfigurator component(Component component) {
			this.component = component;
			return this;
		}

		public Component getComponent() {
			return this.component;
		}

	}

}
