package com.hacknovation.systemservice.v1_0_0.ui.model.request.probability;

import com.hacknovation.systemservice.exception.anotation.Include;
import lombok.Data;

import javax.validation.constraints.NotNull;

/*
 * author: kangto
 * createdAt: 25/08/2022
 * time: 23:03
 */
@Data
public class UpdateProbabilityRuleRQ {

    @Include(contains = "LEAP,VN2,TN,SC", delimiter = ",")
    private String lotteryType;

    @NotNull
    private Boolean isPercentageAllDraws;

}
