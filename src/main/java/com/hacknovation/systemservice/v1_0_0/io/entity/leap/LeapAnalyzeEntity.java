package com.hacknovation.systemservice.v1_0_0.io.entity.leap;

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
 * createdAt: 28/04/2022
 * time: 10:57
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "leap_analyzing")
public class LeapAnalyzeEntity extends BaseAnalyzeEntity {

}
