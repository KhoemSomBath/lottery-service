package com.hacknovation.systemservice.v1_0_0.ui.model.dto.hasLotteryTemplate;

import lombok.Data;

import java.math.BigDecimal;

/*
 * author: kangto
 * createdAt: 30/11/2022
 * time: 14:34
 */
public interface HasLotteryTemplateDTO {

    String getDayOfWeek();
    String getLotteryType();
    String getUserCode();
    String getUsername();
    Boolean getStatus();
    String getPostCode();
    String getRebateCode();
    BigDecimal getLimitDigit();
    String getUpdatedBy();

}
