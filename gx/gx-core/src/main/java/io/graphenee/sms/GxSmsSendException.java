package io.graphenee.sms;

@SuppressWarnings("serial")
public class GxSmsSendException extends Exception {
	private Integer code;

	public GxSmsSendException(Exception e) {
		super(e);
	}

	public GxSmsSendException(String message) {
		super(message);
	}

	public GxSmsSendException(String message, Integer code) {
		super(message);
		this.setCode(code);
	}

	public GxSmsSendException(String message, Exception e) {
		super(message, e);
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}
