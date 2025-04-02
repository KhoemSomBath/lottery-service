package com.hacknovation.systemservice.exception.anotation;

import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldMatchRequireValidator implements ConstraintValidator<FieldMatchRequire, Object> {


    private String firstFieldName;
    private String secondFieldName;
    private String thirdFieldName;
    private String message;

    @Override
    public void initialize(final FieldMatchRequire constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
        thirdFieldName = constraintAnnotation.third();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {

        boolean valid = true;

        try
        {
            final String firstObj = BeanUtils.getProperty(value, firstFieldName);
            final Object secondObj = BeanUtils.getProperty(value, secondFieldName);
            final Object thirdObj = BeanUtils.getProperty(value, thirdFieldName);

        }

        catch (final Exception ignore)
        {
            // ignore
        }

        if (!valid){
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(firstFieldName)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }

        return valid;

    }

}
