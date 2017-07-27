package org.mowit.model;

import java.io.Serializable;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.mowit.annotation.Positive;
import org.mowit.exception.MowerException;
import org.mowit.sequencer.SequencerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

/**
 * This Class represent a lawn
 * 
 * @author snaceur
 * @version V1.0
 */

@Cacheable("lawns")
public class Lawn implements HasValidator, Serializable {

	private static final long serialVersionUID = 1L;
	private static final String VALIDATION_CHAIN_NOT_FOUND = "VALIDATION_CHAIN_NOT_FOUND";

	@Id
	private int id;

	@Positive(message = "The X margin is invalid.")
	private int marginX;

	@Positive(message = "The Y margin is invalid.")
	private int marginY;
	
	
	protected boolean isGenerated;

	@Transient
	transient ValidatorFactory vf;

	public Lawn(int marginX, int marginY) {
		this.marginX = marginX;
		this.marginY = marginY;
		this.id = SequencerFactory.getSequence(Lawn.class);
		this.vf = Validation.buildDefaultValidatorFactory();
	}

	public int getMarginX() {
		return marginX;
	}

	public int getMarginY() {
		return marginY;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	

	public boolean isGenerated() {
		return isGenerated;
	}

	public void setGenerated(boolean isGenerated) {
		this.isGenerated = isGenerated;
	}

	/**
	 * Validate Lawns
	 */
	@Override
	public Set<ConstraintViolation<HasValidator>> validate() {
		if (vf.getValidator() != null) {
			return vf.getValidator().validate(this);
		} else {
			throw new MowerException(VALIDATION_CHAIN_NOT_FOUND);
		}
	}
}
