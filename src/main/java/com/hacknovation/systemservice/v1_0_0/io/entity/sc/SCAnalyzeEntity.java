package com.hacknovation.systemservice.v1_0_0.io.entity.sc;

import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseAnalyzeEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "sc_analyzing")
public class SCAnalyzeEntity extends BaseAnalyzeEntity {

}
