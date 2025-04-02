package com.hacknovation.systemservice.v3_0_0.servive.betting.respond;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class OrderTicketRS {
    private String id;
    private String ticketNumber;
    private String drawCode;
    private String userCode;
    private Date createdAt;
    private AmountRS totalUsd;
    private AmountRS totalKhr;
    private Integer cardLength;
    private OrderSummaryRS summary;
    private OrderSummaryRS total1Digit;
    private OrderSummaryRS total2Digit;
    private OrderSummaryRS total3Digit;
    private OrderSummaryRS total4Digit;
    private List<GroupByPostRS> postGroup = new ArrayList<>();
}
