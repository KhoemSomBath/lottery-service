package com.hacknovation.systemservice.v1_0_0.ui.model.request.result;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/*
 * author: kangto
 * createdAt: 22/01/2022
 * time: 12:39
 */
@Data
public class PublishResultRQ {
    @NotBlank(message = "Please provide lottery type")
    private String lotteryType;
    @NotBlank(message = "Please provide a draw code")
    private String drawCode;

}
