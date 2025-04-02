package com.hacknovation.systemservice.v1_0_0.io.entity;

import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseEntity;
import com.sun.istack.Nullable;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "has_lottery_template")
public class HasLotteryTemplateEntity extends BaseEntity {

    @Column(name = "dow")
    private String dayOfWeek;

    @Column(name = "pc")
    private String postCode;

    @Column(name = "uc")
    private String userCode;

    @Nullable
    @Column(name = "lottery_code")
    private String lotteryCode;

    @Nullable
    @Column(name = "rc")
    private String rebateCode;

    @Nullable
    @Column(name = "limit_digit")
    private BigDecimal limitDigit;

    @Nullable
    @Column(name = "status")
    private Boolean status = true;

    @Nullable
    @Column(name = "updated_by")
    private String updatedBy;


}
