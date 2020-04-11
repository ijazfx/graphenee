/*******************************************************************************
 * Copyright (c) 2016, 2018 Farrukh Ijaz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package io.graphenee.vaadin;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.vaadin.viritin.BeanBinder;
import org.vaadin.viritin.MBeanFieldGroup;
import org.vaadin.viritin.MBeanFieldGroup.FieldGroupListener;
import org.vaadin.viritin.button.DeleteButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.button.PrimaryButton;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.label.RichText;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractSingleComponentContainer;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.util.ReflectTools;

import io.graphenee.vaadin.util.VaadinUtils;

/**
 * This form has been fixed to handle situation where the creation of the form
 * depends on the content of entity. Original form was copied from <a href=
 * "https://github.com/viritin/viritin/wiki/AbstractForm">https://github.com/viritin/viritin/wiki/AbstractForm</a>
 * 
 * @author ijazfx
 * @param <T> - typically a java bean type
 */
public abstract class TRAbstractBaseForm<T> extends CustomComponent implements FieldGroupListener<T> {

	private boolean binding = false;

	private String modalWindowTitle = "Edit entry";
	private String saveCaption = "Save";
	private String deleteCaption = "Delete";
	private String cancelCaption = "Cancel";
	private ProgressBar busyIndicator = new ProgressBar();
	private Set<ValidationListener<T>> validationListeners = new HashSet<>();

	public static class ValidityChangedEvent<T> extends Component.Event {

		private static final Method method = ReflectTools.findMethod(ValidityChangedListener.class, "onValidityChanged", ValidityChangedEvent.class);

		public ValidityChangedEvent(Component source) {
			super(source);
		}

		@Override
		public TRAbstractBaseForm<T> getComponent() {
			return (TRAbstractBaseForm) super.getComponent();
		}

	}

	public interface ValidityChangedListener<T> extends Serializable {

		public void onValidityChanged(ValidityChangedEvent<T> event);
	}

	public interface ValidationListener<T> extends Serializable {

		public void onValidation(T entity, boolean isValid);

	}

	private Window popup;

	public TRAbstractBaseForm() {
		addAttachListener(new AttachListener() {
			@Override
			public void attach(AttachEvent event) {
				lazyInit();
				//				adjustResetButtonState();
			}
		});

		addValidationListener(new ValidationListener<T>() {

			@Override
			public void onValidation(T entity, boolean isValid) {
				TRAbstractBaseForm.this.onValidation(entity, isValid);
			}
		});

	}

	protected void onValidation(T entity, boolean isValid) {
	}

	protected void lazyInit() {
		if (getCompositionRoot() == null) {
			setCompositionRoot(new VerticalLayout());
			//			adjustSaveButtonState();
			//			adjustResetButtonState();
		}
	}

	private MBeanFieldGroup<T> fieldGroup;

	/**
	 * The validity checked and cached on last change. Should be pretty much
	 * always up to date due to eager changes. At least after onFieldGroupChange
	 * call.
	 */
	boolean isValid = false;

	private RichText beanLevelViolations;

	@Override
	public void onFieldGroupChange(MBeanFieldGroup beanFieldGroup) {
		boolean wasValid = isValid;
		isValid = fieldGroup.isValid();
		notifyValidationListeners(isValid);
		adjustSaveButtonState();
		adjustResetButtonState();
		if (wasValid != isValid) {
			fireValidityChangedEvent();
		}
		updateConstraintViolationsDisplay();
	}

	private void notifyValidationListeners(boolean isValid) {
		validationListeners.forEach(validationListener -> {
			validationListener.onValidation(getEntity(), isValid);
		});
	}

	protected void updateConstraintViolationsDisplay() {
		if (beanLevelViolations != null) {
			Collection<String> errorMessages = getFieldGroup().getBeanLevelValidationErrors();
			if (!errorMessages.isEmpty()) {
				StringBuilder sb = new StringBuilder();
				for (String e : errorMessages) {
					sb.append(e);
					sb.append("<br/>");
				}
				beanLevelViolations.setValue(sb.toString());
				beanLevelViolations.setVisible(true);
			} else {
				beanLevelViolations.setVisible(false);
				beanLevelViolations.setValue("");
			}
		}
	}

	public Component getConstraintViolationsDisplay() {
		if (beanLevelViolations == null) {
			beanLevelViolations = new RichText();
			beanLevelViolations.setVisible(false);
			beanLevelViolations.setStyleName(ValoTheme.LABEL_FAILURE);
		}
		return beanLevelViolations;
	}

	public boolean isValid() {
		return isValid;
	}

	protected void adjustSaveButtonState() {
		if (isEagerValidation() && isBound()) {
			// boolean beanModified = fieldGroup.isBeanModified();
			// getSaveButton().setEnabled(beanModified && isValid());
			getSaveButton().setEnabled(isValid());
		}
	}

	protected boolean isBound() {
		return fieldGroup != null;
	}

	protected void adjustResetButtonState() {
		if (popup != null && popup.getParent() != null) {
			// Assume cancel button in a form opened to a popup also closes
			// it, allows closing via cancel button by default
			getResetButton().setEnabled(true);
			return;
		}
		if (isEagerValidation() && isBound()) {
			boolean modified = fieldGroup.isBeanModified();
			getResetButton().setEnabled(modified || popup != null);
		}
	}

	public void addValidationListener(ValidationListener<T> listener) {
		validationListeners.add(listener);
	}

	public void removeValidationListener(ValidationListener<T> listener) {
		validationListeners.remove(listener);
	}

	public void addValidityChangedListener(ValidityChangedListener<T> listener) {
		addListener(ValidityChangedEvent.class, listener, ValidityChangedEvent.method);
	}

	public void removeValidityChangedListener(ValidityChangedListener<T> listener) {
		removeListener(ValidityChangedEvent.class, listener, ValidityChangedEvent.method);
	}

	private void fireValidityChangedEvent() {
		fireEvent(new ValidityChangedEvent(this));
	}

	public interface SavedHandler<T> extends Serializable {

		void onSave(T entity);
	}

	public interface ResetHandler<T> extends Serializable {

		void onReset(T entity);
	}

	public interface DeleteHandler<T> extends Serializable {

		void onDelete(T entity);
	}

	private T entity;
	private SavedHandler<T> savedHandler;
	private ResetHandler<T> resetHandler;
	private DeleteHandler<T> deleteHandler;
	private boolean eagerValidation = true;

	public boolean isEagerValidation() {
		return eagerValidation;
	}

	/**
	 * In case one is working with "detached entities" enabling eager validation
	 * will highly improve usability. The validity of the form will be updated
	 * on each changes and save/cancel buttons will reflect to the validity and
	 * possible changes.
	 *
	 * @param eagerValidation true if the form should have eager validation
	 */
	public void setEagerValidation(boolean eagerValidation) {
		this.eagerValidation = eagerValidation;
	}

	public MBeanFieldGroup<T> setEntity(Class<T> entityClass, T originalEntity) {
		lazyInit();
		this.entity = originalEntity;
		if (entity != null) {
			setCompositionRoot(createContent());
			if (isBound()) {
				fieldGroup.unbind();
			}
			binding = true;
			fieldGroup = bindEntity(entity);
			binding = false;

			for (Map.Entry<MBeanFieldGroup.MValidator<T>, Collection<AbstractComponent>> e : mValidators.entrySet()) {
				fieldGroup.addValidator(e.getKey(), e.getValue().toArray(new AbstractComponent[e.getValue().size()]));
			}
			for (Map.Entry<Class, AbstractComponent> e : validatorToErrorTarget.entrySet()) {
				fieldGroup.setValidationErrorTarget(e.getKey(), e.getValue());
			}

			isValid = fieldGroup.isValid();
			if (isEagerValidation()) {
				fieldGroup.withEagerValidation(this);
				notifyValidationListeners(isValid);
				adjustSaveButtonState();
				adjustResetButtonState();
			}
			fieldGroup.hideInitialEmpyFieldValidationErrors();
			setVisible(true);
			return fieldGroup;
		} else {
			setVisible(false);
			return null;
		}
	}

	@Deprecated
	public MBeanFieldGroup<T> setEntity(T originalEntity) {
		if (originalEntity != null)
			return this.setEntity((Class<T>) originalEntity.getClass(), originalEntity);
		return this.setEntity(null, null);
	}

	protected Component getBindingComponent() {
		return this;
	}

	/**
	 * Creates a field group, configures the fields, binds the entity to those
	 * fields
	 *
	 * @param entity The entity to bind
	 * @return the fieldGroup created
	 */
	protected MBeanFieldGroup<T> bindEntity(T entity) {
		return bindEntityWithComponent(entity, getBindingComponent());
	}

	final protected MBeanFieldGroup<T> bindEntityWithComponent(T entity, Component c) {
		return bindEntityWithComponentAndNestedProperties(entity, c, getNestedProperties());
	}

	final protected MBeanFieldGroup<T> bindEntityWithComponentAndNestedProperties(T entity, Component c, String... nestedProperties) {
		preBinding(entity);
		MBeanFieldGroup<T> beanFieldGroup = BeanBinder.bind(entity, c, nestedProperties);
		beanFieldGroup.setValidateAllProperties(false);
		postBinding(entity);
		return beanFieldGroup;
	}

	protected void preBinding(T entity) {
	}

	protected void postBinding(T entity) {
	}

	private String[] nestedProperties;

	public String[] getNestedProperties() {
		return nestedProperties;
	}

	public void setNestedProperties(String... nestedProperties) {
		this.nestedProperties = nestedProperties;
	}

	/**
	 * Sets the given object to be a handler for saved,reset,deleted, based on
	 * what it happens to implement.
	 *
	 * @param handler the handler to be set as saved/reset/delete handler
	 */
	public void setHandler(Object handler) {
		if (handler != null) {
			if (handler instanceof SavedHandler) {
				setSavedHandler((SavedHandler<T>) handler);
			}
			if (handler instanceof ResetHandler) {
				setResetHandler((ResetHandler) handler);
			}
			if (handler instanceof DeleteHandler) {
				setDeleteHandler((DeleteHandler) handler);
			}
		}
	}

	public void setSavedHandler(SavedHandler<T> savedHandler) {
		this.savedHandler = savedHandler;
		getSaveButton().setVisible(this.savedHandler != null);
	}

	public void setResetHandler(ResetHandler<T> resetHandler) {
		this.resetHandler = resetHandler;
		getResetButton().setVisible(this.resetHandler != null);
	}

	public void setDeleteHandler(DeleteHandler<T> deleteHandler) {
		this.deleteHandler = deleteHandler;
		getDeleteButton().setVisible(this.deleteHandler != null);
	}

	public ResetHandler<T> getResetHandler() {
		return resetHandler;
	}

	public SavedHandler<T> getSavedHandler() {
		return savedHandler;
	}

	public DeleteHandler<T> getDeleteHandler() {
		return deleteHandler;
	}

	public Window openInModalPopup() {
		popup = new Window(getModalWindowTitle(), this);
		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
		focusFirst();
		// localize popup...
		localizeRecursively(this);
		return popup;
	}

	/**
	 * @return the last Popup into which the Form was opened with
	 * #openInModalPopup method or null if the form hasn't been use in window
	 */
	public Window getPopup() {
		return popup;
	}

	/**
	 * If the form is opened into a popup window using openInModalPopup(), you
	 * you can use this method to close the popup.
	 */
	public void closePopup() {
		if (popup != null) {
			popup.close();
			popup = null;
		}
	}

	/**
	 * @return A default toolbar containing save/cancel/delete buttons
	 */
	public HorizontalLayout getToolbar() {
		MLabel spacer = new MLabel("").withFullWidth();
		busyIndicator.setVisible(false);
		busyIndicator.setIndeterminate(true);
		MHorizontalLayout layout = new MHorizontalLayout().withDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
		addButtonsToFooter(layout);
		layout.addComponents(spacer, busyIndicator, getSaveButton(), getResetButton(), getDeleteButton());
		layout.setExpandRatio(spacer, 1);
		return layout;
	}

	protected void addButtonsToFooter(HorizontalLayout footer) {
	}

	protected Button createCancelButton() {
		return new MButton(getCancelCaption()).withVisible(false);
	}

	private Button resetButton;

	public Button getResetButton() {
		if (resetButton == null) {
			setResetButton(createCancelButton());
		}
		return resetButton;
	}

	public void setResetButton(Button resetButton) {
		this.resetButton = resetButton;
		this.resetButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(Button.ClickEvent event) {
				reset(event);
			}
		});
	}

	protected Button createSaveButton() {
		return new PrimaryButton(getSaveCaption()).withVisible(false);
	}

	private Button saveButton;

	public void setSaveButton(Button saveButton) {
		this.saveButton = saveButton;
		saveButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(Button.ClickEvent event) {
				try {
					saveButton.setEnabled(false);
					busyIndicator.setVisible(true);
					UI.getCurrent().push();
					save(event);
				} finally {
					saveButton.setEnabled(true);
					busyIndicator.setVisible(false);
					UI.getCurrent().push();
				}
			}
		});
	}

	public Button getSaveButton() {
		if (saveButton == null) {
			setSaveButton(createSaveButton());
		}
		return saveButton;
	}

	protected Button createDeleteButton() {
		return new DeleteButton(getDeleteCaption()).withVisible(false);
	}

	private Button deleteButton;

	public void setDeleteButton(final Button deleteButton) {
		this.deleteButton = deleteButton;
		deleteButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(Button.ClickEvent event) {
				delete(event);
			}
		});
	}

	public Button getDeleteButton() {
		if (deleteButton == null) {
			setDeleteButton(createDeleteButton());
		}
		return deleteButton;
	}

	protected void save(Button.ClickEvent e) {
		savedHandler.onSave(getEntity());
		getFieldGroup().setBeanModified(false);
		adjustResetButtonState();
		adjustSaveButtonState();
	}

	protected void reset(Button.ClickEvent e) {
		resetHandler.onReset(getEntity());
		getFieldGroup().setBeanModified(false);
		adjustResetButtonState();
		adjustSaveButtonState();
	}

	protected void delete(Button.ClickEvent e) {
		deleteHandler.onDelete(getEntity());
	}

	/**
	 * Focuses the first field found from the form. It often improves UX to call
	 * this method, or focus another field, when you assign a bean for editing.
	 */
	public void focusFirst() {
		Component compositionRoot = getCompositionRoot();
		findFieldAndFocus(compositionRoot);
	}

	private boolean findFieldAndFocus(Component compositionRoot) {
		if (compositionRoot instanceof AbstractComponentContainer) {
			AbstractComponentContainer cc = (AbstractComponentContainer) compositionRoot;
			Iterator<Component> iterator = cc.iterator();
			while (iterator.hasNext()) {
				Component component = iterator.next();
				if (component.isReadOnly())
					continue;
				if (component instanceof AbstractTextField) {
					AbstractTextField abstractTextField = (AbstractTextField) component;
					abstractTextField.selectAll();
					return true;
				}
				if (component instanceof AbstractField) {
					AbstractField abstractField = (AbstractField) component;
					abstractField.focus();
					return true;
				}
				if (component instanceof AbstractSingleComponentContainer) {
					AbstractSingleComponentContainer container = (AbstractSingleComponentContainer) component;
					if (findFieldAndFocus(container.getContent())) {
						return true;
					}
				}
				if (component instanceof AbstractComponentContainer) {
					if (findFieldAndFocus(component)) {
						return true;
					}
				}
			}
		} else if (compositionRoot instanceof AbstractSingleComponentContainer) {
			AbstractSingleComponentContainer container = (AbstractSingleComponentContainer) compositionRoot;
			if (findFieldAndFocus(container.getContent())) {
				return true;
			}
		}
		return false;
	}

	Set<Field> lockedFields = new HashSet<>();

	public void lockFields() {
		lockFields(getCompositionRoot());
	}

	private void lockFields(Component compositionRoot) {
		if (compositionRoot instanceof AbstractComponentContainer) {
			AbstractComponentContainer cc = (AbstractComponentContainer) compositionRoot;
			Iterator<Component> iterator = cc.iterator();
			while (iterator.hasNext()) {
				Component component = iterator.next();
				if (component.isReadOnly())
					continue;
				if (component instanceof AbstractTextField) {
					AbstractTextField abstractTextField = (AbstractTextField) component;
					if (abstractTextField.isEnabled()) {
						lockedFields.add(abstractTextField);
					}
				}
				if (component instanceof AbstractField) {
					AbstractField abstractField = (AbstractField) component;
					if (abstractField.isEnabled()) {
						lockedFields.add(abstractField);
					}
				}
				if (component instanceof AbstractSingleComponentContainer) {
					AbstractSingleComponentContainer container = (AbstractSingleComponentContainer) component;
					lockFields(container.getContent());

				}
				if (component instanceof AbstractComponentContainer) {
					lockFields(component);
				}
			}
		} else if (compositionRoot instanceof AbstractSingleComponentContainer) {
			AbstractSingleComponentContainer container = (AbstractSingleComponentContainer) compositionRoot;
			lockFields(container.getContent());
		}
		lockedFields.forEach(field -> {
			field.setReadOnly(true);
		});
	}

	public void unlockFields() {
		lockedFields.forEach(field -> {
			field.setReadOnly(false);
		});
		lockedFields.clear();
	}

	/**
	 * This method should return the actual content of the form, including
	 * possible toolbar. Use setEntity(T entity) to fill in the data. Am example
	 * implementation could look like this:
	 * 
	 * <pre>
	 * <code>
	 * public class PersonForm extends AbstractForm&lt;Person&gt; {
	 *
	 *     private TextField firstName = new MTextField(&quot;First Name&quot;);
	 *     private TextField lastName = new MTextField(&quot;Last Name&quot;);
	 *
	 *    {@literal @}Override
	 *     protected Component createContent() {
	 *         return new MVerticalLayout(
	 *                 new FormLayout(
	 *                         firstName,
	 *                         lastName
	 *                 ),
	 *                 getToolbar()
	 *         );
	 *     }
	 * }
	 * </code>
	 * </pre>
	 *
	 * @return the content of the form
	 */
	protected abstract Component createContent();

	public MBeanFieldGroup<T> getFieldGroup() {
		return fieldGroup;
	}

	public T getEntity() {
		return entity;
	}

	private final LinkedHashMap<MBeanFieldGroup.MValidator<T>, Collection<AbstractComponent>> mValidators = new LinkedHashMap<MBeanFieldGroup.MValidator<T>, Collection<AbstractComponent>>();

	private final Map<Class, AbstractComponent> validatorToErrorTarget = new LinkedHashMap<Class, AbstractComponent>();

	public void setValidationErrorTarget(Class aClass, AbstractComponent errorTarget) {
		validatorToErrorTarget.put(aClass, errorTarget);
		if (getFieldGroup() != null) {
			getFieldGroup().setValidationErrorTarget(aClass, errorTarget);
		}
	}

	/**
	 * EXPERIMENTAL: The cross field validation support is still experimental
	 * and its API is likely to change.
	 *
	 * @param validator a validator that validates the whole bean making cross
	 * field validation much simpler
	 * @param fields the ui fields that this validator affects and on which a
	 * possible error message is shown.
	 * @return this FieldGroup
	 */
	public TRAbstractBaseForm<T> addValidator(MBeanFieldGroup.MValidator<T> validator, AbstractComponent... fields) {
		mValidators.put(validator, Arrays.asList(fields));
		if (getFieldGroup() != null) {
			getFieldGroup().addValidator(validator, fields);
		}
		return this;
	}

	public TRAbstractBaseForm<T> removeValidator(MBeanFieldGroup.MValidator<T> validator) {
		Collection<AbstractComponent> remove = mValidators.remove(validator);
		if (remove != null) {
			if (getFieldGroup() != null) {
				getFieldGroup().removeValidator(validator);
			}
		}
		return this;
	}

	/**
	 * Removes all MValidators added the MFieldGroup
	 *
	 * @return the instance
	 */
	public TRAbstractBaseForm<T> clearValidators() {
		mValidators.clear();
		if (getFieldGroup() != null) {
			getFieldGroup().clear();
		}
		return this;
	}

	public void setRequired(Field... fields) {
		for (Field field : fields) {
			field.setRequired(true);
		}
	}

	public String getModalWindowTitle() {
		return modalWindowTitle;
	}

	public void setModalWindowTitle(String modalWindowTitle) {
		this.modalWindowTitle = modalWindowTitle;
	}

	public String getCancelCaption() {
		return cancelCaption;
	}

	public void setCancelCaption(String cancelCaption) {
		this.cancelCaption = cancelCaption;
	}

	public String getSaveCaption() {
		return saveCaption;
	}

	public void setSaveCaption(String saveCaption) {
		this.saveCaption = saveCaption;
		if (saveButton != null)
			saveButton.setCaption(saveCaption);
	}

	public String getDeleteCaption() {
		return deleteCaption;
	}

	public void setDeleteCaption(String deleteCaption) {
		this.deleteCaption = deleteCaption;
	}

	public TRAbstractBaseForm<T> withI18NCaption(String saveCaption, String deleteCaption, String cancelCaption) {
		this.saveCaption = saveCaption;
		this.deleteCaption = deleteCaption;
		this.cancelCaption = cancelCaption;
		return this;
	}

	protected boolean isBinding() {
		return this.binding;
	}

	protected String localizedSingularValue(String key) {
		return VaadinUtils.localizedSingularValue(key);
	}

	protected String localizedPluralValue(String key) {
		return VaadinUtils.localizedSingularValue(key);
	}

	protected void localizeRecursively(Component component) {
		VaadinUtils.localizeRecursively(component);
	}

	protected String localizedSingularValue(Locale locale, String key) {
		return VaadinUtils.localizedSingularValue(key);
	}

	protected String localizedPluralValue(Locale locale, String key) {
		return VaadinUtils.localizedSingularValue(key);
	}

	protected void localizeRecursively(Locale locale, Component component) {
		VaadinUtils.localizeRecursively(component);
	}

}
