package com.hacknovation.systemservice.v1_0_0.ui.model.response.user;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserRS {
    private String userCode;
    private String username;
    private String nickname;
    private String roleCode;
    private String currencyCode;
    private String languageCode;
    private Integer parentId;
    private BigDecimal balance = BigDecimal.ZERO;
    private String status;
    private String type;
    private String platformType;
    private String lotteryType;
    private Boolean isLockedScreen;
    private Boolean lastLogin;
    private Date lastLoginWeb;
    private Date lastLoginApp;
    private Date createdAt;
}
