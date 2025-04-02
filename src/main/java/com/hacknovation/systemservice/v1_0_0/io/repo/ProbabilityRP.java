package com.hacknovation.systemservice.v1_0_0.io.repo;

import com.hacknovation.systemservice.v1_0_0.io.entity.ProbabilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * author: kangto
 * createdAt: 18/08/2022
 * time: 23:31
 */
@Repository
public interface ProbabilityRP extends JpaRepository<ProbabilityEntity, Long> {

    @Query(value = "SELECT * FROM probabilities WHERE CASE WHEN :lotteryType <> 'ALL' THEN lt = :lotteryType ELSE TRUE END ", nativeQuery = true)
    List<ProbabilityEntity> findAllByLotteryType(String lotteryType);


    @Query(value = "SELECT * FROM probabilities WHERE lt = :lotteryType AND pc = :postCode ", nativeQuery = true)
    List<ProbabilityEntity> findAllByLotteryTypeAndPostCode(String lotteryType, String postCode);

}
