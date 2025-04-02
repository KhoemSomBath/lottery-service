package com.hacknovation.systemservice.v1_0_0.ui.model.response.settlement;

import lombok.Data;
import lombok.extern.java.Log;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class SettlementSummaryRS {
    private Long itemId;
    private BigDecimal amountKhr = BigDecimal.ZERO;
    private BigDecimal amountUsd = BigDecimal.ZERO;

    public void addFromSettlementSummaryRS(SettlementSummaryRS summaryRS) {
        amountKhr = amountKhr.add(summaryRS.getAmountKhr());
        amountUsd = amountUsd.add(summaryRS.getAmountUsd());
    }
}