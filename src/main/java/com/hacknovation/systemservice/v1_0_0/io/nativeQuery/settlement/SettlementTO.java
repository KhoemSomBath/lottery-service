package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.settlement;

import lombok.Data;

import java.math.BigDecimal;

/*
 * author: kangto
 * createdAt: 03/02/2022
 * time: 21:51
 */
@Data
public class SettlementTO {
    private String lotteryType;
    private String userCode;
    private String type;
    private BigDecimal amountKhr = BigDecimal.ZERO;
    private BigDecimal amountUsd = BigDecimal.ZERO;
}
