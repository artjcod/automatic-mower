package org.mowit.validator;

import org.mowit.annotation.Mandatory;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * This Class aims to check mandatory values
 * 
 * @author snaceur
 * @version v 1.0
 *
 */
public class MandatoryValidator implements ConstraintValidator<Mandatory, Object> {

	public boolean isValid(Object object, ConstraintValidatorContext context) {

		if (object == null) {
			return false;
		} else {
			return true;
		}
	}

}
