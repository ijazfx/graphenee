package io.graphenee.core.enums;

public enum AccountType {

	ASSET("AS"),
	EQUITY("EQ"),
	EXPENSE("EX"),
	INCOME("IN"),
	LIABILITY("LI");

	private String typeCode;

	private AccountType(String typeCode) {
		this.typeCode = typeCode;
	}

	public String typeCode() {
		return this.typeCode;
	}

}
