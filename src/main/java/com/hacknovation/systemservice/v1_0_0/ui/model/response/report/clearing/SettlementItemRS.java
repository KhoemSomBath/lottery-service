package com.hacknovation.systemservice.v1_0_0.ui.model.response.report.clearing;

import com.hacknovation.systemservice.v1_0_0.ui.model.response.CurrencyRS;
import lombok.Data;

import java.math.BigDecimal;

/*
 * author: kangto
 * createdAt: 05/02/2022
 * time: 16:30
 */
@Data
public class SettlementItemRS {
    private String type;
    private Integer sort;
    private CurrencyRS value = new CurrencyRS();
}
