package io.graphenee.core.enums;

public enum GenderEnum {
	Male("M"),
	Female("F"),
	Undisclosed("X");

	private String genderCode;

	private GenderEnum(String genderCode) {
		this.genderCode = genderCode;
	}

	public String getGenderCode() {
		return genderCode;
	}

}