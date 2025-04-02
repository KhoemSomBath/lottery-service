package com.hacknovation.systemservice.v1_0_0.io.entity.vntwo;

import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseAnalyzeEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/*
 * author: kangto
 * createdAt: 19/02/2022
 * time: 10:57
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "vntwo_analyzing")
public class VNTwoAnalyzeEntity extends BaseAnalyzeEntity {

    @Column(name = "is_n")
    private Boolean isNight;
}
