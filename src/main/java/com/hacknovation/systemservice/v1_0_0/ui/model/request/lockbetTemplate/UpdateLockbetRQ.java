package com.hacknovation.systemservice.v1_0_0.ui.model.request.lockbetTemplate;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/*
 * author: kangto
 * createdAt: 17/03/2022
 * time: 13:58
 */
@Data
public class UpdateLockbetRQ {
    private Integer itemId;

    @NotBlank(message = "Please provide lottery type")
    private String lotteryType;

    @NotBlank(message = "Please provide shift code")
    private String shiftCode;

    @NotBlank(message = "Please provide day of week")
    private String dayOfWeek;

    private String userCode;
    private String stopLoAt;
    private String stopAAt;
    private String stopPostAt;
    private String stopDeleteAt;
}
