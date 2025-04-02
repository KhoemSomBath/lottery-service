package com.hacknovation.systemservice.v1_0_0.ui.model.response.user;

import com.hacknovation.systemservice.v1_0_0.ui.model.response.config.LotteryRS;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/*
 * author: kangto
 * createdAt: 11/02/2022
 * time: 15:56
 */
@Data
public class CommissionConfigRS {
    private String username;
    private String nickname;
    private String roleCode;
    private String creator;
    List<LotteryRS> lotteries = new ArrayList<>();
    private List<CommissionLotteryRS> items;
}
