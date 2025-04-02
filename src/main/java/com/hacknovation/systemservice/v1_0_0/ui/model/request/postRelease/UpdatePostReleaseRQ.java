package com.hacknovation.systemservice.v1_0_0.ui.model.request.postRelease;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/*
 * author: kangto
 * createdAt: 30/08/2022
 * time: 11:53
 */
@Data
public class UpdatePostReleaseRQ {
    @NotBlank
    private String lotteryType;
    @NotBlank
    private String postCode;
    @NotNull
    private Boolean isCanRelease;
}
