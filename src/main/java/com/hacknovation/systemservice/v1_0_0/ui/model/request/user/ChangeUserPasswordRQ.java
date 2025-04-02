package com.hacknovation.systemservice.v1_0_0.ui.model.request.user;

import com.hacknovation.systemservice.exception.anotation.FieldMatch;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@FieldMatch(first = "password", second = "confirmPassword")
public class ChangeUserPasswordRQ {

    @NotEmpty(message = "Please provide a password")
    private String password;

    @NotEmpty(message = "Please provide a confirm password")
    private String confirmPassword;

}
