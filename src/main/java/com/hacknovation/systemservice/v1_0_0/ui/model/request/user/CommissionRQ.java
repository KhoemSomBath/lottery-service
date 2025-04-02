package com.hacknovation.systemservice.v1_0_0.ui.model.request.user;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Sombath
 * create at 24/11/21 4:10 PM
 */

@Data
public class CommissionRQ {
    private String rebateCode;
    private String lotteryCode;
    private BigDecimal share = BigDecimal.ZERO;
    private BigDecimal waterRate = BigDecimal.ZERO;
    private BigDecimal rebateRate = BigDecimal.ZERO;
    private BigDecimal commission = BigDecimal.ZERO;
    private BigDecimal maxBetFirst = BigDecimal.ZERO;
    private BigDecimal maxBetSecond = BigDecimal.ZERO;
    private BigDecimal limitDigit = BigDecimal.ZERO;
    private String maxBetSecondAt;
    private BigDecimal maxBetRange;
    private Integer maxBetItemRange;

}
