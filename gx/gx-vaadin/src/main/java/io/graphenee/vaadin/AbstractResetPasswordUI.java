package io.graphenee.vaadin;

import com.vaadin.ui.Component;

import io.graphenee.core.callback.TRErrorCallback;
import io.graphenee.core.callback.TRVoidCallback;
import io.graphenee.core.exception.ChangePasswordFailedException;
import io.graphenee.core.exception.UserCommunicationException;
import io.graphenee.vaadin.view.ResetPasswordPanel;
import io.graphenee.vaadin.view.ResetPasswordPanel.ResetPasswordPanelDelegate;

@SuppressWarnings("serial")
public abstract class AbstractResetPasswordUI extends AbstractDashboardUI {

	protected Component createComponent() {
		setStyleName("loginview");
		ResetPasswordPanel resetPasswordComponent = new ResetPasswordPanel(dashboardSetup());
		resetPasswordComponent.setDelegate(new ResetPasswordPanelDelegate() {

			@Override
			public void sendKeyToUser(String key, String username, TRVoidCallback success, TRErrorCallback error) {
				try {
					sendResetKeyToUser(key, username);
					success.execute();
				} catch (Exception e) {
					error.execute(e);
				}
			}

			@Override
			public void changePassword(String username, String password, TRVoidCallback success, TRErrorCallback error) {
				try {
					changeUserPassword(username, password);
					success.execute();
				} catch (Exception e) {
					error.execute(e);
				}
			}

		});
		return resetPasswordComponent;
	}

	protected abstract void sendResetKeyToUser(String token, String usernameOrEmail) throws UserCommunicationException;

	protected abstract void changeUserPassword(String usernameOrEmail, String password) throws ChangePasswordFailedException;

}
