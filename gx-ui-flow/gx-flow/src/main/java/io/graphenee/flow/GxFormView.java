package io.graphenee.flow;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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

@CssImport("styles/form-view.css")
public abstract class GxFormView<T> extends Div implements AfterNavigationObserver {

	private static final long serialVersionUID = 1L;

	private FormConfigurator<T> fc;

	private Button cancel = new Button("Cancel");
	private Button save = new Button("Save");

	private Binder<T> binder;

	private T bean;

	private VerticalLayout createWrapper() {
		VerticalLayout wrapper = new VerticalLayout();
		wrapper.setClassName("wrapper");
		wrapper.setSpacing(false);
		return wrapper;
	}

	public GxFormView(Class<T> entityClass) {
		fc = new FormConfigurator<T>(entityClass);
		setClassName("form-view");
		configure(fc);

		// Create/Configure Form
		Component formComponent = createFormComponent();
		add(formComponent);
	}

	private Component createFormComponent() {
		VerticalLayout wrapper = createWrapper();
		H5 heading = new H5(fc.getCaption());
		wrapper.add(heading);

		GxFormLayout form = GxFormLayout.builder().expandFields(true).build();
		wrapper.add(form);
		binder = new Binder<>(fc.getEntityClass());
		for (String prop : fc.getEditableProperties()) {
			Component component = componentForProperty(prop, binder);
			String caption = fc.getPropertyCaption(prop);
			if (component != null) {
				form.addFormItem(component, caption);
			}
		}
		Component buttonLayout = createButtonLayout();
		wrapper.add(buttonLayout);

		return wrapper;
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
				Field f = fc.getEntityClass().getDeclaredField(propertyName);
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

	private Component createButtonLayout() {
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setClassName("button-layout");
		buttonLayout.setWidthFull();
		buttonLayout.setSpacing(true);
		cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		cancel.setText(fc.getCancelCaption());
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.setText(fc.getSaveCaption());
		buttonLayout.add(cancel, save);

		cancel.addClickListener(e -> {
			try {
				binder.setBean(bean);
				if (fc.onCancel != null) {
					fc.onCancel.execute(bean);
				}
			} catch (Exception ve) {
				Notification.show(ve.getMessage());
			}
		});

		save.addClickListener(e -> {
			try {
				binder.writeBean(bean);
				if (fc.onSave != null) {
					fc.onSave.execute(bean);
				}
			} catch (Exception ve) {
				Notification.show(ve.getMessage());
			}
		});

		return buttonLayout;
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		T bean = entityToEdit();
		setEntity(bean);
	}

	protected abstract T entityToEdit();

	private void setEntity(T bean) {
		this.bean = bean;
		bindEntity(bean);
	}

	public T getEntity() {
		return bean;
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

	protected void configure(FormConfigurator<T> fc) {
	}

	public static class FormConfigurator<T> {

		public static enum FormPosition {
			START,
			END,
			POPUP
		}

		private Class<T> entityClass;
		private Supplier<String> captionProvider;
		private String[] editableProperties;
		private Component defaultComponent;
		private String saveCaption;
		private String cancelCaption;
		private TRParamCallback<T> onSave;
		private TRParamCallback<T> onCancel;
		private FormPosition position = FormPosition.END;
		Map<String, PropertyConfigurator> propertyConfiguratorMap = new HashMap<>();

		public FormConfigurator(Class<T> entityClass) {
			this.entityClass = entityClass;
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

		public FormConfigurator<T> caption(String caption) {
			return captionProvider(() -> caption);
		}

		public String getCaption() {
			return captionProvider != null ? captionProvider.get() : null;
		}

		public FormConfigurator<T> captionProvider(Supplier<String> captionProvider) {
			this.captionProvider = captionProvider;
			return this;
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
			for (String editable : editableProperties) {
				propertyConfigurator(editable).required(false);
			}
			for (String required : propertyName) {
				propertyConfigurator(required).required(true);
			}
			return this;
		}

		public FormConfigurator<T> optional(String... propertyName) {
			for (String editable : editableProperties) {
				propertyConfigurator(editable).required(true);
			}
			for (String required : propertyName) {
				propertyConfigurator(required).required(false);
			}
			return this;
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

		public Class<T> getEntityClass() {
			return this.entityClass;
		}

	}

	public static class PropertyConfigurator {
		private String propertyName;
		private Supplier<String> captionProvider;
		private boolean required = true;
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
			return captionProvider(() -> caption);
		}

		public String getCaption() {
			return captionProvider != null ? captionProvider.get() : StringUtils.capitalize(StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(propertyName), " "));
		}

		public PropertyConfigurator captionProvider(Supplier<String> captionProvider) {
			this.captionProvider = captionProvider;
			return this;
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
