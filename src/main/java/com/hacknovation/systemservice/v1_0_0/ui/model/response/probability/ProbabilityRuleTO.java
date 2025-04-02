package com.hacknovation.systemservice.v1_0_0.ui.model.response.probability;

import java.util.Date;

/*
 * author: kangto
 * createdAt: 29/08/2022
 * time: 15:38
 */
public interface ProbabilityRuleTO {

    String getLotteryType();
    Boolean getIsPercentageAllDraws();
    String getUpdatedBy();
    Date getUpdatedAt();

}
