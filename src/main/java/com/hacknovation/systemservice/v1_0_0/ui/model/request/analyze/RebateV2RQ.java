package com.hacknovation.systemservice.v1_0_0.ui.model.request.analyze;

import lombok.Data;

import java.math.BigDecimal;

/*
 * author: kangto
 * createdAt: 19/02/2022
 * time: 11:26
 */
@Data
public class RebateV2RQ {
    private String postCode;
    private BigDecimal percentage = BigDecimal.valueOf(10);
}
