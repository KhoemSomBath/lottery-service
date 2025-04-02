package com.hacknovation.systemservice.v1_0_0.ui.model.response.report.ticket;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * author: kangto
 * createdAt: 11/01/2022
 * time: 11:14
 */
@Data
public class TicketRS {
    private String ticketNumber;
    private String drawCode;
    private String userCode;
    private String username;
    private Boolean isSeen;
    private Boolean isMark;
    private Date createdAt;
    private Date drawAt;
    private Integer status;
    private AmountRS totalKhr;
    private AmountRS totalUsd;
    private SummaryAmountRS total1Digit = new SummaryAmountRS();
    private SummaryAmountRS total2Digit = new SummaryAmountRS();
    private SummaryAmountRS total3Digit = new SummaryAmountRS();
    private SummaryAmountRS total4Digit = new SummaryAmountRS();
    private List<GroupOrderItemByPostRS> postGroup = new ArrayList<>();
}
