package io.graphenee.vaadin.flow.component;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;

import io.graphenee.util.callback.TRParamCallback;
import io.graphenee.vaadin.flow.GxAbstractEntityList;
import lombok.Setter;

/**
 * GxComboBox
 * @param <T> The bean type.
 */
public class GxComboBox<T> extends ComboBox<T> {
	private static final long serialVersionUID = 1L;

	@Setter
	private GxAbstractEntityList<T> searchGrid = null;

	private GxComboBoxSearchForm<T> searchForm;

	public GxComboBox(String label) {
		this();
		setLabel(label);
	}

	public GxComboBox() {
		Icon icon = new Icon(VaadinIcon.SEARCH);
		icon.getElement().setAttribute("slot", "suffix");
		icon.getElement().getStyle().set("cursor", "default");
		icon.getElement().executeJs("this.addEventListener('click',function(e){e.stopPropagation();})");

		getElement().appendVirtualChild(icon.getElement());
		setWidth("100%");
		getElement().executeJs("this.$.input.appendChild($0, this.$.toggleButton)", icon);

		searchForm = new GxComboBoxSearchForm<T>();
		icon.addClickListener(cl -> {
			if (searchGrid != null) {
				searchForm.setSelectedEntity(getValue());
				searchForm.showInDialog(searchGrid);
				searchForm.setOnEntitySelect(new TRParamCallback<T>() {

					@Override
					public void execute(T param) {
						setValue(param);
					}

				});
			} else {
				Notification notification = Notification.show("Entity search grid not set!", 5000, Position.BOTTOM_CENTER);
				notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		});
	}

}