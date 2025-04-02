package com.hacknovation.systemservice.v1_0_0.io.repo.vntwo;

import com.hacknovation.systemservice.v1_0_0.io.entity.vntwo.VNTwoAnalyzeLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Sombath
 * create at 2/9/22 2:24 PM
 */
public interface VNTwoAnalyzeLogRP extends JpaRepository<VNTwoAnalyzeLogEntity, Long> {

    @Query(nativeQuery = true, value = "select * from vntwo_analyzing_log where dc = :drawCode order by id")
    List<VNTwoAnalyzeLogEntity> getAllByDrawCode(String drawCode);

}
