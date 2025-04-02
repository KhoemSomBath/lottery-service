package com.hacknovation.systemservice.v1_0_0.io.entity.leap;

import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseOrderEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "leap_temp_orders")
public class LeapTempOrdersEntity extends BaseOrderEntity {

}
