package io.graphenee.jbpm.embedded.exception;

@SuppressWarnings("serial")
public class GxSkipTaskException extends GxTaskException {

	public GxSkipTaskException(Exception e) {
		super(e);
	}

	public GxSkipTaskException(String message) {
		super(message);
	}

	public GxSkipTaskException(String message, Exception e) {
		super(message, e);
	}

}
