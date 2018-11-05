package io.graphenee.core.enums;

public enum AccessTypeStatus {
	ACCESS(0),
	CHECKIN(1),
	CHECKOUT(2);

	private Integer statusCode;

	private AccessTypeStatus(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public Integer statusCode() {
		return this.statusCode;
	}
}
