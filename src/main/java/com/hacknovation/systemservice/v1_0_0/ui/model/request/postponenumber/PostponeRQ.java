package com.hacknovation.systemservice.v1_0_0.ui.model.request.postponenumber;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/*
 * author: kangto
 * createdAt: 21/01/2022
 * time: 18:38
 */
@Data
public class PostponeRQ {
    @NotBlank(message = "Please provide lottery type")
    private String lotteryType;
    private String numberDetail;
    private Boolean status = Boolean.TRUE;
}
