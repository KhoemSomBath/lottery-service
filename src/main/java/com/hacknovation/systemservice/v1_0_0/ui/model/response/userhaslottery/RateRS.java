package com.hacknovation.systemservice.v1_0_0.ui.model.response.userhaslottery;

import lombok.Data;

import java.math.BigDecimal;

/*
 * author: kangto
 * createdAt: 28/03/2022
 * time: 15:45
 */
@Data
public class RateRS {
    private BigDecimal commission = BigDecimal.ZERO;
    private BigDecimal rebateRate = BigDecimal.ZERO;
    private BigDecimal maxBetFirst = BigDecimal.ZERO;
    private BigDecimal maxBetSecond = BigDecimal.ZERO;
    private BigDecimal limitDigit = BigDecimal.ZERO;
    private BigDecimal share = BigDecimal.ZERO;
    private BigDecimal waterRate = BigDecimal.ZERO;
    private String maxBetSecondMin;
}
