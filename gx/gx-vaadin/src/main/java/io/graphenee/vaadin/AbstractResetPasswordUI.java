package io.graphenee.vaadin;

import com.vaadin.ui.Component;

import io.graphenee.core.callback.TRErrorCallback;
import io.graphenee.core.callback.TRVoidCallback;
import io.graphenee.core.exception.AuthenticationFailedException;
import io.graphenee.core.model.GxAuthenticatedUser;
import io.graphenee.vaadin.view.ResetPasswordPanel;
import io.graphenee.vaadin.view.ResetPasswordPanel.ResetPasswordPanelDelegate;

@SuppressWarnings("serial")
public abstract class AbstractResetPasswordUI<C extends GxAuthenticatedUser> extends AbstractDashboardUI {

	protected Component createComponent() {
		setStyleName("loginview");
		ResetPasswordPanel resetPasswordComponent = new ResetPasswordPanel(dashboardSetup());
		resetPasswordComponent.setDelegate(new ResetPasswordPanelDelegate() {

			@Override
			public void sendKeyToUser(String key, String username, TRVoidCallback success, TRErrorCallback error) {
				try {
					GxAuthenticatedUser user = findUserByUsernameOrEmail(username);
					sendResetKeyToUser(key, user);
					success.execute();
				} catch (Exception e) {
					error.execute(e);
				}
			}

			@Override
			public void changePassword(String username, String password, TRVoidCallback success, TRErrorCallback error) {
				if (username.equals(password)) {
					success.execute();
				} else {
					error.execute(new Exception("Password does not qualify for the password policy."));
				}
			}

		});
		return resetPasswordComponent;
	}

	protected abstract void sendResetKeyToUser(String token, GxAuthenticatedUser user) throws AuthenticationFailedException;

	protected abstract GxAuthenticatedUser findUserByUsernameOrEmail(String usernameOrEmail) throws AuthenticationFailedException;

}
