package org.mowit.model;


/**
 * Cardinal represnetation of direction.
 * @author snaceur
 * @version V1.0
 */
public enum Direction {
	NORTH(1), SOUTH(-1), EAST(1), WEST(-1);
	private int move;

	Direction(int move) {
		this.move = move;
	}

	public int getMove() {
		return move;
	}
}
