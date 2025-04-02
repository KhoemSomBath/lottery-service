package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.summaryDaily;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/*
 * author: kangto
 * createdAt: 03/02/2022
 * time: 21:51
 */
@Data
public class SummaryDailyTO {
    private String lotteryType;
    private Date issuedAt;
    private String userCode;
    private BigDecimal amountKhr = BigDecimal.ZERO;
    private BigDecimal amountUsd = BigDecimal.ZERO;
}
