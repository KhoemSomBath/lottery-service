package com.hacknovation.systemservice.v1_0_0.ui.model.response.report.summary;

import lombok.Data;

/*
 * author: kangto
 * createdAt: 17/01/2023
 * time: 14:38
 */
@Data
public class SummaryCurrencyRS {
    private SummaryItemRS khr = new SummaryItemRS();
    private SummaryItemRS usd = new SummaryItemRS();
}
