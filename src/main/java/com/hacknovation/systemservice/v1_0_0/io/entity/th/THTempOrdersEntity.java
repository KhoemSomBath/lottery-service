package com.hacknovation.systemservice.v1_0_0.io.entity.th;

import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseOrderEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "th_temp_orders")
public class THTempOrdersEntity extends BaseOrderEntity {

}
