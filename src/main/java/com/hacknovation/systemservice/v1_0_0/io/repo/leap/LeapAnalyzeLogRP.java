package com.hacknovation.systemservice.v1_0_0.io.repo.leap;

import com.hacknovation.systemservice.v1_0_0.io.entity.leap.LeapAnalyzeLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Sombath
 * create at 2/9/22 2:24 PM
 */
public interface LeapAnalyzeLogRP extends JpaRepository<LeapAnalyzeLogEntity, Long> {

    @Query(nativeQuery = true, value = "select * from leap_analyzing_log where dc = :drawCode order by id")
    List<LeapAnalyzeLogEntity> getAllByDrawCode(String drawCode);

}
