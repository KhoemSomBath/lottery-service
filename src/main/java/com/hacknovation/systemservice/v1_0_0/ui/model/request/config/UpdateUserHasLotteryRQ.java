package com.hacknovation.systemservice.v1_0_0.ui.model.request.config;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class UpdateUserHasLotteryRQ {
    private BigInteger id;
    @NotEmpty(message = "Please provide a user code")
    private String userCode;
    @NotEmpty(message = "Please provide a lottery code")
    private String lotteryCode;
    @NotEmpty(message = "Please provide a rebate code")
    private String rebateCode;
    private BigDecimal rebateRate;
    private BigDecimal waterRate = BigDecimal.valueOf(100);
    private BigDecimal share = BigDecimal.ZERO;
    private BigDecimal commission;
    private BigDecimal maxBetFirst;
    private BigDecimal maxBetSecond;
    private BigDecimal limitDigit;
    private String maxBetSecondAt;
    private BigDecimal maxBetRange;
    private Integer maxBetItemRange;
    private Boolean status;
}
