package com.hacknovation.systemservice.v1_0_0.ui.model.response.analysev2;

import com.hacknovation.systemservice.v1_0_0.ui.model.dto.AnalyzeDTO;
import lombok.Data;

import java.math.BigDecimal;

/*
 * author: kangto
 * createdAt: 06/04/2023
 * time: 16:01
 */
@Data
public class AnalyseItemV2RS {
    private String postCode;
    private String number;
    private BigDecimal totalReward = BigDecimal.ZERO;
    private BigDecimal betAmount = BigDecimal.ZERO;
    private BigDecimal commissionAmount = BigDecimal.ZERO;
    private BigDecimal percentage = BigDecimal.ZERO;

    public void addAmountBetAndWaterAndReward(AnalyzeDTO analyzeTO) {
        betAmount = betAmount.add(analyzeTO.getBetAmount());
        commissionAmount = commissionAmount.add(analyzeTO.getWaterAmount());
        totalReward = totalReward.add(analyzeTO.getRewardAmount());
    }
}
