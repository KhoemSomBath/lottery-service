package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.userlottery;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class UserHasLotteryTO {
    private String originLotteryCode;
    private String originRebateCode;
    private String originRebateName;
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
    private String maxBetSecondAt;
    private BigDecimal limitDigit;
    private BigDecimal maxBetRange;
    private Integer maxBetItemRange;
    private Boolean status;
}