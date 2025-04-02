package com.hacknovation.systemservice.exception.anotation;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone, String> {

    @Autowired
    private HttpServletRequest request;

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {

        int index = request.getServletPath().lastIndexOf('/');
        String seg = request.getServletPath().substring(0,index);
        String lastSeg = seg.substring(seg.lastIndexOf('/') + 1);

        if (NumberUtils.isNumber(phone)) {
            return (phone.length() < 20 && phone.length() > 6);
        }

        if (lastSeg.equalsIgnoreCase("company")) {
            if (!NumberUtils.isNumber(phone) && phone != null) {
                return false;
            }
        }

        return true;

    }

}
