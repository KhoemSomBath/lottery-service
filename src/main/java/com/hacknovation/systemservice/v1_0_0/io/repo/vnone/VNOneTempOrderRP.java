package com.hacknovation.systemservice.v1_0_0.io.repo.vnone;

import com.hacknovation.systemservice.v1_0_0.io.entity.vnone.VNOneTempOrdersEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface VNOneTempOrderRP extends JpaRepository<VNOneTempOrdersEntity, Long> {

    @Query("select v from VNOneTempOrdersEntity v " +
            "where (:drawCode = 'ALL' or v.drawCode = :drawCode) and v.userCode in :userCodes and (:isMark = 0 or v.isMark = true)" +
            "order by v.id ASC")
    Page<VNOneTempOrdersEntity> findByDrawCodeAndUserCodeInOrderByIdDesc(String drawCode, Collection<String> userCodes, Integer isMark, Pageable pageable);


}