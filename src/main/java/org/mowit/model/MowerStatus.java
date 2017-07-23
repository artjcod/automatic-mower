package org.mowit.model;

/**
 * This enum represent the mower's status
 * @author snaceur
 * @version V1.0
 */

public enum MowerStatus {

	IDLE(0), WORKING(1), NOT_RESPONDING(3), UNKNOWN(4),FINISHED(5);

	private int id;

	private MowerStatus(int id) {

		this.id = id;
	}

	public int getStatus() {
		return id;
	}
}
