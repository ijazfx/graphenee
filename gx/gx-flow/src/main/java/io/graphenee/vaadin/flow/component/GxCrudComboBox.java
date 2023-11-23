package io.graphenee.vaadin.flow.component;

import java.util.stream.Stream;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

import io.graphenee.vaadin.flow.base.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm.EntityFormDelegate;
import jakarta.annotation.PostConstruct;

public abstract class GxCrudComboBox<T> extends ComboBox<T> {
	private static final long serialVersionUID = 1L;

	private Class<T> entityClass;

	private Icon addIcon;
	private Icon editIcon;

	public GxCrudComboBox(Class<T> entityClass, String label) {
		this(entityClass);
		setLabel(label);
	}

	public GxCrudComboBox(Class<T> entityClass) {
		this.entityClass = entityClass;
		setWidth("100%");
	}

	@PostConstruct
	private GxCrudComboBox<T> build() {
		addIcon = new Icon(VaadinIcon.PLUS);
		addIcon.getElement().setAttribute("slot", "suffix");
		addIcon.getElement().getStyle().set("cursor", "default");
		addIcon.getElement().executeJs("this.addEventListener('click',function(e){e.stopPropagation();})");

		getElement().appendVirtualChild(addIcon.getElement());
		getElement().executeJs("this.$.input.appendChild($0, this.$.toggleButton)", addIcon);

		editIcon = new Icon(VaadinIcon.EDIT);
		editIcon.getElement().setAttribute("slot", "suffix");
		editIcon.getElement().getStyle().set("cursor", "default");
		editIcon.getElement().executeJs("this.addEventListener('click',function(e){e.stopPropagation();})");

		getElement().appendVirtualChild(editIcon.getElement());
		getElement().executeJs("this.$.input.appendChild($0, this.$.toggleButton)", editIcon);

		addIcon.addClickListener(cl -> {
			try {
				openForm(entityClass.getDeclaredConstructor().newInstance());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		editIcon.addClickListener(cl -> {
			if (getValue() != null) {
				openForm(getValue());
			}

		});

		return this;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		validateState(readOnly);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		validateState(!enabled);
	};

	private void validateState(Boolean value) {
		addIcon.getElement().setEnabled(!value);
		addIcon.getStyle().set("opacity", value ? "0.2" : "1.0");

		editIcon.getElement().setEnabled(!value);
		editIcon.getStyle().set("opacity", value ? "0.2" : "1.0");
	}

	protected abstract Stream<T> getData();

	protected abstract void onSave(T entity);

	protected abstract GxAbstractEntityForm<T> getEntityForm(T entity);

	private void openForm(T entity) {
		preEdit(entity);
		GxAbstractEntityForm<T> entityForm = getEntityForm(entity);
		if (entityForm != null) {
			entityForm.showInDialog(entity);
		}
		EntityFormDelegate<T> delegate = new EntityFormDelegate<T>() {

			@Override
			public void onSave(T entity) {
				GxCrudComboBox.this.onSave(entity);
				refresh();
			}

		};
		entityForm.setDelegate(delegate);
	}

	protected void preEdit(T entity) {
	}

	public void refresh() {
		T value = getValue();
		setItems(getData());
		if (value != null) {
			setValue(value);
		}
	}

}
