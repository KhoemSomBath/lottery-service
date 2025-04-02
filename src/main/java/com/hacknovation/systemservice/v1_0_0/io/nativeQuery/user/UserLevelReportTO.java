package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user;

import lombok.Data;

@Data
public class UserLevelReportTO {
    private String userCode;
    private String username;
    private String nickname;
    private String roleCode;
    private String superSeniorCode;
    private String seniorCode;
    private String masterCode;
    private String agentCode;
    private String status;
}
