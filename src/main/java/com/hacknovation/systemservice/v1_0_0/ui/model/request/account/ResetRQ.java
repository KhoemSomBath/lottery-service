package com.hacknovation.systemservice.v1_0_0.ui.model.request.account;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ResetRQ {

    @NotNull(message = "Please provide a current password")
    private String currentPassword;

    @NotNull(message = "Please provide a new password")
    private String password;

    @NotNull(message = "Please provide a confirmed password")
    private String confirmPassword;
}
