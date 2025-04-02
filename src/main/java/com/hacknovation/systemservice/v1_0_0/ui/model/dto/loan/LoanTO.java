package com.hacknovation.systemservice.v1_0_0.ui.model.dto.loan;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Sombath
 * create at 16/11/21 12:03 PM
 */
public interface LoanTO {
    Long getId();
    String getCode();
    String getUsername();
    String getNickname();
    BigDecimal getPaybackKhr();
    BigDecimal getBorrowKhr();
    BigDecimal getPaybackUsd();
    BigDecimal getBorrowUsd();
    BigDecimal getInstallmentPaymentKhr();
    BigDecimal getInstallmentPaymentUsd();
    Date getNextPayment();
}
