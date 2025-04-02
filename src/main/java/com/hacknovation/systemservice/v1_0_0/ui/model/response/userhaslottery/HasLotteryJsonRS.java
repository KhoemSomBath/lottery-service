package com.hacknovation.systemservice.v1_0_0.ui.model.response.userhaslottery;

import lombok.Data;

/*
 * author: kangto
 * createdAt: 28/03/2022
 * time: 15:46
 */
@Data
public class HasLotteryJsonRS {
    private RateRS oneD = new RateRS();
    private RateRS twoD = new RateRS();
    private RateRS threeD = new RateRS();
    private RateRS fourD = new RateRS();
}
