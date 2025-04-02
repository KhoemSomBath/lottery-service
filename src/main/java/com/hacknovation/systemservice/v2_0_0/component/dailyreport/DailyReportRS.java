package com.hacknovation.systemservice.v2_0_0.component.dailyreport;

import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.sale.SaleReportRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.sale.SummaryReportRS;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sombath
 * create at 25/4/23 12:26 PM
 */

@Data
class DailyReportRS {
    private List<DailyReportItemsRS> items = new ArrayList<>();
    private SummaryReportRS underSummary = new SummaryReportRS();
    private SummaryReportRS upperSummary = new SummaryReportRS();

}

@Data
class DailyReportItemsRS {
    private String lotteryType;
    private SaleReportRS upperLineSale = new SaleReportRS();
    private SaleReportRS underLineSale = new SaleReportRS();
}

