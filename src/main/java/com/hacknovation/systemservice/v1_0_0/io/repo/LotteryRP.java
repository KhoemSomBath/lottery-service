package com.hacknovation.systemservice.v1_0_0.io.repo;

import com.hacknovation.systemservice.v1_0_0.io.entity.LotteryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LotteryRP extends JpaRepository<LotteryEntity, Long> {

    @Query(value = "SELECT * FROM lotteries WHERE status = 1 ORDER BY sort_order ASC ", nativeQuery = true)
    List<LotteryEntity> listing();

    @Query(value = "select count(*) from leap_temp_orders where uc = :userCode and date(draw_at) = curdate()", nativeQuery = true)
    Long isHasLeapOrderByUserAndDate(String userCode);

    @Query(value = "select count(*) from tn_temp_orders where uc = :userCode and date(draw_at) = curdate()", nativeQuery = true)
    Long isHasTnOrderByUserAndDate(String userCode);

    @Query(value = "select count(*) from vnone_temp_orders where uc = :userCode and date(draw_at) = curdate()", nativeQuery = true)
    Long isHasVn1OrderByUserAndDate(String userCode);

    @Query(value = "select count(*) from vntwo_temp_orders where uc = :userCode and date(draw_at) = curdate()", nativeQuery = true)
    Long isHasVn2OrderByUserAndDate(String userCode);

    @Query(value = "select count(*) from khr_temp_orders where uc = :userCode and date(draw_at) = curdate()", nativeQuery = true)
    Long isHasKhOrderByUserAndDate(String userCode);
}
