package com.hacknovation.systemservice.v1_0_0.io.repo.sc;

import com.hacknovation.systemservice.v1_0_0.io.entity.sc.SCTempOrderItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

/**
 * @author Sombath
 * create at 25/3/23 4:41 AM
 */
public interface SCTempOrderItemRP extends JpaRepository<SCTempOrderItemsEntity, Long> {

    @Query(value = "SELECT * from sc_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND status = 1 ", nativeQuery = true)
    List<SCTempOrderItemsEntity> findAllByDrawCodeAndMemberCodeIn(String drawCode, List<String> memberCodes);

    @Query(value = "SELECT * from sc_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND status = 1 AND bet_type LIKE '%RANGE%' ", nativeQuery = true)
    List<SCTempOrderItemsEntity> findAllByDrawCodeAndMemberCodeInAndRange(String drawCode, List<String> memberCodes);

    @Query(value = "SELECT * from sc_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND status = 1 AND bet_type NOT LIKE '%RANGE%' ", nativeQuery = true)
    List<SCTempOrderItemsEntity> findAllByDrawCodeAndMemberCodeInAndNoRange(String drawCode, List<String> memberCodes);

    Collection<SCTempOrderItemsEntity> findByOrderIdIn(List<Integer> ids);
}
