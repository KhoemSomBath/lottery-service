package com.hacknovation.systemservice.v3_0_0.servive.betting.respond;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderListingRS {
    private List<DrawShiftListingRS> drawShifts = new ArrayList<>();
    private Integer pageNumber;
    private List<ColumnRS> lanes = new ArrayList<>();
    private OrderSummaryRS total1Digit;
    private OrderSummaryRS total2Digit;
    private OrderSummaryRS total3Digit;
    private OrderSummaryRS total4Digit;
    private OrderSummaryRS summary;
}
