package com.hacknovation.systemservice.v3_0_0.servive.betting.respond;

import lombok.Data;

import java.util.List;

@Data
public class GroupByPostRS {
    private String posts;
    private OrderSummaryRS summary;
    private List<OrderItemRS> items;
    private Integer ordering;
}
