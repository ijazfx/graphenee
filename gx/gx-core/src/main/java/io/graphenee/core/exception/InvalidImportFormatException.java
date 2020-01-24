package io.graphenee.core.exception;

@SuppressWarnings("serial")
public class InvalidImportFormatException extends Exception {

	public InvalidImportFormatException() {
		super();
	}

	public InvalidImportFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidImportFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidImportFormatException(String message) {
		super(message);
	}

	public InvalidImportFormatException(Throwable cause) {
		super(cause);
	}

}
