package com.hacknovation.systemservice.v1_0_0.ui.model.response.account;

import com.hacknovation.systemservice.enums.UserStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserRS {
    private String roleCode;
    private String username;
    private String parentUsername;
    private Long parentId;
    private Boolean isSubAccount = Boolean.FALSE;
    private String userCode;
    private String nickname;
    private BigDecimal balanceKhr = BigDecimal.ZERO;
    private BigDecimal balanceUsd = BigDecimal.ZERO;
    private String languageCode;
    private String currencyCode;
    private String countryCode;
    private String lotteryType;
    private String platformType;
    private Date lastLogin;
    private Date createdAt;
    private UserStatusEnum status;
    private String systemVersion;
}