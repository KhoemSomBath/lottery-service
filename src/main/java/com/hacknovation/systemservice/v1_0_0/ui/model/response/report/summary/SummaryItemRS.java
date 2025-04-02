package com.hacknovation.systemservice.v1_0_0.ui.model.response.report.summary;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

/*
 * author: kangto
 * createdAt: 17/01/2023
 * time: 14:36
 */
@Data
public class SummaryItemRS {

    private BigDecimal totalSale = BigDecimal.ZERO;
    private BigDecimal commission = BigDecimal.ZERO;
    private BigDecimal reward = BigDecimal.ZERO;
    private BigDecimal winAmount = BigDecimal.ZERO;
    private BigDecimal shareRate = BigDecimal.ZERO;

    public BigDecimal getShareAmount(){
        if(shareRate != null)
            return commission.subtract(reward).multiply(shareRate).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        return commission.subtract(reward);
    }

}
