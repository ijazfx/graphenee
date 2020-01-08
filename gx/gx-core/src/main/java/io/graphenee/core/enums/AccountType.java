package io.graphenee.core.enums;

public enum AccountType {

	ASSET("Asset", "AS"),
	EQUITY("Equity", "EQ"),
	EXPENSE("Expense", "EX"),
	INCOME("Income", "IN"),
	LIABILITY("Liability", "LI");

	private String typeCode;
	private String typeName;

	private AccountType(String typeName, String typeCode) {
		this.typeName = typeName;
		this.typeCode = typeCode;
	}

	public String typeName() {
		return this.typeName;
	}

	public String typeCode() {
		return this.typeCode;
	}

}
