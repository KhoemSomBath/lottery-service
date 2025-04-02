package com.hacknovation.systemservice.v1_0_0.ui.model.response.settlement;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/*
 * author: kangto
 * createdAt: 18/02/2022
 * time: 13:26
 */
@Data
public class SettlementListRS {
    private Boolean isEditable = Boolean.TRUE;
    private List<SettlementsItemListRS> settlements = new ArrayList<>();
    private SettlementTotalSummeryRS summery = new SettlementTotalSummeryRS();
}
