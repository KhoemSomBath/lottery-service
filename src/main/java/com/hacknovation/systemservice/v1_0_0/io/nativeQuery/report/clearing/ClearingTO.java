package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.report.clearing;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/*
 * author: kangto
 * createdAt: 05/02/2022
 * time: 14:30
 */
@Data
public class ClearingTO {
    private String lotteryType;
    private String userCode;
    private Integer pageNumber;
    private String drawCode;
    private Date drawAt;
    private String rebateCode;
    private BigDecimal betAmountKhr;
    private BigDecimal betAmountUsd;
    private BigDecimal commission;
    private BigDecimal rebateRate;
    private BigDecimal winAmountKhr;
    private BigDecimal winAmountUsd;
}
