package io.graphenee.jbpm.embedded.exception;

@SuppressWarnings("serial")
public class GxCompleteTaskException extends GxTaskException {

	public GxCompleteTaskException(Exception e) {
		super(e);
	}

	public GxCompleteTaskException(String message) {
		super(message);
	}

	public GxCompleteTaskException(String message, Exception e) {
		super(message, e);
	}

}
