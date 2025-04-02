package com.hacknovation.systemservice.v1_0_0.ui.model.response.account;

import lombok.Data;

@Data
public class UserDetailRS {
    UserRS user;
    private String token;
}
