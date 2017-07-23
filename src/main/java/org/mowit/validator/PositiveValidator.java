package org.mowit.validator;

import org.mowit.annotation.Positive;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * This Class aims to check mandatory values
 * 
 * @author snaceur
 * @version v 1.0
 *
 */
public class PositiveValidator implements ConstraintValidator<Positive, Integer> {

	public boolean isValid(Integer object, ConstraintValidatorContext context) {

		if (object == null || object <= 0) {
			return false;
		} else {
			return true;
		}
	}

}
