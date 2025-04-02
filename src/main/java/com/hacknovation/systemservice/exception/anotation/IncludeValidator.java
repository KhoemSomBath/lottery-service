package com.hacknovation.systemservice.exception.anotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class IncludeValidator implements ConstraintValidator<Include, Object> {

    private String contains;
    private String delimiter;
    private String message;

    @Override
    public void initialize(Include constraintAnnotation) {
        this.contains = constraintAnnotation.contains();
        this.delimiter = constraintAnnotation.delimiter();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        if (value == null) {
            return true;
        }

        boolean valid = false;
        String input = value.toString();
        String[] conditions = contains.split(this.delimiter);

        for (String item : conditions) {
            if (item.equalsIgnoreCase(input)) {
                valid = true;
                break;
            }
        }

        if (!valid) {

            String condition = input;
            int size = conditions.length;
            if (size > 1) {
                condition = String.join(", ", Arrays.copyOf(conditions, size - 1)) + " or " + conditions[size - 1];
            }

            message = "The input value must be " + condition + ".";
            context
                    .buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }

        return valid;
    }
}
