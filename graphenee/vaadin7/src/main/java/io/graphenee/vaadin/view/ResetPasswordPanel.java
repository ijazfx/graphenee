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
package io.graphenee.vaadin.view;

import org.apache.commons.lang3.RandomStringUtils;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MPasswordField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.core.callback.TRErrorCallback;
import io.graphenee.core.callback.TRVoidCallback;
import io.graphenee.gx.theme.graphenee.GrapheneeTheme;
import io.graphenee.vaadin.AbstractDashboardSetup;
import io.graphenee.vaadin.ui.GxNotification;

@SuppressWarnings("serial")
public class ResetPasswordPanel extends Panel {

	private ResetPasswordPanelDelegate delegate;
	private MTextField usernameTextField;
	private MPasswordField passwordField;
	private MPasswordField retypePasswordField;

	public ResetPasswordPanel(AbstractDashboardSetup dashboardSetup) {
		setStyleName(ValoTheme.PANEL_BORDERLESS);
		addStyleName(GrapheneeTheme.STYLE_CENTER);
		setSizeFull();
		setContent(createContent());
	}

	protected Component createContent() {
		MVerticalLayout rootLayout = new MVerticalLayout().withMargin(false).withSpacing(false);
		rootLayout.setWidth("330px");
		rootLayout.setStyleName("login-panel");

		MVerticalLayout sendResetKeyLayout = new MVerticalLayout().withMargin(false).withSpacing(false);

		MLabel forgotPasswordTitle = new MLabel("Reset Password").withStyleName(ValoTheme.LABEL_H3);
		sendResetKeyLayout.addComponent(forgotPasswordTitle);

		MVerticalLayout layout1 = new MVerticalLayout().withFullWidth().withMargin(false).withSpacing(false).withDefaultComponentAlignment(Alignment.BOTTOM_LEFT);
		layout1.setWidth("100%");
		usernameTextField = new MTextField().withCaption("Username / Email").withFullWidth();

		MButton sendResetKeyButton = new MButton("Send Reset Key");
		sendResetKeyButton.setStyleName(GrapheneeTheme.STYLE_MARGIN_VERTICAL);
		sendResetKeyButton.setEnabled(false);

		layout1.addComponents(usernameTextField, sendResetKeyButton);
		//layout1.setExpandRatio(usernameTextField, 1);
		sendResetKeyLayout.addComponent(layout1);

		MVerticalLayout layout2 = new MVerticalLayout().withFullWidth().withMargin(false).withSpacing(false).withDefaultComponentAlignment(Alignment.BOTTOM_RIGHT);
		layout2.setWidth("100%");
		MTextField resetKeyTextField = new MTextField().withInputPrompt("Enter Reset Key").withFullWidth();
		resetKeyTextField.setMaxLength(6);
		resetKeyTextField.setEnabled(false);

		MButton nextButton = new MButton("Next").withStyleName(ValoTheme.BUTTON_PRIMARY);
		nextButton.addStyleName(GrapheneeTheme.STYLE_MARGIN_TOP);
		nextButton.setEnabled(false);

		layout2.addComponents(resetKeyTextField, nextButton);
		//layout2.setExpandRatio(resetKeyTextField, 1);
		sendResetKeyLayout.addComponent(layout2);

		passwordField = new MPasswordField("New Password").withFullWidth();
		retypePasswordField = new MPasswordField("Re-type Password").withFullWidth();
		retypePasswordField.setEnabled(false);
		MButton changeButton = new MButton("Change").withStyleName(ValoTheme.BUTTON_PRIMARY);
		changeButton.setEnabled(false);

		MVerticalLayout layout3 = new MVerticalLayout().withFullWidth().withMargin(false).withDefaultComponentAlignment(Alignment.BOTTOM_CENTER);
		layout3.addComponents(passwordField, retypePasswordField, changeButton);
		layout3.setComponentAlignment(changeButton, Alignment.MIDDLE_RIGHT);

		sendResetKeyLayout.addComponent(layout3);

		MVerticalLayout changePasswordLayout = new MVerticalLayout().withMargin(false);

		MLabel changePasswordTitle = new MLabel("Change Password").withStyleName(ValoTheme.LABEL_H3);
		changePasswordLayout.addComponents(changePasswordTitle, layout3);

		rootLayout.addComponents(sendResetKeyLayout, changePasswordLayout);
		changePasswordLayout.setVisible(false);

		final String[] keys = new String[1];

		// register listeners...
		usernameTextField.addTextChangeListener(event -> {
			sendResetKeyButton.setEnabled(event.getText().length() > 0);
		});
		usernameTextField.setTextChangeEventMode(TextChangeEventMode.EAGER);
		sendResetKeyButton.addClickListener(event -> {
			keys[0] = RandomStringUtils.randomAlphanumeric(6);
			if (delegate != null) {
				delegate.sendKeyToUser(keys[0], usernameTextField.getValue(), () -> {
					boolean shouldEnable = true;
					resetKeyTextField.clear();
					resetKeyTextField.setEnabled(shouldEnable);
					resetKeyTextField.focus();
					GxNotification notification = GxNotification.tray("Your key to reset password has been sent, please check your email.");
					notification.setDelayMsec(5000);
					notification.show(Page.getCurrent());
				}, error -> {
					usernameTextField.selectAll();
					usernameTextField.focus();
					StringBuilder sb = new StringBuilder();
					sb.append(error.getMessage());
					GxNotification notification = GxNotification.closable("Send Key Failed", sb.toString(), Type.ERROR_MESSAGE, Position.BOTTOM_CENTER);
					notification.setDelayMsec(5000);
					notification.show(Page.getCurrent());
				});
			} else {
				GxNotification notification = GxNotification.closable("Incomplete Implementation", "No delegate available of type ResetPasswordPanel.ResetPasswordPanelDelegate",
						Type.ERROR_MESSAGE, Position.BOTTOM_CENTER);
				notification.setDelayMsec(5000);
				notification.show(Page.getCurrent());
			}
		});
		resetKeyTextField.addTextChangeListener(event -> {
			String key = event.getText();
			if (key.length() == 6) {
				if (key.equals(keys[0]))
					nextButton.setEnabled(true);
				else {
					nextButton.setEnabled(false);
					StringBuilder sb = new StringBuilder();
					sb.append("The reset key is not valid, please try again.");
					GxNotification notification = GxNotification.closable("Verification Failed", sb.toString(), Type.ERROR_MESSAGE, Position.BOTTOM_CENTER);
					notification.setDelayMsec(5000);
					notification.show(Page.getCurrent());
				}
			} else {
				nextButton.setEnabled(false);
			}
		});
		nextButton.addClickListener(event -> {
			sendResetKeyLayout.setVisible(false);
			changePasswordLayout.setVisible(true);
		});

		passwordField.setTextChangeEventMode(TextChangeEventMode.EAGER);
		passwordField.addTextChangeListener(event -> {
			retypePasswordField.setEnabled(event.getText().length() > 0);
		});

		retypePasswordField.addTextChangeListener(event -> {
			String password = passwordField.getValue();
			String retypedPassword = event.getText();
			if (retypedPassword.length() == password.length()) {
				if (retypedPassword.equals(password))
					changeButton.setEnabled(true);
				else {
					changeButton.setEnabled(false);
					StringBuilder sb = new StringBuilder();
					sb.append("New password and re-typed password must match, please try again.");
					GxNotification notification = GxNotification.closable("Match Password Failed", sb.toString(), Type.ERROR_MESSAGE, Position.BOTTOM_CENTER);
					notification.setDelayMsec(5000);
					notification.show(Page.getCurrent());
				}
			} else {
				changeButton.setEnabled(false);
			}
		});

		changeButton.addClickListener(event -> {
			if (delegate != null) {
				delegate.changePassword(usernameTextField.getValue(), retypePasswordField.getValue(), () -> {
					String location = Page.getCurrent().getLocation().toString();
					location = location.replace("reset-password", "login");
					Page.getCurrent().setLocation(location);
				}, error -> {
					GxNotification notification = GxNotification.closable("Change Password Failed", error.getMessage(), Type.ERROR_MESSAGE, Position.BOTTOM_CENTER);
					notification.setDelayMsec(5000);
					notification.show(Page.getCurrent());
				});
			} else {
				GxNotification notification = GxNotification.closable("Incomplete Implementation", "No delegate available of type ResetPasswordPanel.ResetPasswordPanelDelegate",
						Type.ERROR_MESSAGE, Position.BOTTOM_CENTER);
				notification.setDelayMsec(5000);
				notification.show(Page.getCurrent());
			}
		});

		usernameTextField.focus();

		return rootLayout;
	}

	public void setDelegate(ResetPasswordPanelDelegate delegate) {
		this.delegate = delegate;
	}

	public static interface ResetPasswordPanelDelegate {
		void sendKeyToUser(String key, String username, TRVoidCallback success, TRErrorCallback error);

		void changePassword(String username, String password, TRVoidCallback success, TRErrorCallback error);
	}

}
