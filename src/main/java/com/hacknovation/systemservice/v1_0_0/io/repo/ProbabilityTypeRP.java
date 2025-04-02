package com.hacknovation.systemservice.v1_0_0.io.repo;

import com.hacknovation.systemservice.v1_0_0.io.entity.ProbabilityTypeEntity;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.probability.ProbabilityRuleTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * author: kangto
 * createdAt: 25/08/2022
 * time: 21:59
 */
@Repository
public interface ProbabilityTypeRP extends JpaRepository<ProbabilityTypeEntity, Long> {

    @Query(value = "SELECT * FROM probability_types WHERE lottery_type = :lotteryType ORDER BY id LIMIT 1", nativeQuery = true)
    ProbabilityTypeEntity getByLotteryType(String lotteryType);

    @Transactional
    @Modifying
    @Query(value = "UPDATE leap_temp_drawing SET is_has_drawing = :isHasDrawing WHERE is_recent ", nativeQuery = true)
    void updateLeapDrawingTableIsHasDrawing(Boolean isHasDrawing);

    @Transactional
    @Modifying
    @Query(value = "UPDATE sc_temp_drawing SET is_has_drawing = :isHasDrawing WHERE is_recent ", nativeQuery = true)
    void updateSCDrawingTableIsHasDrawing(Boolean isHasDrawing);

    @Transactional
    @Modifying
    @Query(value = "UPDATE vntwo_temp_drawing SET is_has_drawing = :isHasDrawing WHERE is_recent ", nativeQuery = true)
    void updateVN2DrawingTableIsHasDrawing(Boolean isHasDrawing);

    @Transactional
    @Modifying
    @Query(value = "UPDATE tn_temp_drawing SET is_has_drawing = :isHasDrawing WHERE is_recent ", nativeQuery = true)
    void updateTNDrawingTableIsHasDrawing(Boolean isHasDrawing);


    @Query(value = "SELECT p.lottery_type lotteryType, " +
            "p.is_percentage_all_drawing isPercentageAllDraws, " +
            "p.updated_by updatedBy, " +
            "p.updated_at updatedAt " +
            "FROM probability_types p ", nativeQuery = true)
    List<ProbabilityRuleTO> getAllProbabilityRules();
}
