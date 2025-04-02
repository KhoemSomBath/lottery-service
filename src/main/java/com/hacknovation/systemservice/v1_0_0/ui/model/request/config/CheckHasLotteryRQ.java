package com.hacknovation.systemservice.v1_0_0.ui.model.request.config;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CheckHasLotteryRQ {
    @NotEmpty(message = "Please provide a user code")
    private String userCode;
    @NotEmpty(message = "Please provide a lottery code")
    private String lotteryCode;
    @NotEmpty(message = "Please provide a rebate code")
    private String rebateCode;
}
