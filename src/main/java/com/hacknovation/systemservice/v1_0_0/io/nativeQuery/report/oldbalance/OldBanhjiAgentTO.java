package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.report.oldbalance;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Sombath
 * create at 5/11/21 1:24 PM
 */

@Data
public class OldBanhjiAgentTO {

    private BigDecimal oldBalanceKhr = BigDecimal.ZERO;
    private BigDecimal oldBalanceUsd = BigDecimal.ZERO;
    private BigDecimal underProtestKhr = BigDecimal.ZERO;
    private BigDecimal underProtestUsd = BigDecimal.ZERO;
    private BigDecimal upperProtestKhr = BigDecimal.ZERO;
    private BigDecimal upperProtestUsd = BigDecimal.ZERO;
    private BigDecimal giveKhr = BigDecimal.ZERO;
    private BigDecimal giveUsd = BigDecimal.ZERO;
    private BigDecimal borrowKhr = BigDecimal.ZERO;
    private BigDecimal borrowUsd = BigDecimal.ZERO;

    private BigDecimal leapKhr = BigDecimal.ZERO;
    private BigDecimal leapUsd = BigDecimal.ZERO;
    private BigDecimal mtKhr = BigDecimal.ZERO;
    private BigDecimal mtUsd = BigDecimal.ZERO;
    private BigDecimal vn1Khr = BigDecimal.ZERO;
    private BigDecimal vn1Usd = BigDecimal.ZERO;
    private BigDecimal tnKhr = BigDecimal.ZERO;
    private BigDecimal tnUsd = BigDecimal.ZERO;
    private BigDecimal khKhr = BigDecimal.ZERO;
    private BigDecimal khUsd = BigDecimal.ZERO;

}
