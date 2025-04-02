package com.hacknovation.systemservice.v1_0_0.ui.model.response.config;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

@Data
public class UserLotteryRS {
    private String originLotteryCode;
    private String  originRebateCode;
    private String  originRebateName;
    private BigDecimal originRebateRate;
    private BigDecimal originWaterRate;
    private BigInteger id;
    private String userCode;
    private String lotteryCode;
    private String rebateCode;
    private BigDecimal rebateRate;
    private BigDecimal waterRate;
    private BigDecimal share;
    private BigDecimal commission;
    private BigDecimal maxBetFirst;
    private BigDecimal maxBetSecond;
    private Integer maxBetSecondAt;
    private Integer limitDigit;
    private Boolean status;
    private Map<String, BigDecimal> rebates;
}
