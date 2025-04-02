package com.hacknovation.systemservice.v1_0_0.ui.model.dto.user;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Sombath
 * create at 28/1/22 5:21 PM
 */
public interface UserDTO {
     String getUserCode();
     String getUsername();
     String getNickname();
     String getRoleCode();
     String getRoleName();
     String getStatus();
     Boolean getIsLocked();
     Date getCreatedAt();
     String getPlatformType();
     String getLotteryType();
     Date getLastLogin();
     BigDecimal getBalanceKhr();
     BigDecimal getBalanceUsd();
     BigDecimal getDepositKhr();
     BigDecimal getWithdrawKhr();
     BigDecimal getDepositUsd();
     BigDecimal getWithdrawUsd();
}
