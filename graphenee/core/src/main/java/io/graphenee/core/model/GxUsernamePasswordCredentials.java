package io.graphenee.core.model;

public class GxUsernamePasswordCredentials extends GxAbstractCredentials {

	public static final String USERNAME_KEY = "username";
	public static final String PASSWORD_KEY = "password";

	public GxUsernamePasswordCredentials(String username, String password) {
		setCredential(USERNAME_KEY, username);
		setCredential(PASSWORD_KEY, password);
	}

	public String getUsername() {
		return getCredential(USERNAME_KEY);
	}

	public String getPassword() {
		return getCredential(PASSWORD_KEY);
	}

}
