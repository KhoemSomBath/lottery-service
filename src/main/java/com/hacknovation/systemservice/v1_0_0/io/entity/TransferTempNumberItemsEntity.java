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
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "transfer_temp_number_items")
public class TransferTempNumberItemsEntity extends BaseEntity {

    @Column(name = "tn_id")
    private Integer tnId;

    @Column(name = "number")
    private String number;

    @Nullable
    @Column(name = "transfer_amount")
    private BigDecimal transferAmount;

}
