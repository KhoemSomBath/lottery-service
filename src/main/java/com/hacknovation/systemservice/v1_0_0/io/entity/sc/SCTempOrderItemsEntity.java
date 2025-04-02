package com.hacknovation.systemservice.v1_0_0.io.entity.sc;

import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseOrderItemsEntity;
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
@Table(name = "sc_temp_order_items")
public class SCTempOrderItemsEntity extends BaseOrderItemsEntity {

}
