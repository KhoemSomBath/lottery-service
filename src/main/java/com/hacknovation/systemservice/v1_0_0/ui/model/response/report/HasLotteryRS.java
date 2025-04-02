package com.hacknovation.systemservice.v1_0_0.ui.model.response.report;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class HasLotteryRS {
    private BigDecimal rebateRate1D = BigDecimal.ZERO;
    private BigDecimal rebateRate2D = BigDecimal.ZERO;
    private BigDecimal rebateRate3D = BigDecimal.ZERO;
    private BigDecimal rebateRate4D = BigDecimal.ZERO;
    private BigDecimal com1D = BigDecimal.ZERO;
    private BigDecimal com2D = BigDecimal.ZERO;
    private BigDecimal com3D = BigDecimal.ZERO;
    private BigDecimal com4D = BigDecimal.ZERO;
    private BigDecimal share1D = BigDecimal.ZERO;
    private BigDecimal share2D = BigDecimal.ZERO;
    private BigDecimal share3D = BigDecimal.ZERO;
    private BigDecimal share4D = BigDecimal.ZERO;
}