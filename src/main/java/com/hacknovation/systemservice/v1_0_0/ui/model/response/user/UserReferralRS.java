package com.hacknovation.systemservice.v1_0_0.ui.model.response.user;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserReferralRS {
    private String key;
    private String nickName;
    private String label;
    private String roleName;
    private String roleCode;
    private List<UserReferralRS> nodes = new ArrayList<>();
}
