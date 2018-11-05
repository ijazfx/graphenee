package io.graphenee.core.enums;

public enum AccessKeyType {
	RETINASCAN(0),
	FINGERPRINT(1),
	CARD(2);

	private Integer typeCode;

	private AccessKeyType(Integer typeCode) {
		this.typeCode = typeCode;
	}

	public Integer typeCode() {
		return this.typeCode;
	}

	public static AccessKeyType accessKeyType(Integer typeCode) {
		if (typeCode == 0)
			return AccessKeyType.RETINASCAN;
		if (typeCode == 1)
			return AccessKeyType.FINGERPRINT;
		if (typeCode == 2)
			return AccessKeyType.CARD;
		return null;
	}

}
