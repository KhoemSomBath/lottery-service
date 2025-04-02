package com.hacknovation.systemservice.v1_0_0.utility.auth;

import lombok.Data;

import java.util.List;

@Data
public class UserToken {
    private Long id;
    private String username;
    private String nickName;
    private String userCode;
    private String userRole;
    private String currency;
    private String userType;
    private String superSeniorCode;
    private String seniorCode;
    private String masterCode;
    private String agentCode;
    private Integer parentId;
    private String parentUsername;
    private String parentCode;
    private String parentRole;
    private String parentNickname;
    private List<String> permissions;
}
