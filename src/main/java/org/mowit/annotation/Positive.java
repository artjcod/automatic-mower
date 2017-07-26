package org.mowit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.mowit.validator.PositiveValidator;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER ,ElementType.CONSTRUCTOR})
@Constraint(validatedBy = PositiveValidator.class)

/**
 * This annotation is to check positive values
 * @author snaceur
 * @version V1.0
 */
public @interface Positive {
	
	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
	
	String message() default "{The value should be positive.}";
}
