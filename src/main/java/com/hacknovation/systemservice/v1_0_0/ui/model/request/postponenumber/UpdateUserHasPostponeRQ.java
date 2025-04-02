package com.hacknovation.systemservice.v1_0_0.ui.model.request.postponenumber;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/*
 * author: kangto
 * createdAt: 21/01/2022
 * time: 18:38
 */
@Data
public class UpdateUserHasPostponeRQ {
    @NotBlank(message = "Please provide lottery type")
    private String lotteryType;
    @NotBlank(message = "Please provide draw code")
    private String drawCode;
    @NotBlank(message = "Please provide user code")
    private String userCode;
    private String numberDetail;
    @Min(value = 0)
    @Max(value = 99999999)
    private BigDecimal limitAmount;

    private Boolean isAllMember = false;
    private Boolean isDefault = false;

    public BigDecimal getLimitAmount() {
        if (limitAmount == null)
            return BigDecimal.ZERO;
        return limitAmount;
    }

}
