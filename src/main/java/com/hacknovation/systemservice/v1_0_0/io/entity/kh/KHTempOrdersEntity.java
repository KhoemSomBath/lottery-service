package com.hacknovation.systemservice.v1_0_0.io.entity.kh;

import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseOrderEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "khr_temp_orders")
public class KHTempOrdersEntity extends BaseOrderEntity {

}
