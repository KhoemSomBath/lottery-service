package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserTO {
    private String userCode;
    private String username;
    private String nickname;
    private String roleCode;
    private String superSeniorCode;
    private String seniorCode;
    private String masterCode;
    private String agentCode;
    private String currencyCode;
    private String languageCode;
    private Integer parentId;
    private BigDecimal balanceKhr = BigDecimal.ZERO;
    private BigDecimal balanceUsd = BigDecimal.ZERO;
    private String status;
    private String type;
    private String platformType;
    private String lotteryType;
    private Boolean isLockedScreen;
    private Boolean isOnline;
    private Date lastLogin;
    private Date lastLoginWeb;
    private Date lastLoginApp;
    private Date createdAt;
}
