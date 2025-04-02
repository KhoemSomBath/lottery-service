package com.hacknovation.systemservice.v1_0_0.ui.model.response.winItem;

import lombok.Data;

import java.math.BigDecimal;

/**
 * author : phokkinnky
 * date : 1/2/22
 */
@Data
public class WinItemRS {
    private String postCode;
    private String rebateCode;
    private String number;
    private BigDecimal betAmount;
}
