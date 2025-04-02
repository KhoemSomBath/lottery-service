package com.hacknovation.systemservice.v1_0_0.ui.model.response.loan;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Sombath
 * create at 17/11/21 11:59 AM
 */

@Data
public class LoanAmountRS {
    private Long id;
    private BigDecimal amountKhr;
    private BigDecimal amountUsd;

    public LoanAmountRS(Long id, BigDecimal amountKhr, BigDecimal amountUsd) {
        this.id = id;
        this.amountKhr = amountKhr;
        this.amountUsd = amountUsd;
    }
}
