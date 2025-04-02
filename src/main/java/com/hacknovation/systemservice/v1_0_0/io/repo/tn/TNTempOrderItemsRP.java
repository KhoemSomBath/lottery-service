package com.hacknovation.systemservice.v1_0_0.io.repo.tn;

import com.hacknovation.systemservice.v1_0_0.io.entity.tn.TNTempOrderItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface TNTempOrderItemsRP extends JpaRepository<TNTempOrderItemsEntity, Long> {

    @Query(value = "SELECT * from tn_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND status = 1 ", nativeQuery = true)
    List<TNTempOrderItemsEntity> findAllByDrawCodeAndMemberCodeIn(String drawCode, List<String> memberCodes);

    @Query(value = "SELECT * from tn_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND status = 1 AND bet_type LIKE '%RANGE%' ", nativeQuery = true)
    List<TNTempOrderItemsEntity> findAllByDrawCodeAndMemberCodeInAndRange(String drawCode, List<String> memberCodes);

    @Query(value = "SELECT * from tn_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND status = 1 AND bet_type NOT LIKE '%RANGE%' ", nativeQuery = true)
    List<TNTempOrderItemsEntity> findAllByDrawCodeAndMemberCodeInAndNoRange(String drawCode, List<String> memberCodes);



    @Query(value = "SELECT * from tn_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND status = 1 AND is_lo = :isLo ", nativeQuery = true)
    List<TNTempOrderItemsEntity> findAllByDrawCodeAndMemberCodeInAndIsLo(String drawCode, List<String> memberCodes, Boolean isLo);

    @Query(value = "SELECT * from tn_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND status = 1 AND is_lo = :isLo AND bet_type LIKE '%RANGE%' ", nativeQuery = true)
    List<TNTempOrderItemsEntity> findAllByDrawCodeAndMemberCodeInAndIsLoAndRange(String drawCode, List<String> memberCodes, Boolean isLo);

    @Query(value = "SELECT * from tn_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND status = 1 AND is_lo = :isLo AND bet_type NOT LIKE '%RANGE%' ", nativeQuery = true)
    List<TNTempOrderItemsEntity> findAllByDrawCodeAndMemberCodeInAndIsLoAndNoRange(String drawCode, List<String> memberCodes, Boolean isLo);

    @Query(value = "SELECT * from tn_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND posts LIKE '%A%' AND post_analyze NOT LIKE 'A:%'", nativeQuery = true)
    List<TNTempOrderItemsEntity> findAllByPostA1A2A3A4(String drawCode, List<String> memberCodes);

    @Query(value = "SELECT * from tn_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND posts LIKE '%A%' AND post_analyze NOT LIKE 'A:%' AND bet_type LIKE '%RANGE%' ", nativeQuery = true)
    List<TNTempOrderItemsEntity> findAllByPostA1A2A3A4AndRange(String drawCode, List<String> memberCodes);

    @Query(value = "SELECT * from tn_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND posts LIKE '%A%' AND post_analyze NOT LIKE 'A:%' AND bet_type NOT LIKE '%RANGE%' ", nativeQuery = true)
    List<TNTempOrderItemsEntity> findAllByPostA1A2A3A4AndNoRange(String drawCode, List<String> memberCodes);

    @Query(value = "SELECT * from tn_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND posts LIKE '%A%' AND post_analyze LIKE 'A:%'", nativeQuery = true)
    List<TNTempOrderItemsEntity> findAllByPostAx4(String drawCode, List<String> memberCodes);

    @Query(value = "SELECT * from tn_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND posts LIKE '%A%' AND post_analyze LIKE 'A:%' AND bet_type LIKE '%RANGE%' ", nativeQuery = true)
    List<TNTempOrderItemsEntity> findAllByPostAx4AndRange(String drawCode, List<String> memberCodes);

    @Query(value = "SELECT * from tn_temp_order_items WHERE dc = :drawCode AND mc IN :memberCodes AND posts LIKE '%A%' AND post_analyze LIKE 'A:%' AND bet_type NOT LIKE '%RANGE%' ", nativeQuery = true)
    List<TNTempOrderItemsEntity> findAllByPostAx4AndNoRange(String drawCode, List<String> memberCodes);

    Collection<TNTempOrderItemsEntity> findByOrderIdIn(List<Integer> ids);
}
