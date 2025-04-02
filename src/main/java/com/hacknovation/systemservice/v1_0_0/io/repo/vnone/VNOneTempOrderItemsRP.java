package com.hacknovation.systemservice.v1_0_0.io.repo.vnone;

import com.hacknovation.systemservice.v1_0_0.io.entity.vnone.VNOneTempOrderItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/*
 * author: kangto
 * createdAt: 19/03/2022
 * time: 21:53
 */
@Repository
public interface VNOneTempOrderItemsRP extends JpaRepository<VNOneTempOrderItemsEntity, Long> {

    @Query(value = "SELECT * from vnone_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND status = 1 ", nativeQuery = true)
    List<VNOneTempOrderItemsEntity> findAllByDrawCodeAndMemberCodeIn(String drawCode, List<String> memberCodes);

    @Query(value = "SELECT * from vnone_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND status = 1 and bet_type LIKE '%RANGE%' ", nativeQuery = true)
    List<VNOneTempOrderItemsEntity> findAllByDrawCodeAndMemberCodeInAndRange(String drawCode, List<String> memberCodes);

    @Query(value = "SELECT * from vnone_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND status = 1 AND bet_type NOT LIKE '%RANGE%' ", nativeQuery = true)
    List<VNOneTempOrderItemsEntity> findAllByDrawCodeAndMemberCodeInAndNoRange(String drawCode, List<String> memberCodes);


    @Query(value = "SELECT * from vnone_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND status = 1 AND is_lo = :isLo ", nativeQuery = true)
    List<VNOneTempOrderItemsEntity> findAllByDrawCodeAndMemberCodeInAndIsLo(String drawCode, List<String> memberCodes, Boolean isLo);

    @Query(value = "SELECT * from vnone_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND status = 1 AND is_lo = :isLo AND bet_type LIKE '%RANGE%' ", nativeQuery = true)
    List<VNOneTempOrderItemsEntity> findAllByDrawCodeAndMemberCodeInAndIsLoAndRange(String drawCode, List<String> memberCodes, Boolean isLo);

    @Query(value = "SELECT * from vnone_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND status = 1 AND is_lo = :isLo AND bet_type NOT LIKE '%RANGE%' ", nativeQuery = true)
    List<VNOneTempOrderItemsEntity> findAllByDrawCodeAndMemberCodeInAndIsLoAndNoRange(String drawCode, List<String> memberCodes, Boolean isLo);

    @Query(value = "SELECT * from vnone_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND posts LIKE '%A%' AND post_analyze NOT LIKE 'A:%'", nativeQuery = true)
    List<VNOneTempOrderItemsEntity> findAllByPostA1A2A3A4(String drawCode, List<String> memberCodes);

    @Query(value = "SELECT * from vnone_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND posts LIKE '%A%' AND post_analyze NOT LIKE 'A:%' AND bet_type LIKE '%RANGE%' ", nativeQuery = true)
    List<VNOneTempOrderItemsEntity> findAllByPostA1A2A3A4AndRange(String drawCode, List<String> memberCodes);

    @Query(value = "SELECT * from vnone_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND posts LIKE '%A%' AND post_analyze NOT LIKE 'A:%' AND bet_type NOT LIKE '%RANGE%' ", nativeQuery = true)
    List<VNOneTempOrderItemsEntity> findAllByPostA1A2A3A4AndNoRange(String drawCode, List<String> memberCodes);

    @Query(value = "SELECT * from vnone_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND posts LIKE '%A%' AND post_analyze LIKE 'A:%'", nativeQuery = true)
    List<VNOneTempOrderItemsEntity> findAllByPostAx4(String drawCode, List<String> memberCodes);

    @Query(value = "SELECT * from vnone_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND posts LIKE '%A%' AND post_analyze LIKE 'A:%' AND bet_type LIKE '%RANGE%' ", nativeQuery = true)
    List<VNOneTempOrderItemsEntity> findAllByPostAx4AndRange(String drawCode, List<String> memberCodes);

    @Query(value = "SELECT * from vnone_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND posts LIKE '%A%' AND post_analyze LIKE 'A:%' AND bet_type NOT LIKE '%RANGE%' ", nativeQuery = true)
    List<VNOneTempOrderItemsEntity> findAllByPostAx4AndNoRange(String drawCode, List<String> memberCodes);

    Collection<VNOneTempOrderItemsEntity> findByOrderIdIn(Collection<Integer> orderIds);

}
