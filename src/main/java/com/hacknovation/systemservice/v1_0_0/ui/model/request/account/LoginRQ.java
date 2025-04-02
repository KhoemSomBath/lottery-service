package com.hacknovation.systemservice.v1_0_0.ui.model.request.account;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class LoginRQ {

    @NotNull(message = "Please provide a username")
    @NotEmpty(message = "Please provide a username")
    private String username;

    @NotNull(message = "Please provide a password")
    @NotEmpty(message = "Please provide a password")
    private String password;

}
