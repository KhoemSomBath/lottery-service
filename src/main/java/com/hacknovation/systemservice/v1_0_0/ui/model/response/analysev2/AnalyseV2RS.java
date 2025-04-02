package com.hacknovation.systemservice.v1_0_0.ui.model.response.analysev2;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/*
 * author: kangto
 * createdAt: 06/04/2023
 * time: 16:03
 */
@Data
public class AnalyseV2RS {
    private String postCode;
    private BigDecimal betAmount = BigDecimal.ZERO;
    private BigDecimal commissionAmount = BigDecimal.ZERO;
    private Integer countItem = 0;
    private List<AnalyseItemV2RS> items = new ArrayList<>();

    public Integer getCountItem() {
        return items.size();
    }
}
