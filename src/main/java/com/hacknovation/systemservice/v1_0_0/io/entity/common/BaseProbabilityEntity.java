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
 * createdAt: 18/08/2022
 * time: 22:46
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@MappedSuperclass
public class BaseProbabilityEntity extends BaseEntity {

    @Column(name = "lt")
    private String lotteryType;

    @Column(name = "pc")
    private String postCode;

    @Column(name = "pg")
    private String postGroup;

    @Column(name = "rc")
    private String rebateCode;

    @Column(name = "pro_key")
    private String probabilityKey;

    @Column(name = "percentage")
    private BigDecimal percentage;

    @Column(name = "is_probability")
    private Boolean isProbability;

    @Column(name = "updated_by")
    private String updatedBy;

}
