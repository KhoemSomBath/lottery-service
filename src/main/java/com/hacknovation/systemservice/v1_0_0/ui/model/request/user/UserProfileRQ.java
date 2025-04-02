package com.hacknovation.systemservice.v1_0_0.ui.model.request.user;

import lombok.Data;

@Data
public class UserProfileRQ {
    private String username;
    private String nickname;
    private String languageCode;
    private String currencyCode;
    private String userType;
}
