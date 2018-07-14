package io.graphenee.jbpm.embedded.exception;

@SuppressWarnings("serial")
public class GxTaskException extends Exception {

	public GxTaskException(Exception e) {
		super(e);
	}

	public GxTaskException(String message, Throwable cause) {
		super(message, cause);
	}

	public GxTaskException(String message) {
		super(message);
	}

}
