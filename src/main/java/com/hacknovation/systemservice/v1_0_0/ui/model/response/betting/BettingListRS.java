package com.hacknovation.systemservice.v1_0_0.ui.model.response.betting;

import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.ticket.SummaryAmountRS;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/*
 * author: kangto
 * createdAt: 24/01/2022
 * time: 11:44
 */
@Data
public class BettingListRS {
    private String pageNumber;
    private List<ColumnRS> lanes = new ArrayList<>();
    private SummaryAmountRS total1Digit;
    private SummaryAmountRS total2Digit;
    private SummaryAmountRS total3Digit;
    private SummaryAmountRS total4Digit;
    private SummaryAmountRS summaryAmount;
}
