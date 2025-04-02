package com.hacknovation.systemservice.v1_0_0.ui.model.request.user;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/*
 * author: kangto
 * createdAt: 31/12/2022
 * time: 15:41
 */
@Data
public class UpdateLimitBetRQ {
    @NotNull
    @NotBlank
    private String lotteryType;

    @NotNull
    @NotBlank
    private String userCode;

    @NotNull
    @Min(value = 0)
    private BigDecimal limitAmount;

}
