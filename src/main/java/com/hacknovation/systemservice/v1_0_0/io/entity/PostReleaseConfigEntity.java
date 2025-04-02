package com.hacknovation.systemservice.v1_0_0.io.entity;

import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseEntity;
import com.sun.istack.Nullable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/*
 * author: kangto
 * createdAt: 30/08/2022
 * time: 11:00
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "post_release_config")
@Data
public class PostReleaseConfigEntity extends BaseEntity {

    @Column(name = "lt")
    private String lotteryType;

    @Column(name = "pc")
    private String postCode;

    @Column(name = "is_can_release")
    private Boolean isCanRelease;

    @Nullable
    @Column(name = "updated_by")
    private String updatedBy;
}
