package io.graphenee.vaadin.flow.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog.ConfirmEvent;

import io.graphenee.util.callback.TRParamCallback;

public class DialogFactory {

	public static ConfirmDialog confirmDialog(Component component, TRParamCallback<ConfirmEvent> confirmCallback) {
		return customDialog("Confirmation", component, "Yes", "No", confirmCallback);
	}

	public static ConfirmDialog questionDialog(String title, Component component, TRParamCallback<ConfirmEvent> confirmCallback) {
		return customDialog(title, component, "Yes", "No", confirmCallback);
	}

	public static ConfirmDialog customDialog(String title, Component component, String confirmText, String cancelText, TRParamCallback<ConfirmEvent> callback) {
		ConfirmDialog d = new ConfirmDialog(title, null, confirmText, dlg -> {
			callback.execute(dlg);
		});
		d.setText(component);
		d.setCancelable(true);
		d.setCancelButtonTheme(ButtonVariant.LUMO_ERROR.getVariantName());
		d.setCancelText(cancelText);
		d.addClassName("gx-dialog");
		return d;
	}

	public static ConfirmDialog confirmDialog(String message, TRParamCallback<ConfirmEvent> confirmCallback) {
		return customDialog("Confirmation", message, "Yes", "No", confirmCallback);
	}

	public static ConfirmDialog questionDialog(String title, String message, TRParamCallback<ConfirmEvent> confirmCallback) {
		return customDialog(title, message, "Yes", "No", confirmCallback);
	}

	public static ConfirmDialog customDialog(String title, String message, String confirmText, String cancelText, TRParamCallback<ConfirmEvent> callback) {
		ConfirmDialog d = new ConfirmDialog(title, message, confirmText, dlg -> {
			callback.execute(dlg);
		});
		d.setCancelable(true);
		d.setCancelButtonTheme(ButtonVariant.LUMO_ERROR.getVariantName());
		d.setCancelText(cancelText);
		d.addClassName("gx-dialog");
		return d;
	}

}
