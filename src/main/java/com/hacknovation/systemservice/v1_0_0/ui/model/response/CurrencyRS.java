package com.hacknovation.systemservice.v1_0_0.ui.model.response;

import lombok.Data;

import java.math.BigDecimal;

/*
 * author: kangto
 * createdAt: 03/02/2022
 * time: 21:37
 */
@Data
public class CurrencyRS {
    private BigDecimal amountKhr = BigDecimal.ZERO;
    private BigDecimal amountUsd = BigDecimal.ZERO;

    public void addFromCurrencyRS(CurrencyRS currencyRS) {
        this.amountKhr =  this.amountKhr.add(currencyRS.getAmountKhr());
        this.amountUsd = this.amountUsd.add(currencyRS.getAmountUsd());
    }
}
