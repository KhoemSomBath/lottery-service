package com.hacknovation.systemservice.v1_0_0.io.repo.sc;

import com.hacknovation.systemservice.v1_0_0.io.entity.sc.SCAnalyzeLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Sombath
 * create at 2/9/22 2:24 PM
 */
public interface SCAnalyzeLogRP extends JpaRepository<SCAnalyzeLogEntity, Long> {

    @Query(nativeQuery = true, value = "select * from sc_analyzing_log where dc = :drawCode order by id")
    List<SCAnalyzeLogEntity> getAllByDrawCode(String drawCode);

}
