package com.hacknovation.systemservice.v1_0_0.io.entity;

import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseEntity;
import com.sun.istack.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "transfer_temp_numbers")
public class TransferTempNumbersEntity extends BaseEntity {

    @Column(name = "uc")
    private String userCode;

    @Column(name = "lt")
    private String lotteryType;

    @Nullable
    @Column(name = "dc")
    private String drawCode;

    @Nullable
    @Column(name = "pc")
    private String postCode;

    @Nullable
    @Column(name = "rc")
    private String rebateCode;

    @Nullable
    @Column(name = "cb")
    private String createdBy;

}

