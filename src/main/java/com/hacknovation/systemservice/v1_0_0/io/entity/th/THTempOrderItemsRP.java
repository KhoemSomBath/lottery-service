package com.hacknovation.systemservice.v1_0_0.io.entity.th;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface THTempOrderItemsRP extends JpaRepository<THTempOrderItemsEntity, Long> {

    Collection<THTempOrderItemsEntity> findByOrderIdIn(List<Integer> ids);

}