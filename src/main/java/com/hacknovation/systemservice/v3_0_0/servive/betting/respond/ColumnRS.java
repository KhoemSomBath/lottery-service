package com.hacknovation.systemservice.v3_0_0.servive.betting.respond;

import lombok.Data;

import java.util.List;

@Data
public class ColumnRS {
    private String id;
    private OrderSummaryRS summary;
    private OrderSummaryRS total1Digit;
    private OrderSummaryRS total2Digit;
    private OrderSummaryRS total3Digit;
    private OrderSummaryRS total4Digit;
    private String title;
//    private List<CardRS> cards;
    private List<OrderTicketRS> cards;
}
