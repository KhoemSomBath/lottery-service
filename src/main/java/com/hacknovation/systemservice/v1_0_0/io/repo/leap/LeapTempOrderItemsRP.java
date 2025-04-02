package com.hacknovation.systemservice.v1_0_0.io.repo.leap;

import com.hacknovation.systemservice.v1_0_0.io.entity.leap.LeapTempOrderItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/*
 * author: kangto
 * createdAt: 28/04/2022
 * time: 21:53
 */
@Repository
public interface LeapTempOrderItemsRP extends JpaRepository<LeapTempOrderItemsEntity, Long> {

    @Query(value = "SELECT * from leap_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND status = 1", nativeQuery = true)
    List<LeapTempOrderItemsEntity> findAllByDrawCodeAndMemberCodeIn(String drawCode, List<String> memberCodes);

    @Query(value = "SELECT * from leap_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND status = 1 AND bet_type LIKE '%RANGE%' ", nativeQuery = true)
    List<LeapTempOrderItemsEntity> findAllByDrawCodeAndMemberCodeInAndRange(String drawCode, List<String> memberCodes);

    @Query(value = "SELECT * from leap_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND status = 1 AND bet_type NOT LIKE '%RANGE%' ", nativeQuery = true)
    List<LeapTempOrderItemsEntity> findAllByDrawCodeAndMemberCodeInAndNoRange(String drawCode, List<String> memberCodes);


    @Query(value = "SELECT * from leap_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND status = 1 AND is_lo = :isLo ", nativeQuery = true)
    List<LeapTempOrderItemsEntity> findAllByDrawCodeAndMemberCodeInAndIsLo(String drawCode, List<String> memberCodes, Boolean isLo);

    @Query(value = "SELECT * from leap_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND status = 1 AND is_lo = :isLo AND bet_type LIKE '%RANGE%' ", nativeQuery = true)
    List<LeapTempOrderItemsEntity> findAllByDrawCodeAndMemberCodeInAndIsLoAndRange(String drawCode, List<String> memberCodes, Boolean isLo);

    @Query(value = "SELECT * from leap_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND status = 1 AND is_lo = :isLo AND bet_type NOT LIKE '%RANGE%' ", nativeQuery = true)
    List<LeapTempOrderItemsEntity> findAllByDrawCodeAndMemberCodeInAndIsLoAndNoRange(String drawCode, List<String> memberCodes, Boolean isLo);


    Collection<LeapTempOrderItemsEntity> findByOrderIdIn(List<Integer> ids);
}
