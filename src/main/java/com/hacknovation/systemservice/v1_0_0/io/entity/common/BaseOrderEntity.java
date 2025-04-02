package com.hacknovation.systemservice.v1_0_0.io.entity.common;

import com.sun.istack.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/*
 * author: kangto
 * createdAt: 09/01/2022
 * time: 11:38
 */
@Setter
@Getter
@ToString
@RequiredArgsConstructor
@MappedSuperclass
public class BaseOrderEntity extends BaseEntity {

    @Nullable
    @Column(name = "uc")
    private String userCode;

    @Nullable
    @Column(name = "tn")
    private String ticketNumber;

    @Nullable
    @Column(name = "pn")
    private Integer pageNumber;

    @Nullable
    @Column(name = "cn")
    private Integer columnNumber;

    @Nullable
    @Column(name = "dc")
    private String drawCode;

    @Column(name = "draw_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date drawAt;

    @Column(name = "platform_name")
    private String platformName;

    @Column(name = "platform_type")
    private String platformType;

    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Nullable
    @Column(name = "status")
    private Integer status = 1;

    private Boolean isSeen;
    private Boolean isMark;

    @Nullable
    @Column(name = "has_lottery_id")
    private Integer userHasLotteryId;

    @NotNull
    @Column(name = "is_cal_reward", nullable = false)
    private Boolean isCalReward = false;

}
