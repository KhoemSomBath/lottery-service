package com.hacknovation.systemservice.v1_0_0.ui.model.response.userhaslottery;

import lombok.Data;

/*
 * author: kangto
 * createdAt: 28/03/2022
 * time: 15:46
 */
@Data
public class UserHasLotteryRS {
    private HasLotteryJsonRS member = new HasLotteryJsonRS();
    private HasLotteryJsonRS agent = new HasLotteryJsonRS();
    private HasLotteryJsonRS master = new HasLotteryJsonRS();
    private HasLotteryJsonRS senior = new HasLotteryJsonRS();
    private HasLotteryJsonRS superSenior = new HasLotteryJsonRS();

}
