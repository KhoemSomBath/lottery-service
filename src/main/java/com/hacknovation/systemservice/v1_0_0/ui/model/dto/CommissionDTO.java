package com.hacknovation.systemservice.v1_0_0.ui.model.dto;

import java.math.BigDecimal;
import java.util.Date;

public interface CommissionDTO {
    Integer getId();
    String getUserCode();
    String getLotteryType();
    String getRebateCode();
    BigDecimal getRebateRate();
    BigDecimal getCommission();
}