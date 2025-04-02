package com.hacknovation.systemservice.v1_0_0.ui.model.request.account;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LogoutRQ {

    @NotNull(message = "Please provide a token")
    private String deviceToken;

}
