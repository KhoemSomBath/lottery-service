package com.hacknovation.systemservice.v1_0_0.ui.model.response.report.sale;

import lombok.Data;

import java.util.List;

@Data
public class MainSaleReportRS {
    private String filterByStartDate;
    private String filterByEndDate;
    private SaleReportRS upperLineSale;
    private SaleReportRS underLineSale;
}
