package io.graphenee.util.enums;

public enum RequestStatus {
	PENDING("Pending", 0),
	APPROVED("Approved", 1),
	REJECTED("Rejected", 2),
	OBJECTION("Objection", 3);

	private int statusCode;
	private String statusName;

	private RequestStatus(String statusName, int statusCode) {
		this.statusCode = statusCode;
		this.statusName = statusName;
	}

	public int statusCode() {
		return this.statusCode;
	}

	public String statusName() {
		return this.statusName;
	}

	public static String findByStatusCode(int statusCode) {

		switch (statusCode) {
		case 0:
			return RequestStatus.PENDING.statusName;
		case 1:
			return RequestStatus.APPROVED.statusName;
		case 2:
			return RequestStatus.REJECTED.statusName;
		default:
			return null;
		}
	}

	public static RequestStatus findStatusByStatusCode(int statusCode) {
		for (RequestStatus requestStatus : values())
			if (requestStatus.statusCode == statusCode)
				return requestStatus;
		return null;
	}
}