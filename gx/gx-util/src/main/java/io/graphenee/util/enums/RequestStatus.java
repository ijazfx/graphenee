package io.graphenee.util.enums;

/**
 * An enum that represents the status of a request.
 */
public enum RequestStatus {
	/**
	 * The request is pending.
	 */
	PENDING("Pending", 0),
	/**
	 * The request is approved.
	 */
	APPROVED("Approved", 1),
	/**
	 * The request is rejected.
	 */
	REJECTED("Rejected", 2),
	/**
	 * The request has an objection.
	 */
	OBJECTION("Objection", 3);

	private int statusCode;
	private String statusName;

	private RequestStatus(String statusName, int statusCode) {
		this.statusCode = statusCode;
		this.statusName = statusName;
	}

	/**
	 * Gets the status code.
	 * @return The status code.
	 */
	public int statusCode() {
		return this.statusCode;
	}

	/**
	 * Gets the status name.
	 * @return The status name.
	 */
	public String statusName() {
		return this.statusName;
	}

	/**
	 * Finds a status by status code.
	 * @param statusCode The status code.
	 * @return The status name.
	 */
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

	/**
	 * Finds a status by status code.
	 * @param statusCode The status code.
	 * @return The status.
	 */
	public static RequestStatus findStatusByStatusCode(int statusCode) {
		for (RequestStatus requestStatus : values())
			if (requestStatus.statusCode == statusCode)
				return requestStatus;
		return null;
	}
}