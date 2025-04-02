package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.report.oldbalance;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Sombath
 * create at 5/11/21 1:24 PM
 */

@Data
public class OldBanhjiTO {

    private BigDecimal oldBalanceKhr = BigDecimal.ZERO;
    private BigDecimal oldBalanceUsd = BigDecimal.ZERO;

}
