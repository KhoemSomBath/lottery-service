package com.hacknovation.systemservice.v1_0_0.io.entity.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/*
 * author: kangto
 * createdAt: 01/02/2022
 * time: 15:06
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@MappedSuperclass
public class BaseWinOrderItems extends BaseEntity {

    @Column(name = "dc")
    private String drawCode;

    @Column(name = "oi")
    private Integer orderId;

    @Column(name = "oii")
    private Integer orderItemId;

    @Column(name = "`is`")
    private Boolean isSettlement;

    @Column(name = "wd")
    private String winDetail;

    @Column(name = "wq")
    private Integer winQty;

}
