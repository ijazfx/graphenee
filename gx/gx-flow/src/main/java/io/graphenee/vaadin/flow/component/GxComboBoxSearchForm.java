package io.graphenee.vaadin.flow.component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.graphenee.util.callback.TRParamCallback;
import io.graphenee.vaadin.flow.GxAbstractEntityList;
import lombok.Setter;

public class GxComboBoxSearchForm<T> extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	private Button selectButton;
	private Button dismissButton;
	private Dialog dialog = null;

	private GxAbstractEntityList<T> searchGrid = null;

	private T entity = null;

	@Setter
	private T selectedEntity = null;

	@Setter
	public TRParamCallback<T> onEntitySelect = null;

	public GxComboBoxSearchForm() {
		setSizeFull();
		setMargin(false);
		setPadding(true);
		setSpacing(true);
		addClassName("gx-entity-form");
	}

	@SuppressWarnings("serial")
	public GxComboBoxSearchForm<T> build() {
		removeAll();
		if (searchGrid != null) {
			searchGrid.setEditable(false);
			add(searchGrid);
			searchGrid.refresh();
			searchGrid.entityGrid().select(selectedEntity);

			searchGrid.setOnSingleItemSelect(new TRParamCallback<T>() {

				@Override
				public void execute(T param) {
					entity = param;
					if (param != null) {
						selectButton.setEnabled(true);
					} else {
						selectButton.setEnabled(false);
					}
				}

			});
		}
		add(buildToolbar());
		return this;
	}

	private HorizontalLayout buildToolbar() {
		HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.addClassName("gx-footer");
		toolbar.getStyle().set("border-radius", "var(--lumo-border-radius)");
		toolbar.setWidthFull();
		toolbar.setPadding(false);

		selectButton = new Button("SELECT");
		selectButton.setEnabled(false);
		selectButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		selectButton.addClickListener(cl -> {
			onEntitySelect.execute(entity);
			if (dialog != null) {
				dialog.close();
			}
		});

		dismissButton = new Button("DISMISS");
		dismissButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
		dismissButton.addClickListener(cl -> {
			if (dialog != null) {
				dialog.close();
			}
		});

		dismissButton.getElement().getStyle().set("margin-left", "auto");
		toolbar.add(selectButton, dismissButton);

		return toolbar;
	}

	public Dialog showInDialog(GxAbstractEntityList<T> searchGrid) {
		this.searchGrid = searchGrid;
		build();
		dialog = new Dialog(GxComboBoxSearchForm.this);
		dialog.setMaxHeight("90%");
		dialog.setMaxWidth("90%");
		dialog.setModal(true);
		dialog.setCloseOnEsc(true);
		dialog.setDraggable(true);
		dialog.setResizable(true);
		dialog.setSizeFull();
		dialog.open();
		return dialog;
	}

}
