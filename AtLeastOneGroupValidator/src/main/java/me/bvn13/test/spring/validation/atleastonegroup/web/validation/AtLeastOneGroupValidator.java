package me.bvn13.test.spring.validation.atleastonegroup.web.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class AtLeastOneGroupValidator implements ConstraintValidator<AtLeastOneGroupValidated, Object> {

    private Class<?>[] groups;

    @Override
    public void initialize(AtLeastOneGroupValidated constraintAnnotation) {
        groups = constraintAnnotation.checkingGroups();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        final Validator validator;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
        for (Class<?> group : groups) {
            final Set<ConstraintViolation<Object>> violation = validator.validate(value, group);
            if (violation.isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
