package org.mowit.model;

import java.util.Set;

import javax.validation.ConstraintViolation;

/**
 * 
 * has Validation contract.
 * @author snaceur
 *
 */
public interface HasValidator {

	public Set<ConstraintViolation<HasValidator>> validate();
}
