package io.graphenee.vaadin.flow;

import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.RandomStringUtils;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import io.graphenee.util.callback.TRErrorCallback;
import io.graphenee.util.callback.TRVoidCallback;
import jakarta.annotation.PostConstruct;

@CssImport("./styles/graphenee.css")
public abstract class GxAbstractResetPasswordView extends FlexLayout {

	private static final long serialVersionUID = 1L;

	private TextField usernameTextField;
	private PasswordField passwordField;
	private PasswordField retypePasswordField;

	public GxAbstractResetPasswordView() {
		setClassName("gx-login-view");
		setSizeFull();
		setFlexDirection(FlexDirection.COLUMN);
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);
	}

	@PostConstruct
	private void postBuild() {
		Div rootLayout = new Div();
		rootLayout.addClassName("gx-reset-password-layout");
		add(rootLayout);

		Image appLogo = flowSetup().appLogo();
		if (appLogo != null) {
			Div appLogoDiv = new Div(appLogo);
			appLogoDiv.addClassName("gx-login-app-logo");
			rootLayout.add(appLogoDiv);
		}

		Div heading = new Div();
		heading.addClassName("gx-login-header");
		Span appTitle = new Span(flowSetup().appTitle());
		appTitle.addClassName("gx-login-app-title");
		Span appVersion = new Span(flowSetup().appVersion());
		appVersion.addClassName("gx-login-app-version");
		Div appTitleVersion = new Div(appTitle, appVersion);
		appTitleVersion.addClassName("gx-login-app-title-version");
		heading.add(appTitleVersion);
		rootLayout.add(heading);

		// add content to loginForm
		Div form1 = new Div();
		form1.addClassName("gx-reset-password-form");
		usernameTextField = new TextField("Username or Email");
		usernameTextField.setAutoselect(true);
		usernameTextField.setRequired(true);

		Button sendResetKeyButton = new Button("Send Security Code");
		sendResetKeyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		// sendResetKeyButton.setEnabled(false);

		TextField resetKeyTextField = new TextField("Enter Security Code");
		resetKeyTextField.setMaxLength(6);
		resetKeyTextField.setEnabled(false);

		Button nextButton = new Button("Next");
		nextButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		nextButton.setEnabled(false);

		Button dismissButton = new Button("Back to Login");
		dismissButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
		dismissButton.setEnabled(true);

		form1.add(usernameTextField, sendResetKeyButton, resetKeyTextField, nextButton, dismissButton);

		passwordField = new PasswordField("New Password");
		retypePasswordField = new PasswordField("Re-type Password");
		retypePasswordField.setEnabled(false);
		Button changeButton = new Button("Save Password");
		changeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		changeButton.setEnabled(false);

		Div form2 = new Div();
		form2.addClassName("gx-reset-password-form");
		form2.add(passwordField, retypePasswordField, changeButton);
		form2.setVisible(false);

		rootLayout.add(form1, form2);

		AtomicReference<String> securityPin = new AtomicReference<>();

		// register listeners...
		usernameTextField.addValueChangeListener(event -> {
			sendResetKeyButton.setEnabled(event.getValue().length() > 0);
		});
		usernameTextField.setValueChangeMode(ValueChangeMode.EAGER);
		sendResetKeyButton.addClickListener(event -> {
			if (usernameTextField.isEmpty()) {
				usernameTextField.focus();
			} else {
				securityPin.set(RandomStringUtils.secureStrong().nextAlphabetic(6));
				sendSecurityPinToUser(securityPin.get(), usernameTextField.getValue(), () -> {
					boolean shouldEnable = true;
					resetKeyTextField.clear();
					resetKeyTextField.setEnabled(shouldEnable);
					resetKeyTextField.focus();
					Notification.show("The reset code has been sent to your registered email/phone.", 5000,
							Position.BOTTOM_CENTER);
				}, error -> {
					usernameTextField.focus();
					StringBuilder sb = new StringBuilder();
					sb.append(error.getMessage());
					Notification.show(sb.toString(), 5000, Position.BOTTOM_CENTER);
				});
			}
		});

		resetKeyTextField.addValueChangeListener(event -> {
			String key = event.getValue();
			if (key.length() == 6) {
				if (key.equals(securityPin.get()))
					nextButton.setEnabled(true);
				else {
					nextButton.setEnabled(false);
					StringBuilder sb = new StringBuilder();
					sb.append("The reset code is not valid, please try again.");
					Notification.show(sb.toString(), 5000, Position.BOTTOM_CENTER);
				}
			} else {
				nextButton.setEnabled(false);
			}
		});
		nextButton.addClickListener(event -> {
			form1.setVisible(false);
			form2.setVisible(true);
		});

		dismissButton.addClickListener(event -> {
			UI.getCurrent().navigate("login");
		});

		passwordField.setValueChangeMode(ValueChangeMode.EAGER);
		passwordField.addValueChangeListener(event -> {
			retypePasswordField.setEnabled(event.getValue().length() > 0);
		});

		retypePasswordField.addValueChangeListener(event -> {
			String password = passwordField.getValue();
			String retypedPassword = event.getValue();
			if (retypedPassword.length() == password.length()) {
				if (retypedPassword.equals(password))
					changeButton.setEnabled(true);
				else {
					changeButton.setEnabled(false);
					StringBuilder sb = new StringBuilder();
					sb.append("New password and re-typed password must match, please try again.");
					Notification.show(sb.toString(), 5000, Position.BOTTOM_CENTER);
				}
			} else {
				changeButton.setEnabled(false);
			}
		});

		changeButton.addClickListener(event -> {
			changePassword(usernameTextField.getValue(), retypePasswordField.getValue(), () -> {
				getUI().ifPresent(ui -> {
					ui.navigate("login");
				});
			}, error -> {
				Notification.show(error.getMessage(), 5000, Position.BOTTOM_CENTER);
			});
		});

		usernameTextField.focus();
		// end content addition

	}

	protected abstract GxAbstractFlowSetup flowSetup();

	protected abstract void sendSecurityPinToUser(String securityPin, String username, TRVoidCallback success,
			TRErrorCallback error);

	protected abstract void changePassword(String username, String password, TRVoidCallback success,
			TRErrorCallback error);

}
