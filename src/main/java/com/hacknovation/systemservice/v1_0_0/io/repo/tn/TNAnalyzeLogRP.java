package com.hacknovation.systemservice.v1_0_0.io.repo.tn;

import com.hacknovation.systemservice.v1_0_0.io.entity.tn.TNAnalyzeLogEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vntwo.VNTwoAnalyzeLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Sombath
 * create at 2/9/22 2:24 PM
 */
public interface TNAnalyzeLogRP extends JpaRepository<TNAnalyzeLogEntity, Long> {

    @Query(nativeQuery = true, value = "select * from tn_analyzing_log where dc = :drawCode order by id")
    List<TNAnalyzeLogEntity> getAllByDrawCode(String drawCode);

}
