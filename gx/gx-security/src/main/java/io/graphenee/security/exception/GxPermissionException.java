package io.graphenee.security.exception;

@SuppressWarnings("serial")
public class GxPermissionException extends Exception {
	public GxPermissionException(Exception e) {
		super(e);
	}

	public GxPermissionException(String message, Throwable cause) {
		super(message, cause);
	}

	public GxPermissionException(String message) {
		super(message);
	}
}
