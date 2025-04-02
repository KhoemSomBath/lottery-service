package com.hacknovation.systemservice.v1_0_0.ui.model.request.resultInput;

import lombok.Data;

import javax.validation.constraints.NotNull;

/*
 * author: kangto
 * createdAt: 29/10/2022
 * time: 15:24
 */
@Data
public class RenderNumberRQ {

    @NotNull
    private String lotteryType;

    @NotNull
    private String postCode;

    @NotNull
    private String rebateCode;

    @NotNull
    private Integer columnNumber;
}
