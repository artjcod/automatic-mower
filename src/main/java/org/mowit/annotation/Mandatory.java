package org.mowit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.mowit.validator.MandatoryValidator;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER ,ElementType.CONSTRUCTOR})
@Constraint(validatedBy = MandatoryValidator.class)

/**
 * This annotation is for mandatory fields check
 * @author snaceur
 * @version V1.0
 */
public @interface Mandatory {
	
	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
	
	String message() default "{The value is mandatory but null value found.}";
}
