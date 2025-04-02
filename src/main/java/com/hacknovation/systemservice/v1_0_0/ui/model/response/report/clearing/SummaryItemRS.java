package com.hacknovation.systemservice.v1_0_0.ui.model.response.report.clearing;

import lombok.Data;

import java.math.BigDecimal;

/*
 * author: kangto
 * createdAt: 05/02/2022
 * time: 16:21
 */
@Data
public class SummaryItemRS {
    private BigDecimal rate;
    private String rateString;
    private BigDecimal amountKhr = BigDecimal.ZERO;
    private BigDecimal subTotalKhr = BigDecimal.ZERO;
    private BigDecimal amountUsd = BigDecimal.ZERO;
    private BigDecimal subTotalUsd = BigDecimal.ZERO;

    public String getRateString() {
        if (rateString != null)
            return rateString.replaceAll("\\.0+$", "");
        return null;
    }
}
