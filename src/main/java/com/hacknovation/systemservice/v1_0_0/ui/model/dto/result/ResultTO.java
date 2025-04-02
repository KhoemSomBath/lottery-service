package com.hacknovation.systemservice.v1_0_0.ui.model.dto.result;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author : sombath
 * Date : 7/23/21, Friday
 **/

@Data
public class ResultTO {
    BigInteger id;
    Integer drawingId;
    Integer columnNumber;
    String postCode;
    String province;
    String postGroup;
    String twoDigit;
    String threeDigit;
    String fourDigit;
    String fiveDigit;
    String sixDigit;
    String sevenDigit;
    String detail;
    BigDecimal protestPercent;
    BigDecimal protestPercent3d;
    BigDecimal totalSale;
    BigDecimal totalCommission;
    BigDecimal totalReward;
    BigDecimal realPercent;
    String number;

    BigDecimal totalSale3D;
    BigDecimal totalCommission3D;
    BigDecimal totalReward3D;
    BigDecimal realPercent3D;
    String number3D;

}
