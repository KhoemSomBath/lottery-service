package com.hacknovation.systemservice.v1_0_0.ui.model.request.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CloneUserRQ {

    @NotBlank(message = "Please provide a clone from user code")
    private String cloneFromUserCode;

    @NotBlank(message = "Please provide a under user code")
    private String underUserCode;

    @NotBlank(message = "Please provide a username")
    private String username;

    @NotBlank(message = "Please provide a nickname")
    private String nickname;

    @NotBlank(message = "Please provide a password")
    private String password;

    @NotBlank(message = "Please provide a confirm password")
    private String confirmPassword;

}