package com.hacknovation.systemservice.v1_0_0.io.repo;

import com.hacknovation.systemservice.v1_0_0.io.entity.SettlementItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SettlementItemRP extends JpaRepository<SettlementItemsEntity, Long> {

    @Query(value = "SELECT * FROM settlement_items si WHERE si.lc = :lotteryType AND si.mc IN (:memberCodes) AND DATE(si.issued_at) BETWEEN :filterByStartDate AND :filterByEndDate", nativeQuery = true)
    List<SettlementItemsEntity> listing(String lotteryType, List<String> memberCodes, String filterByStartDate, String filterByEndDate);


    @Query(value = "SELECT * FROM settlement_items si WHERE si.mc IN (:memberCodes) AND DATE(si.issued_at) BETWEEN :filterByStartDate AND :filterByEndDate", nativeQuery = true)
    List<SettlementItemsEntity> getAllSettlement(List<String> memberCodes, String filterByStartDate, String filterByEndDate);


    @Transactional
    @Modifying
    @Query(value = "DELETE FROM settlement_items WHERE DATE(issued_at) < :date", nativeQuery = true)
    void removeAllByDateLessThan(LocalDate date);

    @Query(value = "SELECT * FROM settlement_items WHERE DATE(issued_at) < :date", nativeQuery = true)
    List<SettlementItemsEntity> getAllByDateLessThan(LocalDate date);
}