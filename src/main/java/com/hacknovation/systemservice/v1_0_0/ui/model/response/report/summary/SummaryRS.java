package com.hacknovation.systemservice.v1_0_0.ui.model.response.report.summary;

import lombok.Data;

/*
 * author: kangto
 * createdAt: 17/01/2023
 * time: 14:39
 */
@Data
public class SummaryRS {
    private SummaryCurrencyRS oneD = new SummaryCurrencyRS();
    private SummaryCurrencyRS twoD = new SummaryCurrencyRS();
    private SummaryCurrencyRS threeD = new SummaryCurrencyRS();
    private SummaryCurrencyRS fourD = new SummaryCurrencyRS();
}
