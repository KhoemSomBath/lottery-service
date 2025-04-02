package com.hacknovation.systemservice.v1_0_0.io.entity;

import com.hacknovation.systemservice.enums.FinanceTypeEnum;
import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseEntity;
import com.sun.istack.Nullable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name="settlement_items")
public class SettlementItemsEntity extends BaseEntity {

    @Nullable
    @Column(name = "lc")
    private String lotteryCode;

    @Nullable
    @Column(name = "amount_usd")
    private BigDecimal amountUsd = BigDecimal.ZERO;

    @Nullable
    @Column(name = "amount_khr")
    private BigDecimal amountKhr = BigDecimal.ZERO;

    @Nullable
    @Column(name = "mc")
    private String memberCode;

    private Date issuedAt;

    @Nullable
    @Enumerated(EnumType.STRING)
    private FinanceTypeEnum type;

    private String createdBy;
    private String updatedBy;

}