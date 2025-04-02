package com.hacknovation.systemservice.v1_0_0.ui.model.response.report.sale;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SaleItemsRS {
    private String userCode;
    private String nickname;
    private String username;
    private String roleCode;
    private String drawCode;
    private Date drawAt;
    private Integer pageNumber;

    private BigDecimal com1DUsd = BigDecimal.ZERO;
    private BigDecimal com2DUsd = BigDecimal.ZERO;
    private BigDecimal com3DUsd = BigDecimal.ZERO;
    private BigDecimal com4DUsd = BigDecimal.ZERO;

    private BigDecimal com1DKhr = BigDecimal.ZERO;
    private BigDecimal com2DKhr = BigDecimal.ZERO;
    private BigDecimal com3DKhr = BigDecimal.ZERO;
    private BigDecimal com4DKhr = BigDecimal.ZERO;

    private BigDecimal share1DUsd = BigDecimal.ZERO;
    private BigDecimal share2DUsd = BigDecimal.ZERO;
    private BigDecimal share3DUsd = BigDecimal.ZERO;
    private BigDecimal share4DUsd = BigDecimal.ZERO;

    private BigDecimal share1DKhr = BigDecimal.ZERO;
    private BigDecimal share2DKhr = BigDecimal.ZERO;
    private BigDecimal share3DKhr = BigDecimal.ZERO;
    private BigDecimal share4DKhr = BigDecimal.ZERO;

    private BigDecimal betAmount1DUsd = BigDecimal.ZERO;
    private BigDecimal betAmount2DUsd = BigDecimal.ZERO;
    private BigDecimal betAmount3DUsd = BigDecimal.ZERO;
    private BigDecimal betAmount4DUsd = BigDecimal.ZERO;

    private BigDecimal betAmount1DKhr = BigDecimal.ZERO;
    private BigDecimal betAmount2DKhr = BigDecimal.ZERO;
    private BigDecimal betAmount3DKhr = BigDecimal.ZERO;
    private BigDecimal betAmount4DKhr = BigDecimal.ZERO;

    private BigDecimal winAmount1DUsd = BigDecimal.ZERO;
    private BigDecimal winAmount2DUsd = BigDecimal.ZERO;
    private BigDecimal winAmount3DUsd = BigDecimal.ZERO;
    private BigDecimal winAmount4DUsd = BigDecimal.ZERO;

    private BigDecimal winAmount1DKhr = BigDecimal.ZERO;
    private BigDecimal winAmount2DKhr = BigDecimal.ZERO;
    private BigDecimal winAmount3DKhr = BigDecimal.ZERO;
    private BigDecimal winAmount4DKhr = BigDecimal.ZERO;

    private BigDecimal rewardAmount1DUsd = BigDecimal.ZERO;
    private BigDecimal rewardAmount2DUsd = BigDecimal.ZERO;
    private BigDecimal rewardAmount3DUsd = BigDecimal.ZERO;
    private BigDecimal rewardAmount4DUsd = BigDecimal.ZERO;

    private BigDecimal rewardAmount1DKhr = BigDecimal.ZERO;
    private BigDecimal rewardAmount2DKhr = BigDecimal.ZERO;
    private BigDecimal rewardAmount3DKhr = BigDecimal.ZERO;
    private BigDecimal rewardAmount4DKhr = BigDecimal.ZERO;

    private BigDecimal winLoseAmountKhr = BigDecimal.ZERO;
    private BigDecimal winLoseAmountUsd = BigDecimal.ZERO;

    public BigDecimal getCommissionKhr() {
        return com1DKhr.add(com2DKhr).add(com3DKhr).add(com4DKhr);
    }

    public BigDecimal getCommissionUsd() {
        return com1DUsd.add(com2DUsd).add(com3DUsd).add(com4DUsd);
    }

    public BigDecimal getRewardAmountKhr() {
        return rewardAmount1DKhr.add(rewardAmount2DKhr).add(rewardAmount3DKhr).add(rewardAmount4DKhr);
    }

    public BigDecimal getRewardAmountUsd() {
        return rewardAmount1DUsd.add(rewardAmount2DUsd).add(rewardAmount3DUsd).add(rewardAmount4DUsd);
    }


}