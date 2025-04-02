package com.hacknovation.systemservice.v1_0_0.ui.model.request.settlement;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author KHOEM Sombath
 * Date: 6/8/2021
 * Time: 10:43 AM
 */

@Data
public class SettlementItemRQ {
    private Long id;
    private String drawCode;
    private String lotteryCode;
    private String rebateCode;
    private BigDecimal amountKhr;
    private BigDecimal amountUsd;
    private String type; //'OWED','BORROW','GIVE','UNDER_PROTEST','UPPER_PROTEST'
}
