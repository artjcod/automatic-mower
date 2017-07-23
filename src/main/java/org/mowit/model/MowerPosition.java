package org.mowit.model;

import java.io.Serializable;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.mowit.annotation.Mandatory;
import org.mowit.exception.MowerException;
import org.springframework.data.annotation.Transient;

/**
 * This class represents the position of the Mower by its X & y abscissa .
 * 
 * @author snaceur
 * @version V 1.0
 */

public class MowerPosition implements Serializable, Cloneable, HasValidator {

	private static final long serialVersionUID = 9031817482647793754L;
	private static final String VALIDATION_CHAIN_NOT_FOUND = "VALIDATION_CHAIN_NOT_FOUND";

	@Transient
	transient ValidatorFactory vf;

	private Direction direction;

	private int x, y;

	@Mandatory
	private Lawn lawn;

	public MowerPosition(int x, int y, Direction direction, Lawn lawn) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.lawn = lawn;
		this.vf = Validation.buildDefaultValidatorFactory();
	}

	public Direction getDirection() {
		return direction;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof MowerPosition) {
			MowerPosition pos = (MowerPosition) object;
			return this.x == pos.x && this.y == pos.y && this.direction == (pos.direction);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return x * y * direction.hashCode();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Lawn getLawn() {
		return lawn;
	}

	public void setLawn(Lawn lawn) {
		this.lawn = lawn;
	}
	
	@Override
	public String toString() {
		StringBuilder coordinate = new StringBuilder("(");
		coordinate.append(x).append(",").append(y).append(",").append(direction).append(")");
		return coordinate.toString();
	}

	/**
	 * This method is to validate the constraints
	 */
	@Override
	public Set<ConstraintViolation<HasValidator>> validate() {
		if (vf.getValidator() != null) {
			return vf.getValidator().validate(this);

		} else {
			throw new MowerException(VALIDATION_CHAIN_NOT_FOUND, new String[] {});
		}
	}
}
