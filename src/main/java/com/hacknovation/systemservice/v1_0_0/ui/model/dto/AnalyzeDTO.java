package com.hacknovation.systemservice.v1_0_0.ui.model.dto;

import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

/*
 * author: kangto
 * createdAt: 19/02/2022
 * time: 11:01
 */
@Data
public class AnalyzeDTO {
    private String postGroup;
    private String postCode;
    private String rebateCode;
    private String number;
    private BigDecimal rewardAmount;
    private BigDecimal betAmount = BigDecimal.ZERO;
    private BigDecimal waterAmount = BigDecimal.ZERO;

    public AnalyzeDTO(AnalyzeTO analyzeTO) {
        BeanUtils.copyProperties(analyzeTO, this);
    }

    public AnalyzeDTO() {
    }

}
