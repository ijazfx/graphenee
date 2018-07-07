package io.graphenee.jbpm.embedded.exception;

@SuppressWarnings("serial")
public class GxAssignTaskException extends GxTaskException {

	public GxAssignTaskException(Exception e) {
		super(e);
	}

	public GxAssignTaskException(String message) {
		super(message);
	}

	public GxAssignTaskException(String message, Exception e) {
		super(message, e);
	}

}
