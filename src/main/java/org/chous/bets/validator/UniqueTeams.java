package org.chous.bets.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TeamsValidator.class)
@Documented
public @interface UniqueTeams {

    String message() default "Teams must be unique";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
