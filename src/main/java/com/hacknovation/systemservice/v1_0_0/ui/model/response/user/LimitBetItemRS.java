package com.hacknovation.systemservice.v1_0_0.ui.model.response.user;

import lombok.Data;

import java.math.BigDecimal;

/*
 * author: kangto
 * createdAt: 31/12/2022
 * time: 15:38
 */
@Data
public class LimitBetItemRS {
    private String lotteryType;
    private BigDecimal limitAmount = BigDecimal.ZERO;

    public LimitBetItemRS(String lottery) {
        this.lotteryType = lottery.toUpperCase();
    }

    public LimitBetItemRS() {
    }
}
