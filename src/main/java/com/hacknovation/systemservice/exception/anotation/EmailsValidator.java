package com.hacknovation.systemservice.exception.anotation;

import org.apache.commons.validator.routines.EmailValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailsValidator implements ConstraintValidator<Email, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        boolean b = true;


        if (value == null) {
           return true;
        }
        if (!EmailValidator.getInstance().isValid(value) && !value.isEmpty()) {
            b = false;
        }

        return b;

    }

}
