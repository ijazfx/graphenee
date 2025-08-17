package io.graphenee.vaadin.flow.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog.ConfirmEvent;

import io.graphenee.util.callback.TRParamCallback;

/**
 * A factory for creating dialogs.
 */
public class DialogFactory {

	/**
	 * Creates a new confirmation dialog.
	 * @param component The component to display in the dialog.
	 * @param confirmCallback The callback to execute when the dialog is confirmed.
	 * @return The new dialog.
	 */
	public static ConfirmDialog confirmDialog(Component component, TRParamCallback<ConfirmEvent> confirmCallback) {
		return customDialog("Confirmation", component, "Yes", "No", confirmCallback);
	}

	/**
	 * Creates a new question dialog.
	 * @param title The title of the dialog.
	 * @param component The component to display in the dialog.
	 * @param confirmCallback The callback to execute when the dialog is confirmed.
	 * @return The new dialog.
	 */
	public static ConfirmDialog questionDialog(String title, Component component, TRParamCallback<ConfirmEvent> confirmCallback) {
		return customDialog(title, component, "Yes", "No", confirmCallback);
	}

	/**
	 * Creates a new custom dialog.
	 * @param title The title of the dialog.
	 * @param component The component to display in the dialog.
	 * @param confirmText The text of the confirm button.
	 * @param cancelText The text of the cancel button.
	 * @param callback The callback to execute when the dialog is confirmed.
	 * @return The new dialog.
	 */
	public static ConfirmDialog customDialog(String title, Component component, String confirmText, String cancelText, TRParamCallback<ConfirmEvent> callback) {
		ConfirmDialog d = new ConfirmDialog(title, null, confirmText, dlg -> {
			callback.execute(dlg);
			if(dlg.getSource().isOpened()) {
				dlg.getSource().close();
			}
		});
		d.setText(component);
		d.setCancelable(true);
		d.setCancelButtonTheme(ButtonVariant.LUMO_ERROR.getVariantName());
		d.setCancelText(cancelText);
		d.addClassName("gx-dialog");
		return d;
	}

	/**
	 * Creates a new confirmation dialog.
	 * @param message The message to display in the dialog.
	 * @param confirmCallback The callback to execute when the dialog is confirmed.
	 * @return The new dialog.
	 */
	public static ConfirmDialog confirmDialog(String message, TRParamCallback<ConfirmEvent> confirmCallback) {
		return customDialog("Confirmation", message, "Yes", "No", confirmCallback);
	}

	/**
	 * Creates a new question dialog.
	 * @param title The title of the dialog.
	 * @param message The message to display in the dialog.
	 * @param confirmCallback The callback to execute when the dialog is confirmed.
	 * @return The new dialog.
	 */
	public static ConfirmDialog questionDialog(String title, String message, TRParamCallback<ConfirmEvent> confirmCallback) {
		return customDialog(title, message, "Yes", "No", confirmCallback);
	}

	/**
	 * Creates a new custom dialog.
	 * @param title The title of the dialog.
	 * @param message The message to display in the dialog.
	 * @param confirmText The text of the confirm button.
	 * @param cancelText The text of the cancel button.
	 * @param callback The callback to execute when the dialog is confirmed.
	 * @return The new dialog.
	 */
	public static ConfirmDialog customDialog(String title, String message, String confirmText, String cancelText, TRParamCallback<ConfirmEvent> callback) {
		final ConfirmDialog d = new ConfirmDialog(title, message, confirmText, dlg -> {
			callback.execute(dlg);
			if(dlg.getSource().isOpened()) {
				dlg.getSource().close();
			}
		});
		d.setCancelable(true);
		d.setCancelButtonTheme(ButtonVariant.LUMO_ERROR.getVariantName());
		d.setCancelText(cancelText);
		d.addClassName("gx-dialog");
		return d;
	}

}
