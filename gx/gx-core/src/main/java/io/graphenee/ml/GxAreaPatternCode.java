package io.graphenee.ml;

public enum GxAreaPatternCode {
	UNITEDSTATES("United States of America", "us"),
	BRAZIL("Brazil", "br"),
	EUROPE("Europe", "eu"),
	FRANCE("France", "fr"),
	GREATBRITAIN("Great Britain", "gb"),
	AUSTRALIA("Australia", "au");

	private String countryName;
	private String countryCode;

	private GxAreaPatternCode(String countryName, String countryCode) {
		this.countryName = countryName;
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public String getCountryCode() {
		return countryCode;
	}

}
