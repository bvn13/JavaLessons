package me.bvn13.test.spring.validation.atleastonegroup.web.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Object passes validation if only one of specified validation group passes.
 */
@Documented
@Constraint(validatedBy = AtLeastOneGroupValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AtLeastOneGroupValidated {
    String message() default "All constrain groups are not valid!";

    Class<?>[] groups() default {};

    /**
     * These groups are for checking validation.
     * If any of specified group passes the validation, then the whole validation passes.
     *
     * @return Validation groups (must be specified)
     */
    Class<?>[] checkingGroups();

    Class<? extends Payload>[] payload() default {};
}
