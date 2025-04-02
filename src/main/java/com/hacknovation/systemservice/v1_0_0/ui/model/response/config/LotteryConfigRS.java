package com.hacknovation.systemservice.v1_0_0.ui.model.response.config;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LotteryConfigRS {
    private String userCode;
    private String lotteryCode;
    private String rebateCode;
    private BigDecimal rebateRate = BigDecimal.ZERO;
    private BigDecimal waterRate = BigDecimal.ZERO;
    private BigDecimal share = BigDecimal.ZERO;
    private BigDecimal commission = BigDecimal.ZERO;
    private BigDecimal minBet = BigDecimal.ZERO;
    private BigDecimal maxBet = BigDecimal.ZERO;
    private Integer limitDigit = 0;
}
