package com.hacknovation.systemservice.v1_0_0.ui.model.request.config;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RebateRQ {
    private BigDecimal rebateRate;
    private BigDecimal waterRate;
    private Boolean status;
}
