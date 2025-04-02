package com.hacknovation.systemservice.v2_0_0.component.dailyreport;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Sombath
 * create at 25/4/23 4:53 PM
 */
@Data
public
class DailyReportTO {

    private String lottery;
    private String userCode;
    private String drawCode;
    private Date drawAt;
    private Integer pageNumber;
    private Long orderId;
    private Long hasLotteryId;

    private BigDecimal share;

    private BigDecimal com1DUsd;
    private BigDecimal com2DUsd;
    private BigDecimal com3DUsd;
    private BigDecimal com4DUsd;

    private BigDecimal com1DKhr;
    private BigDecimal com2DKhr;
    private BigDecimal com3DKhr;
    private BigDecimal com4DKhr;

    private BigDecimal betAmount1DUsd;
    private BigDecimal betAmount2DUsd;
    private BigDecimal betAmount3DUsd;
    private BigDecimal betAmount4DUsd;

    private BigDecimal betAmount1DKhr;
    private BigDecimal betAmount2DKhr;
    private BigDecimal betAmount3DKhr;
    private BigDecimal betAmount4DKhr;

    private BigDecimal winAmount1DUsd;
    private BigDecimal winAmount2DUsd;
    private BigDecimal winAmount3DUsd;
    private BigDecimal winAmount4DUsd;

    private BigDecimal winAmount1DKhr;
    private BigDecimal winAmount2DKhr;
    private BigDecimal winAmount3DKhr;
    private BigDecimal winAmount4DKhr;

    private BigDecimal rewardAmount1DUsd;
    private BigDecimal rewardAmount2DUsd;
    private BigDecimal rewardAmount3DUsd;
    private BigDecimal rewardAmount4DUsd;

    private BigDecimal rewardAmount1DKhr;
    private BigDecimal rewardAmount2DKhr;
    private BigDecimal rewardAmount3DKhr;
    private BigDecimal rewardAmount4DKhr;

}
