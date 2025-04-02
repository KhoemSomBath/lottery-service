package com.hacknovation.systemservice.v1_0_0.ui.model.response.report.ticket;

import lombok.Data;

/*
 * author: kangto
 * createdAt: 11/01/2022
 * time: 10:36
 */
@Data
public class SummaryAmountRS {
    private AmountRS totalKhr  = new AmountRS();
    private AmountRS totalUsd = new AmountRS();

    public void addFromSummaryAmountRS(SummaryAmountRS amountRS) {
        this.totalKhr.addFromAmountRS(amountRS.getTotalKhr());
        this.totalUsd.addFromAmountRS(amountRS.getTotalUsd());
    }
}
