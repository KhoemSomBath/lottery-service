package com.hacknovation.systemservice.v1_0_0.io.repo;

import com.hacknovation.systemservice.v1_0_0.io.entity.ShiftsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShiftsRP extends JpaRepository<ShiftsEntity, Long> {

    @Query(value = "SELECT * FROM shifts s WHERE CASE WHEN :lotteryType <> 'ALL' THEN s.type = :lotteryType ELSE TRUE END AND s.status = 1 ORDER BY s.type,s.resulted_post_at ASC", nativeQuery = true)
    List<ShiftsEntity> shifts(String lotteryType);

    @Query(value = "SELECT * FROM shifts s WHERE s.type = :lotteryType AND s.status = 1 AND code = :shiftCode ORDER BY s.resulted_post_at ASC LIMIT 1", nativeQuery = true)
    ShiftsEntity findShiftByLotteryTypeAndShiftCode(String lotteryType, String shiftCode);

}
