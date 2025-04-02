package com.hacknovation.systemservice.v1_0_0.ui.model.response.activityLog;

import lombok.Data;

import java.math.BigDecimal;

/*
 * author: kangto
 * createdAt: 29/12/2021
 * time: 15:26
 */
@Data
public class FinanceRS {

    private String deviceType;
    private String financeType;
    private String userCode;
    private String processBy;
    private BigDecimal currentBalance;
    private BigDecimal newAmount;
    private String currencyCode;

}
