package com.hacknovation.systemservice.v1_0_0.ui.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/*
 * author: kangto
 * createdAt: 19/02/2022
 * time: 11:01
 */
public interface AnalyzeTO {
    String getPostGroup();
    String getPostCode();
    String getRebateCode();
    String getNumber();
    BigDecimal getRewardAmount();
    BigDecimal getBetAmount();
    BigDecimal getWaterAmount();
}
