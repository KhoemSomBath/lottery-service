package com.hacknovation.systemservice.v1_0_0.ui.model.dto;

import lombok.Data;

import java.math.BigInteger;

/*
 * author: kangto
 * createdAt: 19/01/2022
 * time: 17:02
 */
@Data
public class PagingDTO {
    private BigInteger id;
    private String lotteryType;
    private String drawCode;
    private String userCode;
    private Integer pageNumber;
}
