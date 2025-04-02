package com.hacknovation.systemservice.v1_0_0.ui.model.request.probability;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

/*
 * author: kangto
 * createdAt: 14/10/2022
 * time: 13:15
 */
@Data
public class UpdateAllProbabilityRQ {
    @NotEmpty(message = "please provide lottery type")
    private String lotteryType;
    private String proKey;
    private BigDecimal percentage;
}
