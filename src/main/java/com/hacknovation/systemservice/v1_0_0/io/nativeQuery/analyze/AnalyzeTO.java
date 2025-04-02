package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.analyze;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AnalyzeTO {
    private String postCode;
    private String postGroup;
    private String rebateCode;
    private String number;
    private BigDecimal betAmount = BigDecimal.ZERO;
}