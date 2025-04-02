package com.hacknovation.systemservice.v1_0_0.ui.model.response.loan;

import com.hacknovation.systemservice.v1_0_0.ui.model.dto.loan.LoanTO;
import lombok.Data;

import java.util.Date;

/**
 * @author Sombath
 * create at 17/11/21 11:56 AM
 */

@Data
public class LoanRS {
    private Long id;
    private String username;
    private String nickname;
    private String userCode;
    private Date nextPayment;
    private LoanAmountRS borrow;
    private LoanAmountRS payback;
    private LoanAmountRS installmentPayment;

    public LoanRS(LoanTO item) {
        this.id = item.getId();
        this.username = item.getUsername();
        this.nickname = item.getNickname();
        this.userCode = item.getCode();
        this.nextPayment = item.getNextPayment();
        this.borrow = new LoanAmountRS(item.getId(), item.getBorrowKhr(), item.getBorrowUsd());
        this.payback = new LoanAmountRS(item.getId(), item.getPaybackKhr(), item.getPaybackUsd());
        this.installmentPayment = new LoanAmountRS(item.getId(), item.getInstallmentPaymentKhr(), item.getInstallmentPaymentUsd());
    }
}
