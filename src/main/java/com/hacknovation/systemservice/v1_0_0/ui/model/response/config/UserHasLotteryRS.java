package com.hacknovation.systemservice.v1_0_0.ui.model.response.config;

import lombok.Data;

import java.util.List;

/*
 * author: kangto
 * createdAt: 25/01/2022
 * time: 15:25
 */
@Data
public class UserHasLotteryRS {
    private String lotteryType;
    private List<UserLotteryRS> items;
}
