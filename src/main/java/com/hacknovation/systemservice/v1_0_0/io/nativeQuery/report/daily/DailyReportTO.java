package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.report.daily;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DailyReportTO {
    private String lotteryType;
    private String userCode;
    private String agentCode;
    private String masterCode;
    private String seniorCode;
    private String superSeniorCode;
    private String rebateCode;
    private BigDecimal memberCommission;
    private BigDecimal memberRebateRate;
    private BigDecimal betAmountUsd;
    private BigDecimal betAmountKhr;
    private BigDecimal winAmountUsd;
    private BigDecimal winAmountKhr;
}