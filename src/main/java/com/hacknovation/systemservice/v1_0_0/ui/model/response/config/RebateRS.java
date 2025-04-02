package com.hacknovation.systemservice.v1_0_0.ui.model.response.config;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RebateRS {
    private Long id;
    private String code;
    private String name;
    private BigDecimal rebateRate;
    private BigDecimal waterRate;
    private String updatedBy;
    private String type;
    private Integer sortOrder;
    private Boolean status;
}
