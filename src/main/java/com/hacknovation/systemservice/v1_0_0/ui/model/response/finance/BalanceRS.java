package com.hacknovation.systemservice.v1_0_0.ui.model.response.finance;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceRS {
    private String userCode;
    private BigDecimal balanceAmount = BigDecimal.ZERO;
    private String currency = "KHR";
    private String currencySignal = "៛";
}