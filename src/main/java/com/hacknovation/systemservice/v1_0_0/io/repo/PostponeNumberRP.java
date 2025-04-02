package com.hacknovation.systemservice.v1_0_0.io.repo;

import com.hacknovation.systemservice.v1_0_0.io.entity.PostponeNumberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostponeNumberRP extends JpaRepository<PostponeNumberEntity, Long> {

    @Query(value = "SELECT * FROM postpone_number p WHERE IF(lower(:lotteryType) <> 'all', p.lottery_type = :lotteryType, TRUE) AND p.status = 1 ORDER BY p.lottery_type", nativeQuery = true)
    List<PostponeNumberEntity> postponeNumberList(String lotteryType);

    @Query(value = "SELECT * FROM postpone_number p WHERE p.lottery_type = :lotteryType AND p.status = 1 ORDER BY id DESC LIMIT 1", nativeQuery = true)
    PostponeNumberEntity postponeNumber(String lotteryType);

}
