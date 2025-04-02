package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user;

import lombok.Data;

@Data
public class UserReferralTO {
    private String code;
    private String username;
    private String nickname;
    private String roleCode;
    private String roleName;
    private String superSeniorCode;
    private String seniorCode;
    private String masterCode;
    private String agentCode;
}