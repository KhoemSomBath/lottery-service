package com.hacknovation.systemservice.v1_0_0.io.entity.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;

/*
 * author: kangto
 * createdAt: 19/02/2022
 * time: 10:52
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@MappedSuperclass
public class BaseAnalyzeEntity extends BaseEntity {

    @Column(name = "dc")
    private String drawCode;

    @Column(name = "uc")
    private String userCode;

    @Column(name = "io")
    private Integer orderId;

    @Column(name = "pc")
    private String postCode;

    @Column(name = "pg")
    private String postGroup;

    @Column(name = "rc")
    private String rebateCode;

    @Column(name = "nu")
    private String number;

    @Column(name = "ba")
    private BigDecimal betAmount;

    @Column(name = "rebate_rate")
    private String rebateRate;

    @Column(name = "is_lo")
    private Boolean isLo;

}
