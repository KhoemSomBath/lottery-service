package com.hacknovation.systemservice.v1_0_0.ui.model.response.report.ticket;

import lombok.Data;

import java.util.List;

/*
 * author: kangto
 * createdAt: 11/01/2022
 * time: 10:15
 */
@Data
public class GroupOrderItemByPostRS {
    private String posts;
    private List<OrderItemsRS> items;
    private SummaryAmountRS summaryAmount;
    private Integer ordering = 0;

    public String getPosts() {
        if (posts != null)
            return posts.replace("LoABCD", "Lo");
        return null;
    }
}
