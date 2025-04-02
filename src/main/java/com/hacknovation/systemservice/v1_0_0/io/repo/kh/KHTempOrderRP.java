package com.hacknovation.systemservice.v1_0_0.io.repo.kh;

import com.hacknovation.systemservice.v1_0_0.io.entity.kh.KHTempOrdersEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface KHTempOrderRP extends JpaRepository<KHTempOrdersEntity, Long> {
    @Query("select v from KHTempOrdersEntity v " +
            "where (:drawCode = 'ALL' or v.drawCode = :drawCode) and v.userCode in :userCodes and (:isMark = 0 or v.isMark = true )" +
            "order by v.id ASC")
    Page<KHTempOrdersEntity> findByDrawCodeAndUserCodeInOrderByIdDesc(@Param("drawCode") String drawCode, @Param("userCodes") Collection<String> userCodes, Integer isMark, Pageable pageable);

}