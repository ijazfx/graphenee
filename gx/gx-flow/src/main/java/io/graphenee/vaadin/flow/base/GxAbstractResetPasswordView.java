package io.graphenee.vaadin.flow.base;

import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.RandomStringUtils;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import io.graphenee.util.callback.TRErrorCallback;
import io.graphenee.util.callback.TRVoidCallback;
import jakarta.annotation.PostConstruct;

@CssImport("./styles/gx-common.css")
public abstract class GxAbstractResetPasswordView extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private Image appLogo;

	private TextField usernameTextField;
	private PasswordField passwordField;
	private PasswordField retypePasswordField;

	public GxAbstractResetPasswordView() {
		setSizeFull();
		setClassName("gx-login-view");
		setJustifyContentMode(JustifyContentMode.CENTER);
		setAlignItems(Alignment.CENTER);
	}

	@PostConstruct
	private void postBuild() {
		FlexLayout headingLayout = new FlexLayout();
		headingLayout.setAlignItems(Alignment.BASELINE);
		headingLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
		headingLayout.setWidth("328px");
		Span heading = new Span(flowSetup().appTitle());
		heading.getStyle().set("color", "var(--app-layout-bar-font-color)");
		heading.getStyle().set("font-size", "var(--lumo-font-size-xxl)");
		Span version = new Span(flowSetup().appVersion());
		version.getStyle().set("color", "var(--app-layout-bar-font-color)");
		version.getStyle().set("font-size", "var(--lumo-font-size-s)");
		headingLayout.add(heading, version);

		add(headingLayout);

		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setWidth("328px");
		rootLayout.setSpacing(false);
		rootLayout.setPadding(false);
		rootLayout.getElement().getStyle().set("border-radius", "var(--lumo-border-radius-l)");
		rootLayout.getElement().getStyle().set("background", "white");

		add(rootLayout);

		appLogo = flowSetup().appLogo();
		if (appLogo != null) {
			appLogo.getElement().getStyle().set("padding-top", "var(--lumo-space-l)");
			appLogo.setWidth("100px");
			rootLayout.add(appLogo);
			rootLayout.setHorizontalComponentAlignment(Alignment.CENTER, appLogo);
		}

		// add content to loginForm
		FormLayout form1 = new FormLayout();
		usernameTextField = new TextField("Username");
		usernameTextField.setAutoselect(true);

		Button sendResetKeyButton = new Button("Send Security Pin");
		sendResetKeyButton.setEnabled(false);

		form1.add(usernameTextField, sendResetKeyButton);

		FormLayout form2 = new FormLayout();
		TextField resetKeyTextField = new TextField("Enter Security Pin");
		resetKeyTextField.setMaxLength(6);
		resetKeyTextField.setEnabled(false);

		Button nextButton = new Button("Next");
		nextButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		nextButton.setEnabled(false);

		form2.add(resetKeyTextField, nextButton);

		passwordField = new PasswordField("New Password");
		retypePasswordField = new PasswordField("Re-type Password");
		retypePasswordField.setEnabled(false);
		Button changeButton = new Button("Change");
		changeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		changeButton.setEnabled(false);

		FormLayout form3 = new FormLayout();
		form3.add(passwordField, retypePasswordField, changeButton);

		VerticalLayout formLayout = new VerticalLayout();
		formLayout.getElement().getStyle().set("padding", "var(--lumo-space-l)");
		form1.getElement().getStyle().set("padding", "var(--lumo-space-s)");
		form2.getElement().getStyle().set("padding", "var(--lumo-space-s)");
		form3.getElement().getStyle().set("padding", "var(--lumo-space-s)");
		formLayout.add(form1, form2, form3);
		rootLayout.add(formLayout);

		form3.setVisible(false);

		AtomicReference<String> securityPin = new AtomicReference<>();

		// register listeners...
		usernameTextField.addValueChangeListener(event -> {
			sendResetKeyButton.setEnabled(event.getValue().length() > 0);
		});
		usernameTextField.setValueChangeMode(ValueChangeMode.EAGER);
		sendResetKeyButton.addClickListener(event -> {
			securityPin.set(RandomStringUtils.randomAlphanumeric(6));
			sendSecurityPinToUser(securityPin.get(), usernameTextField.getValue(), () -> {
				boolean shouldEnable = true;
				resetKeyTextField.clear();
				resetKeyTextField.setEnabled(shouldEnable);
				resetKeyTextField.focus();
				Notification.show("The reset pin has been sent to your registered email/phone.", 5000, Position.BOTTOM_CENTER);
			}, error -> {
				usernameTextField.focus();
				StringBuilder sb = new StringBuilder();
				sb.append(error.getMessage());
				Notification.show(sb.toString(), 5000, Position.BOTTOM_CENTER);
			});
		});

		resetKeyTextField.addValueChangeListener(event -> {
			String key = event.getValue();
			if (key.length() == 6) {
				if (key.equals(securityPin.get()))
					nextButton.setEnabled(true);
				else {
					nextButton.setEnabled(false);
					StringBuilder sb = new StringBuilder();
					sb.append("The reset pin is not valid, please try again.");
					Notification.show(sb.toString(), 5000, Position.BOTTOM_CENTER);
				}
			} else {
				nextButton.setEnabled(false);
			}
		});
		nextButton.addClickListener(event -> {
			form1.setVisible(false);
			form2.setVisible(false);
			form3.setVisible(true);
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

	protected abstract void sendSecurityPinToUser(String securityPin, String username, TRVoidCallback success, TRErrorCallback error);

	protected abstract void changePassword(String username, String password, TRVoidCallback success, TRErrorCallback error);

}
