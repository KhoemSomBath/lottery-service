package com.hacknovation.systemservice.v3_0_0.servive.betting.respond;

import lombok.Data;

@Data
public class OrderSummaryRS {
    private AmountRS grandTotalUsd = new AmountRS();
    private AmountRS grandTotalKhr = new AmountRS();
}
