package com.hacknovation.systemservice.v1_0_0.io.repo;

import com.hacknovation.systemservice.v1_0_0.io.entity.ProbabilityHasDrawingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * author: kangto
 * createdAt: 18/08/2022
 * time: 23:32
 */
@Repository
public interface ProbabilityHasDrawingRP extends JpaRepository<ProbabilityHasDrawingEntity, Long> {

    @Query(value = "SELECT * FROM probability_has_drawing WHERE CASE WHEN :lotteryType <> 'ALL' THEN lt = :lotteryType ELSE TRUE END AND IF(:shiftCode <> 'ALL', sc = :shiftCode, TRUE) ", nativeQuery = true)
    List<ProbabilityHasDrawingEntity> findAllByLotteryTypeAndShiftCode(String lotteryType, String shiftCode);

    @Query(value = "SELECT * FROM probability_has_drawing WHERE lt = :lotteryType AND sc = :shiftCode AND pc = :postCode ", nativeQuery = true)
    List<ProbabilityHasDrawingEntity> findAllByLotteryTypeAndShiftCodeAndPostCode(String lotteryType, String shiftCode, String postCode);

}
