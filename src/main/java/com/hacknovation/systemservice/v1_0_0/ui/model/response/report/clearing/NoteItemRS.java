package com.hacknovation.systemservice.v1_0_0.ui.model.response.report.clearing;

import lombok.Data;

import java.math.BigDecimal;

/*
 * author: kangto
 * createdAt: 05/02/2022
 * time: 16:36
 */
@Data
public class NoteItemRS {
    private Long id;
    private BigDecimal amountKhr = BigDecimal.ZERO;
    private BigDecimal amountUsd = BigDecimal.ZERO;
}
