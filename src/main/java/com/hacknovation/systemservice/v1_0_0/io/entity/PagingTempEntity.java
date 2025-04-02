package com.hacknovation.systemservice.v1_0_0.io.entity;

import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseEntity;
import com.sun.istack.Nullable;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "paging_temp")
public class PagingTempEntity extends BaseEntity {

    @Column(name = "uc")
    private String userCode;

    @Nullable
    @Column(name = "pn")
    private Integer pageNumber = 0;

    @Nullable
    @Column(name = "lottery_code")
    private String lotteryCode;

    @Nullable
    @Column(name = "dc")
    private String drawCode;

}
