package com.hacknovation.systemservice.v1_0_0.io.entity;

import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseEntity;
import com.sun.istack.Nullable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name="initial_balance")
public class InitialBalanceEntity extends BaseEntity {
    @Nullable
    @Column(name = "lottery_type")
    private String lotteryType;

    @Nullable
    @Column(name = "uc")
    private String userCode;

    @Nullable
    @Column(name = "balance_khr")
    private BigDecimal balanceKhr = BigDecimal.ZERO;

    @Nullable
    @Column(name = "balance_usd")
    private BigDecimal balanceUsd = BigDecimal.ZERO;

    @Nullable
    @Column(name = "issued_at")
    private Date issuedAt;

    @Nullable
    @Column(name = "created_by")
    private String createdBy;

    @Nullable
    @Column(name = "updated_by")
    private String updatedBy;
}