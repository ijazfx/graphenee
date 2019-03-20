package io.graphenee.vaadin.component;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.viritin.button.MButton;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.vaadin.TRAbstractForm;

public abstract class TRAbstractEntityComboBox<T> extends CssLayout {

	private static final Logger L = LoggerFactory.getLogger(TRAbstractEntityComboBox.class);

	private Class<T> entityClass;
	private boolean isBuilt;
	private ComboBox comboBox;
	private MButton addButton;
	private MButton editButton;
	private MButton deleteButton;
	private CssLayout layout;
	private Object filter;

	BeanItemContainer<T> beanItemContainer;

	private AbstractComboBoxToolbarDelegate<T> delegate = null;

	public TRAbstractEntityComboBox(Class<T> entity) {
		this.entityClass = entity;
		if (!isSpringComponent()) {
			postConstruct();
		}
	}

	@PostConstruct
	private void postConstruct() {
		postInitialize();
	}

	protected void postInitialize() {
	}

	protected boolean isSpringComponent() {
		return this.getClass().getAnnotation(SpringComponent.class) != null;
	}

	public TRAbstractEntityComboBox<T> build() {
		if (!isBuilt) {
			addButton = new MButton(FontAwesome.PLUS, event -> {
				try {
					onAddButtonClick(initializeEntity(entityClass.newInstance()));
				} catch (Exception e) {
					L.warn(e.getMessage(), e);
				}
			}).withStyleName(ValoTheme.BUTTON_ICON_ONLY);

			editButton = new MButton(FontAwesome.PENCIL, event -> {
				try {
					T item = (T) comboBox.getValue();
					if (item != null) {
						preEdit(item);
						openEditorForm(item);
					}
				} catch (Exception e) {
					L.warn(e.getMessage(), e);
				}
			}).withStyleName(ValoTheme.BUTTON_ICON_ONLY);
			editButton.setEnabled(false);
			deleteButton = new MButton(FontAwesome.TRASH, event -> {
				if (shouldShowDeleteConfirmation()) {
					ConfirmDialog.show(UI.getCurrent(), "Are you sure to remove selected record?", e -> {
						if (e.isConfirmed()) {
							try {
								T item = (T) comboBox.getValue();
								if (item != null) {
									if (onDeleteEntity(item)) {
										beanItemContainer.removeItem(item);
										if (delegate != null) {
											delegate.onDelete(item);
										}
									}
								}
							} catch (Exception ex) {
								L.warn(e.getMessage(), ex);
							}
						}
					});
				} else {
					T item = (T) comboBox.getValue();
					if (item != null) {
						if (onDeleteEntity(item)) {
							beanItemContainer.removeItem(item);
							if (delegate != null) {
								delegate.onDelete(item);
							}
						}
					}
				}
			}).withStyleName(ValoTheme.BUTTON_ICON_ONLY);
			deleteButton.setEnabled(false);

			comboBox = new ComboBox();
			beanItemContainer = new BeanItemContainer<>(entityClass);
			comboBox.setContainerDataSource(beanItemContainer);
			comboBox.setItemCaptionPropertyId(comboBoxVisibleProperty());
			comboBox.setInputPrompt(comboBoxInputPrompt());
			comboBox.addValueChangeListener(entity -> {
				if (delegate != null) {
					delegate.onItemSelect((T) comboBox.getValue());
				}
				if (comboBox.getValue() != null) {
					editButton.setEnabled(true);
					deleteButton.setEnabled(true);
				}
			});

			layout = new CssLayout(addButton, comboBox, editButton, deleteButton);
			layout.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

			addComponent(layout);

			postBuild();
			isBuilt = true;
			refresh();
		}
		return this;
	}

	protected abstract TRAbstractForm<T> editorForm();

	private void openEditorForm(T item) {
		if (editorForm() != null) {
			editorForm().setEntity(entityClass, item);
			editorForm().setSavedHandler(entity -> {
				try {
					if (onSaveEntity(entity)) {
						if (delegate != null) {
							delegate.onSave(entity);
						}
						editorForm().closePopup();
						refresh();
					}
				} catch (Exception e) {
					L.warn(e.getMessage(), e);
				}
			});
			editorForm().openInModalPopup();
		}
	}

	public TRAbstractEntityComboBox<T> refresh() {
		if (filter != null) {
			return refresh(filter);
		}
		UI.getCurrent().access(() -> {
			List<T> entities = fetchEntities();
			beanItemContainer.removeAllItems();
			beanItemContainer.addAll(entities);
			UI.getCurrent().push();
		});
		return this;
	}

	public <F extends Object> TRAbstractEntityComboBox<T> refresh(F filter) {
		this.filter = filter;
		UI.getCurrent().access(() -> {
			List<T> entities = postFetch(fetchEntities(filter));
			beanItemContainer.removeAllItems();
			beanItemContainer.addAll(entities);
			UI.getCurrent().push();
		});
		return this;
	}

	protected T initializeEntity(T item) {
		return item;
	}

	protected abstract boolean onSaveEntity(T entity);

	protected abstract boolean onDeleteEntity(T entity);

	protected abstract List<T> fetchEntities();

	protected <F extends Object> List<T> fetchEntities(F filter) {
		return fetchEntities();
	}

	protected List<T> postFetch(List<T> fetchedEntities) {
		return fetchedEntities;
	}

	public void setAddButtonEnable(boolean isEnable) {
		if (addButton != null) {
			addButton.setEnabled(isEnable);
		}
	}

	public void setEditButtonEnable(boolean isEnable) {
		if (editButton != null) {
			editButton.setEnabled(isEnable);
		}
	}

	public void setDeleteButtonEnable(boolean isEnable) {
		if (deleteButton != null) {
			deleteButton.setEnabled(isEnable);
		}
	}

	public void setComboBoxEnable(boolean isEnable) {
		if (comboBox != null) {
			comboBox.setEnabled(isEnable);
		}
	}

	public void clearComboBox() {
		if (this.comboBox != null) {
			this.comboBox.clear();
		}
	}

	public static interface AbstractComboBoxToolbarDelegate<T> {
		default void onItemSelect(T entity) {
		}

		default void onSave(T entity) {
		}

		default void onDelete(T entity) {
		}
	}

	protected void preEdit(T item) {
	}

	protected void postBuild() {
	}

	protected void onAddButtonClick(T entity) {
		preEdit(entity);
		openEditorForm(entity);
	}

	public MButton getAddButton() {
		return addButton;
	}

	public MButton getEditButton() {
		return editButton;
	}

	public MButton getDeleteButton() {
		return deleteButton;
	}

	public ComboBox getComboBox() {
		return comboBox;
	}

	protected boolean shouldShowDeleteConfirmation() {
		return false;
	}

	protected abstract String toolbarCaption();

	protected abstract String comboBoxVisibleProperty();

	protected abstract String comboBoxInputPrompt();

	public void setDelegate(AbstractComboBoxToolbarDelegate<T> delegate) {
		this.delegate = delegate;
	}

}
