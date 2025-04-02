package com.hacknovation.systemservice.v1_0_0.ui.model.request.draw;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/*
 * author: kangto
 * createdAt: 21/01/2022
 * time: 22:22
 */
@Data
public class DrawingPostponeRQ {
    private String lotteryType;
    @NotEmpty(message = "Please provide a draw code")
    private String drawCode;
    @NotEmpty(message = "Please provide a postpone number")
    private String postponeNumber;
}
