package com.hacknovation.systemservice.v1_0_0.io.entity.vntwo;

import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseOrderEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "vntwo_temp_orders")
public class VNTwoTempOrdersEntity extends BaseOrderEntity {

}
