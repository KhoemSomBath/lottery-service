package com.hacknovation.systemservice.v1_0_0.ui.model.response.betting;

import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.ticket.SummaryAmountRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.ticket.TicketRS;
import lombok.Data;

import java.util.List;

/*
 * author: kangto
 * createdAt: 24/01/2022
 * time: 11:36
 */
@Data
public class ColumnRS {
    private String id;
    private String pageNumber;
    private SummaryAmountRS summaryAmount = new SummaryAmountRS();
    private SummaryAmountRS total1Digit = new SummaryAmountRS();
    private SummaryAmountRS total2Digit = new SummaryAmountRS();
    private SummaryAmountRS total3Digit = new SummaryAmountRS();
    private SummaryAmountRS total4Digit = new SummaryAmountRS();
    private String title;
    private TicketRS ticket;
    private Integer status;
    private List<TicketRS> cards;
}
