package com.hacknovation.systemservice.v1_0_0.ui.model.request.analyze;

import lombok.Data;

import java.math.BigDecimal;

/*
 * author: kangto
 * createdAt: 19/02/2022
 * time: 11:26
 */
@Data
public class RebateRQ {
    private String postCode;
    private BigDecimal oneD = BigDecimal.valueOf(100);
    private BigDecimal twoD = BigDecimal.valueOf(100);
    private BigDecimal threeD = BigDecimal.valueOf(100);
    private BigDecimal fourD = BigDecimal.valueOf(100);
    private BigDecimal fiveD = BigDecimal.valueOf(100);


    public void setDefaultMoney() {
        this.oneD = BigDecimal.valueOf(20000);
        this.twoD = BigDecimal.valueOf(20000);
        this.threeD = BigDecimal.valueOf(20000);
        this.fourD = BigDecimal.valueOf(20000);
        this.fiveD = BigDecimal.valueOf(20000);
    }
}
