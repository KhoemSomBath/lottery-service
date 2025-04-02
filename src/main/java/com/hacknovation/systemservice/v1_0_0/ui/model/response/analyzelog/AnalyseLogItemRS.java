package com.hacknovation.systemservice.v1_0_0.ui.model.response.analyzelog;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Sombath
 * create at 2/9/22 2:38 PM
 */

@Data
public class AnalyseLogItemRS {
    private String postCode;
    private String number;
    private String fullNumber;
    private String rebateCode;
    private BigDecimal protestPercentage = BigDecimal.ZERO;
    private BigDecimal realPercentage = BigDecimal.ZERO;
    private BigDecimal betAmount = BigDecimal.ZERO;
    private BigDecimal commissionAmount = BigDecimal.ZERO;
    private BigDecimal rewardAmount = BigDecimal.ZERO;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private BigDecimal winLoseAmount = BigDecimal.ZERO;
    private BigDecimal rebateAmount = BigDecimal.ZERO;
    private BigDecimal totalCommissionAmount = BigDecimal.ZERO;
    private Boolean isEditable = Boolean.FALSE;
    private Boolean isLoading = Boolean.FALSE;
    private Boolean isCanRelease = Boolean.TRUE;
}
