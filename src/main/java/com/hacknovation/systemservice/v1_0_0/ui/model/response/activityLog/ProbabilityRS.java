package com.hacknovation.systemservice.v1_0_0.ui.model.response.activityLog;

import lombok.Data;

import java.math.BigDecimal;

/*
 * author: kangto
 * createdAt: 29/12/2021
 * time: 16:00
 */
@Data
public class ProbabilityRS {
    private String gameType;
    private Boolean isJackpot;
    private String isProbability;
    private BigDecimal settlementValue;
    private Boolean isSettlement;
}
