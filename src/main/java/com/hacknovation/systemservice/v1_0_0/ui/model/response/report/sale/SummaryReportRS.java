package com.hacknovation.systemservice.v1_0_0.ui.model.response.report.sale;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SummaryReportRS {
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

    private BigDecimal rewardAmount1DUsd = BigDecimal.ZERO;
    private BigDecimal rewardAmount2DUsd = BigDecimal.ZERO;
    private BigDecimal rewardAmount3DUsd = BigDecimal.ZERO;
    private BigDecimal rewardAmount4DUsd = BigDecimal.ZERO;

    private BigDecimal rewardAmount1DKhr = BigDecimal.ZERO;
    private BigDecimal rewardAmount2DKhr = BigDecimal.ZERO;
    private BigDecimal rewardAmount3DKhr = BigDecimal.ZERO;
    private BigDecimal rewardAmount4DKhr = BigDecimal.ZERO;

    private BigDecimal winAmount1DUsd = BigDecimal.ZERO;
    private BigDecimal winAmount2DUsd = BigDecimal.ZERO;
    private BigDecimal winAmount3DUsd = BigDecimal.ZERO;
    private BigDecimal winAmount4DUsd = BigDecimal.ZERO;

    private BigDecimal winAmount1DKhr = BigDecimal.ZERO;
    private BigDecimal winAmount2DKhr = BigDecimal.ZERO;
    private BigDecimal winAmount3DKhr = BigDecimal.ZERO;
    private BigDecimal winAmount4DKhr = BigDecimal.ZERO;

    private BigDecimal winLoseAmountKhr = BigDecimal.ZERO;
    private BigDecimal winLoseAmountUsd = BigDecimal.ZERO;

    private BigDecimal oldAmountUsd = BigDecimal.ZERO;
    private BigDecimal oldAmountKhr = BigDecimal.ZERO;

    private BigDecimal giveAmountUsd = BigDecimal.ZERO;
    private BigDecimal giveAmountKhr = BigDecimal.ZERO;

    private BigDecimal borrowAmountUsd = BigDecimal.ZERO;
    private BigDecimal borrowAmountKhr = BigDecimal.ZERO;

    private BigDecimal protestAmountUsd = BigDecimal.ZERO;
    private BigDecimal protestAmountKhr = BigDecimal.ZERO;

    private BigDecimal shareAmountUsd = BigDecimal.ZERO;
    private BigDecimal shareAmountKhr = BigDecimal.ZERO;

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

    public BigDecimal getTotalAmountUsd() {
        BigDecimal totalUsd = oldAmountUsd.add(giveAmountUsd).add(borrowAmountUsd).add(protestAmountUsd);
        if(this.getTotalShareUsd() != null && this.getTotalShareUsd().compareTo(BigDecimal.ZERO) > 0)
            totalUsd = totalUsd.add(this.getTotalShareUsd());
        else
            totalUsd = totalUsd.add(winLoseAmountUsd);
        return totalUsd;
    }

    public BigDecimal getTotalAmountKhr() {
        BigDecimal totalKhr = oldAmountKhr.add(giveAmountKhr).add(borrowAmountKhr).add(protestAmountKhr);
        if(this.getTotalShareKhr() != null && this.getTotalShareKhr().compareTo(BigDecimal.ZERO) > 0)
            totalKhr = totalKhr.add(this.getTotalShareKhr());
        else
            totalKhr = totalKhr.add(winLoseAmountKhr);
        return totalKhr;
    }

    public BigDecimal getTotalShareUsd() {
        return share1DUsd.add(share2DUsd).add(share3DUsd).add(share4DUsd);
    }

    public BigDecimal getTotalShareKhr() {
        return share1DKhr.add(share2DKhr).add(share3DKhr).add(share4DKhr);
    }

    public void add(SummaryReportRS summary) {
        this.setCom1DKhr(this.getCom1DKhr().add(summary.getCom1DKhr()));
        this.setCom2DKhr(this.getCom2DKhr().add(summary.getCom2DKhr()));
        this.setCom3DKhr(this.getCom3DKhr().add(summary.getCom3DKhr()));
        this.setCom4DKhr(this.getCom4DKhr().add(summary.getCom4DKhr()));

        this.setCom1DUsd(this.getCom1DUsd().add(summary.getCom1DUsd()));
        this.setCom2DUsd(this.getCom2DUsd().add(summary.getCom2DUsd()));
        this.setCom3DUsd(this.getCom3DUsd().add(summary.getCom3DUsd()));
        this.setCom4DUsd(this.getCom4DUsd().add(summary.getCom4DUsd()));

        this.setShare1DKhr(this.getShare1DKhr().add(summary.getShare1DKhr()));
        this.setShare2DKhr(this.getShare2DKhr().add(summary.getShare2DKhr()));
        this.setShare3DKhr(this.getShare3DKhr().add(summary.getShare3DKhr()));
        this.setShare4DKhr(this.getShare4DKhr().add(summary.getShare4DKhr()));

        this.setShare1DUsd(this.getShare1DUsd().add(summary.getShare1DUsd()));
        this.setShare2DUsd(this.getShare2DUsd().add(summary.getShare2DUsd()));
        this.setShare3DUsd(this.getShare3DUsd().add(summary.getShare3DUsd()));
        this.setShare4DUsd(this.getShare4DUsd().add(summary.getShare4DUsd()));

        this.setBetAmount1DKhr(this.getBetAmount1DKhr().add(summary.getBetAmount1DKhr()));
        this.setBetAmount2DKhr(this.getBetAmount2DKhr().add(summary.getBetAmount2DKhr()));
        this.setBetAmount3DKhr(this.getBetAmount3DKhr().add(summary.getBetAmount3DKhr()));
        this.setBetAmount4DKhr(this.getBetAmount4DKhr().add(summary.getBetAmount4DKhr()));

        this.setBetAmount1DUsd(this.getBetAmount1DUsd().add(summary.getBetAmount1DUsd()));
        this.setBetAmount2DUsd(this.getBetAmount2DUsd().add(summary.getBetAmount2DUsd()));
        this.setBetAmount3DUsd(this.getBetAmount3DUsd().add(summary.getBetAmount3DUsd()));
        this.setBetAmount4DUsd(this.getBetAmount4DUsd().add(summary.getBetAmount4DUsd()));

        this.setWinAmount1DKhr(this.getWinAmount1DKhr().add(summary.getWinAmount1DKhr()));
        this.setWinAmount2DKhr(this.getWinAmount2DKhr().add(summary.getWinAmount2DKhr()));
        this.setWinAmount3DKhr(this.getWinAmount3DKhr().add(summary.getWinAmount3DKhr()));
        this.setWinAmount4DKhr(this.getWinAmount4DKhr().add(summary.getWinAmount4DKhr()));

        this.setWinAmount1DUsd(this.getWinAmount1DUsd().add(summary.getWinAmount1DUsd()));
        this.setWinAmount2DUsd(this.getWinAmount2DUsd().add(summary.getWinAmount2DUsd()));
        this.setWinAmount3DUsd(this.getWinAmount3DUsd().add(summary.getWinAmount3DUsd()));
        this.setWinAmount4DUsd(this.getWinAmount4DUsd().add(summary.getWinAmount4DUsd()));

        this.setRewardAmount1DKhr(this.getRewardAmount1DKhr().add(summary.getRewardAmount1DKhr()));
        this.setRewardAmount2DKhr(this.getRewardAmount2DKhr().add(summary.getRewardAmount2DKhr()));
        this.setRewardAmount3DKhr(this.getRewardAmount3DKhr().add(summary.getRewardAmount3DKhr()));
        this.setRewardAmount4DKhr(this.getRewardAmount4DKhr().add(summary.getRewardAmount4DKhr()));

        this.setRewardAmount1DUsd(this.getRewardAmount1DUsd().add(summary.getRewardAmount1DUsd()));
        this.setRewardAmount2DUsd(this.getRewardAmount2DUsd().add(summary.getRewardAmount2DUsd()));
        this.setRewardAmount3DUsd(this.getRewardAmount3DUsd().add(summary.getRewardAmount3DUsd()));
        this.setRewardAmount4DUsd(this.getRewardAmount4DUsd().add(summary.getRewardAmount4DUsd()));

        this.setWinLoseAmountKhr(this.getWinLoseAmountKhr().add(summary.getWinLoseAmountKhr()));
        this.setWinLoseAmountUsd(this.getWinLoseAmountUsd().add(summary.getWinLoseAmountUsd()));

        this.setShareAmountKhr(this.getShareAmountKhr().add(summary.getShareAmountKhr()));
        this.setShareAmountUsd(this.getShareAmountUsd().add(summary.getShareAmountUsd()));

        this.setOldAmountKhr(this.getOldAmountKhr().add(summary.getOldAmountKhr()));
        this.setOldAmountUsd(this.getOldAmountUsd().add(summary.getOldAmountUsd()));

        this.setGiveAmountKhr(this.getGiveAmountKhr().add(summary.getGiveAmountKhr()));
        this.setGiveAmountUsd(this.getGiveAmountUsd().add(summary.getGiveAmountUsd()));

        this.setBorrowAmountKhr(this.getBorrowAmountKhr().add(summary.getBorrowAmountKhr()));
        this.setBorrowAmountUsd(this.getBorrowAmountUsd().add(summary.getBorrowAmountUsd()));

        this.setProtestAmountKhr(this.getProtestAmountKhr().add(summary.getProtestAmountKhr()));
        this.setProtestAmountUsd(this.getProtestAmountUsd().add(summary.getProtestAmountUsd()));
    }

    public Boolean getIsShow1DKhr() {
        return this.getCom1DKhr().compareTo(BigDecimal.ZERO) > 0;
    }

    public Boolean getIsShow1DUsd() {
        return this.getCom1DUsd().compareTo(BigDecimal.ZERO) > 0;
    }

    public Boolean getIsShow4DKhr() {
        return this.getCom4DKhr().compareTo(BigDecimal.ZERO) > 0;
    }

    public Boolean getIsShow4DUsd() {
        return this.getCom4DUsd().compareTo(BigDecimal.ZERO) > 0;
    }

    public Boolean getIsShowUsd() {
        return this.getTotalAmountUsd().compareTo(BigDecimal.ZERO) != 0 ||
                this.getWinLoseAmountUsd().compareTo(BigDecimal.ZERO) != 0 ||
                this.getOldAmountUsd().compareTo(BigDecimal.ZERO) != 0 ||
                this.getGiveAmountUsd().compareTo(BigDecimal.ZERO) != 0 ||
                this.getBorrowAmountUsd().compareTo(BigDecimal.ZERO) != 0 ||
                this.getProtestAmountUsd().compareTo(BigDecimal.ZERO) != 0;
    }

}