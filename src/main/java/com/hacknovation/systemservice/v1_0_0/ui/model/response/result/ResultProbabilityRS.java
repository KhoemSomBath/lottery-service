package com.hacknovation.systemservice.v1_0_0.ui.model.response.result;


import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Sombath
 * create at 4/9/21 10:40 AM
 */

@Data
public class ResultProbabilityRS {
    BigInteger id;
    String post;
    String postTemp;
    Boolean isEditable = true;
    String rebateCode;
    String number;
    String sixDigit;
    BigDecimal rewardPercent;
    BigDecimal totalSale = BigDecimal.ZERO;
    BigDecimal protestPercent = BigDecimal.ZERO;
    BigDecimal protestPercent3d = BigDecimal.ZERO;
    BigDecimal realPercent  = BigDecimal.ZERO;
    Boolean isRelease = true;
}
