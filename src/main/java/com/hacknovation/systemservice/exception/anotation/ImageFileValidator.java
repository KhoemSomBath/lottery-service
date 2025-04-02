package com.hacknovation.systemservice.exception.anotation;

/*
 * @author KHOEM Sombath
 * Date: 6/8/2021
 * Time: 3:39 PM
 */

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImageFileValidator implements ConstraintValidator<ValidImage, MultipartFile> {

    @Override
    public void initialize(ValidImage constraintAnnotation) {

    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
        boolean result = true;

        if(multipartFile == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Image is required.")
                    .addConstraintViolation();
            return false;
        }
        String contentType = multipartFile.getContentType();
        assert contentType != null;
        if (!isSupportedContentType(contentType)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Only PNG or JPG images are allowed.")
                    .addConstraintViolation();

            result = false;
        }

        return result;
    }

    private boolean isSupportedContentType(String contentType) {
        return contentType.equalsIgnoreCase("image/*")
                || contentType.equalsIgnoreCase("image/png")
                || contentType.equalsIgnoreCase("image/jpg")
                || contentType.equalsIgnoreCase("image/jpeg");
    }
}