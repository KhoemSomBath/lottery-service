package com.hacknovation.systemservice.v1_0_0.io.entity;

import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name="summery_daily")
public class SummeryDailyEntity extends BaseEntity {
    private String lotteryType;
    private String userCode;
    private BigDecimal totalTurnoverKhr = BigDecimal.ZERO;
    private BigDecimal totalTurnoverUsd = BigDecimal.ZERO;
    private BigDecimal totalRewardKhr = BigDecimal.ZERO;
    private BigDecimal totalRewardUsd = BigDecimal.ZERO;
    private Date issuedAt;
    private String detail;


    public BigDecimal getWinLoseKhr() {
        return this.totalTurnoverKhr.subtract(this.totalRewardKhr);
    }

    public BigDecimal getWinLoseUsd() {
        return this.totalTurnoverUsd.subtract(this.totalRewardUsd);
    }

}