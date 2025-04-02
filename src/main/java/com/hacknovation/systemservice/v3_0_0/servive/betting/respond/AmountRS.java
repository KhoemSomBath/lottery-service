package com.hacknovation.systemservice.v3_0_0.servive.betting.respond;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AmountRS {
    private BigDecimal betAmount = BigDecimal.ZERO;
    private BigDecimal waterAmount = BigDecimal.ZERO;
    private BigDecimal winAmount = BigDecimal.ZERO;
    private BigDecimal winLoseAmount = BigDecimal.ZERO;
}