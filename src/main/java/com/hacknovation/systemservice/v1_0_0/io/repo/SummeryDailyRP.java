package com.hacknovation.systemservice.v1_0_0.io.repo;

import com.hacknovation.systemservice.v1_0_0.io.entity.SummeryDailyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface SummeryDailyRP extends JpaRepository<SummeryDailyEntity, Long> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM summery_daily WHERE DATE(issued_at) < :date", nativeQuery = true)
    void removeAllByDateLessThan(LocalDate date);

    @Query(value = "SELECT * FROM summery_daily WHERE lottery_type = :lottery and user_code in :userCodes and issued_at between :startDate and :endDate", nativeQuery = true)
    List<SummeryDailyEntity> findAllByLotteryUserCodeIn(String lottery, List<String> userCodes, String startDate, String endDate);

    @Query(value = "SELECT * FROM summery_daily WHERE user_code in :userCodes and issued_at between :startDate and :endDate", nativeQuery = true)
    List<SummeryDailyEntity> findAllByUserCodeIn(List<String> userCodes, String startDate, String endDate);

    @Query(value = "SELECT * FROM summery_daily WHERE date(issued_at) < date(:filterDate) ", nativeQuery = true)
    List<SummeryDailyEntity> findAllByUserCodeInLessThanThat(String filterDate);

    @Query(value = "SELECT * FROM summery_daily WHERE lottery_type = :lottery and user_code in :userCodes and date(issued_at) < :filterDate", nativeQuery = true)
    List<SummeryDailyEntity> findAllByLotteryUserCodeInLessThanThat(String lottery, List<String> userCodes, String filterDate);

    @Query(value = "SELECT * FROM summery_daily WHERE user_code in :userCodes and date(issued_at) < :filterDate", nativeQuery = true)
    List<SummeryDailyEntity> findAllUserCodeInLessThanThatMix(List<String> userCodes, String filterDate);

}