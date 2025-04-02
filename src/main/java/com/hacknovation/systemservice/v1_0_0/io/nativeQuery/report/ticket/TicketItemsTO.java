package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.report.ticket;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TicketItemsTO {
    private Integer orderId;
    private String betCode;
    private BigDecimal betValue;
    private BigDecimal betAmount;
    private String currencyCode;
    private Boolean isWin;
}