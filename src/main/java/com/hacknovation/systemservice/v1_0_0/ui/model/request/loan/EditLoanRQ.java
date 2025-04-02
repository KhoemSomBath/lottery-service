package com.hacknovation.systemservice.v1_0_0.ui.model.request.loan;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class EditLoanRQ {

    @NotBlank(message = "Please provide lottery type")
    private String lotteryType;

    private Long id;
    @NotNull
    private BigDecimal amountKhr;
    @NotNull
    private BigDecimal amountUsd;
    @NotBlank
    private String type;
    @NotBlank
    private String userCode;

    @NotBlank
    @NotNull
    private String date;

    private String nextPayment;


}
