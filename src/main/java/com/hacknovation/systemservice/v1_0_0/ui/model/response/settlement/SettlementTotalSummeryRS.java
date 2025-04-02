package com.hacknovation.systemservice.v1_0_0.ui.model.response.settlement;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SettlementTotalSummeryRS {
    private BigDecimal com1DUsd = BigDecimal.ZERO;
    private BigDecimal com2DUsd = BigDecimal.ZERO;
    private BigDecimal com3DUsd = BigDecimal.ZERO;
    private BigDecimal com4DUsd = BigDecimal.ZERO;

    private BigDecimal com1DKhr = BigDecimal.ZERO;
    private BigDecimal com2DKhr = BigDecimal.ZERO;
    private BigDecimal com3DKhr = BigDecimal.ZERO;
    private BigDecimal com4DKhr = BigDecimal.ZERO;

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

    private BigDecimal oldAmountUsd = BigDecimal.ZERO;
    private BigDecimal oldAmountKhr = BigDecimal.ZERO;
    private SettlementSummaryRS borrow = new SettlementSummaryRS();
    private SettlementSummaryRS give = new SettlementSummaryRS();
    private SettlementSummaryRS protestAmount = new SettlementSummaryRS();
    private BigDecimal totalKHR = BigDecimal.ZERO;
    private BigDecimal totalUSD = BigDecimal.ZERO;
    private BigDecimal shareKhr = BigDecimal.ZERO;
    private BigDecimal shareUsd = BigDecimal.ZERO;

    public void addValueFromSettlementItemListRS(SettlementsItemListRS itemListRS) {

        this.com1DUsd = this.com1DUsd.add(itemListRS.getCom1DUsd());
        this.com2DUsd = this.com2DUsd.add(itemListRS.getCom2DUsd());
        this.com3DUsd = this.com3DUsd.add(itemListRS.getCom3DUsd());
        this.com4DUsd = this.com4DUsd.add(itemListRS.getCom4DUsd());
        this.com1DKhr = this.com1DKhr.add(itemListRS.getCom1DKhr());
        this.com2DKhr = this.com2DKhr.add(itemListRS.getCom2DKhr());
        this.com3DKhr = this.com3DKhr.add(itemListRS.getCom3DKhr());
        this.com4DKhr = this.com4DKhr.add(itemListRS.getCom4DKhr());

        this.betAmount1DUsd = this.betAmount1DUsd.add(itemListRS.getBetAmount1DUsd());
        this.betAmount2DUsd = this.betAmount2DUsd.add(itemListRS.getBetAmount2DUsd());
        this.betAmount3DUsd = this.betAmount3DUsd.add(itemListRS.getBetAmount3DUsd());
        this.betAmount4DUsd = this.betAmount4DUsd.add(itemListRS.getBetAmount4DUsd());
        this.betAmount1DKhr = this.betAmount1DKhr.add(itemListRS.getBetAmount1DKhr());
        this.betAmount2DKhr = this.betAmount2DKhr.add(itemListRS.getBetAmount2DKhr());
        this.betAmount3DKhr = this.betAmount3DKhr.add(itemListRS.getBetAmount3DKhr());
        this.betAmount4DKhr = this.betAmount4DKhr.add(itemListRS.getBetAmount4DKhr());

        this.winAmount1DUsd = this.winAmount1DUsd.add(itemListRS.getWinAmount1DUsd());
        this.winAmount2DUsd = this.winAmount2DUsd.add(itemListRS.getWinAmount2DUsd());
        this.winAmount3DUsd = this.winAmount3DUsd.add(itemListRS.getWinAmount3DUsd());
        this.winAmount4DUsd = this.winAmount4DUsd.add(itemListRS.getWinAmount4DUsd());
        this.winAmount1DKhr = this.winAmount1DKhr.add(itemListRS.getWinAmount1DKhr());
        this.winAmount2DKhr = this.winAmount2DKhr.add(itemListRS.getWinAmount2DKhr());
        this.winAmount3DKhr = this.winAmount3DKhr.add(itemListRS.getWinAmount3DKhr());
        this.winAmount4DKhr = this.winAmount4DKhr.add(itemListRS.getWinAmount4DKhr());

        this.rewardAmount1DUsd = this.rewardAmount1DUsd.add(itemListRS.getRewardAmount1DUsd());
        this.rewardAmount2DUsd = this.rewardAmount2DUsd.add(itemListRS.getRewardAmount2DUsd());
        this.rewardAmount3DUsd = this.rewardAmount3DUsd.add(itemListRS.getRewardAmount3DUsd());
        this.rewardAmount4DUsd = this.rewardAmount4DUsd.add(itemListRS.getRewardAmount4DUsd());
        this.rewardAmount1DKhr = this.rewardAmount1DKhr.add(itemListRS.getRewardAmount1DKhr());
        this.rewardAmount2DKhr = this.rewardAmount2DKhr.add(itemListRS.getRewardAmount2DKhr());
        this.rewardAmount3DKhr = this.rewardAmount3DKhr.add(itemListRS.getRewardAmount3DKhr());
        this.rewardAmount4DKhr = this.rewardAmount4DKhr.add(itemListRS.getRewardAmount4DKhr());

        this.winLoseAmountKhr = this.winLoseAmountKhr.add(itemListRS.getWinLoseAmountKhr());
        this.winLoseAmountUsd = this.winLoseAmountUsd.add(itemListRS.getWinLoseAmountUsd());

        this.totalKHR = this.totalKHR.add(itemListRS.getTotalAmountKhr());
        this.totalUSD = this.totalUSD.add(itemListRS.getTotalAmountUsd());

        this.shareKhr = this.shareKhr.add(itemListRS.getShareKhr());
        this.shareUsd = this.shareUsd.add(itemListRS.getShareUsd());
    }

    public Boolean getIsShow1DKhr(){
        return this.com1DKhr.compareTo(BigDecimal.ZERO) > 0;
    }

    public Boolean getIsShow1DUsd(){
        return this.com1DUsd.compareTo(BigDecimal.ZERO) > 0;
    }

    public Boolean getIsShow4DKhr(){
        return this.com4DKhr.compareTo(BigDecimal.ZERO) > 0;
    }

    public Boolean getIsShow4DUsd(){
        return this.com4DUsd.compareTo(BigDecimal.ZERO) > 0;
    }

    public Boolean getIsShowUsd() {
        return this.getTotalUSD().compareTo(BigDecimal.ZERO) != 0 ||
                this.getWinLoseAmountUsd().compareTo(BigDecimal.ZERO) != 0 ||
                this.getOldAmountUsd().compareTo(BigDecimal.ZERO) != 0 ||
                this.getGive().getAmountUsd().compareTo(BigDecimal.ZERO) != 0 ||
                this.getBorrow().getAmountUsd().compareTo(BigDecimal.ZERO) != 0 ||
                this.getProtestAmount().getAmountUsd().compareTo(BigDecimal.ZERO) != 0;
    }

}
