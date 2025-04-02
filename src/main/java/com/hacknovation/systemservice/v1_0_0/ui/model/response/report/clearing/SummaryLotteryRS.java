package com.hacknovation.systemservice.v1_0_0.ui.model.response.report.clearing;

import com.hacknovation.systemservice.v1_0_0.ui.model.response.CurrencyRS;
import lombok.Data;

/*
 * author: kangto
 * createdAt: 05/02/2022
 * time: 16:23
 */
@Data
public class SummaryLotteryRS {

    private SummaryItemRS commissionTwoD = new SummaryItemRS();
    private SummaryItemRS commissionThreeD = new SummaryItemRS();
    private SummaryItemRS commissionFourD = new SummaryItemRS();

    private SummaryItemRS winTwoD = new SummaryItemRS();
    private SummaryItemRS winThreeD = new SummaryItemRS();
    private SummaryItemRS winFourD = new SummaryItemRS();

    private CurrencyRS winLose = new CurrencyRS();

}
