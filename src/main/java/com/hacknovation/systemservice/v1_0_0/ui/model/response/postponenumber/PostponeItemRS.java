package com.hacknovation.systemservice.v1_0_0.ui.model.response.postponenumber;

import lombok.Data;

import java.math.BigDecimal;

/*
 * author: kangto
 * createdAt: 21/02/2022
 * time: 23:50
 */
@Data
public class PostponeItemRS {
    private Integer itemId;
    private String number;
    private BigDecimal limitAmount;
    private Boolean isAllMember;
}
