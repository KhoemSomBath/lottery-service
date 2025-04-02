package com.hacknovation.systemservice.v1_0_0.io.repo.vntwo;

import com.hacknovation.systemservice.v1_0_0.io.entity.vntwo.VNTwoTempOrderItemsEntity;
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
public interface VNTwoTempOrderItemsRP extends JpaRepository<VNTwoTempOrderItemsEntity, Long> {

    @Query(value = "SELECT * from vntwo_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND status = 1 ", nativeQuery = true)
    List<VNTwoTempOrderItemsEntity> findAllByDrawCodeAndMemberCodeIn(String drawCode, List<String> memberCodes);

    @Query(value = "SELECT * from vntwo_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND status = 1 AND bet_type LIKE '%RANGE%' ", nativeQuery = true)
    List<VNTwoTempOrderItemsEntity> findAllByDrawCodeAndMemberCodeInAndRange(String drawCode, List<String> memberCodes);

    @Query(value = "SELECT * from vntwo_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND status = 1 AND bet_type NOT LIKE '%RANGE%' ", nativeQuery = true)
    List<VNTwoTempOrderItemsEntity> findAllByDrawCodeAndMemberCodeInAndNoRange(String drawCode, List<String> memberCodes);

    @Query(value = "SELECT * from vntwo_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND status = 1 AND is_lo = :isLo ", nativeQuery = true)
    List<VNTwoTempOrderItemsEntity> findAllByDrawCodeAndMemberCodeInAndIsLo(String drawCode, List<String> memberCodes, Boolean isLo);

    @Query(value = "SELECT * from vntwo_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND status = 1 AND is_lo = :isLo AND bet_type LIKE '%RANGE%' ", nativeQuery = true)
    List<VNTwoTempOrderItemsEntity> findAllByDrawCodeAndMemberCodeInAndIsLoAndRange(String drawCode, List<String> memberCodes, Boolean isLo);

    @Query(value = "SELECT * from vntwo_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND status = 1 AND is_lo = :isLo AND bet_type NOT LIKE '%RANGE%' ", nativeQuery = true)
    List<VNTwoTempOrderItemsEntity> findAllByDrawCodeAndMemberCodeInAndIsLoAndNoRange(String drawCode, List<String> memberCodes, Boolean isLo);

    @Query(value = "SELECT * from vntwo_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND posts LIKE '%A%' AND post_analyze NOT LIKE 'A:%'", nativeQuery = true)
    List<VNTwoTempOrderItemsEntity> findAllByPostA1A2A3A4(String drawCode, List<String> memberCodes);

    @Query(value = "SELECT * from vntwo_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND posts LIKE '%A%' AND post_analyze NOT LIKE 'A:%' AND bet_type LIKE '%RANGE%' ", nativeQuery = true)
    List<VNTwoTempOrderItemsEntity> findAllByPostA1A2A3A4AndRange(String drawCode, List<String> memberCodes);

    @Query(value = "SELECT * from vntwo_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND posts LIKE '%A%' AND post_analyze NOT LIKE 'A:%' AND bet_type NOT LIKE '%RANGE%' ", nativeQuery = true)
    List<VNTwoTempOrderItemsEntity> findAllByPostA1A2A3A4AndNoRange(String drawCode, List<String> memberCodes);

    @Query(value = "SELECT * from vntwo_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND posts LIKE '%A%' AND post_analyze LIKE 'A:%'", nativeQuery = true)
    List<VNTwoTempOrderItemsEntity> findAllByPostAx4(String drawCode, List<String> memberCodes);

    @Query(value = "SELECT * from vntwo_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND posts LIKE '%A%' AND post_analyze LIKE 'A:%' AND bet_type LIKE '%RANGE%' ", nativeQuery = true)
    List<VNTwoTempOrderItemsEntity> findAllByPostAx4AndRange(String drawCode, List<String> memberCodes);

    @Query(value = "SELECT * from vntwo_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND posts LIKE '%A%' AND post_analyze LIKE 'A:%' AND bet_type NOT LIKE '%RANGE%' ", nativeQuery = true)
    List<VNTwoTempOrderItemsEntity> findAllByPostAx4AndNoRange(String drawCode, List<String> memberCodes);

    Collection<VNTwoTempOrderItemsEntity> findByOrderIdIn(List<Integer> ids);
}
