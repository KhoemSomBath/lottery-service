package com.hacknovation.systemservice.v1_0_0.io.repo.kh;

import com.hacknovation.systemservice.v1_0_0.io.entity.kh.KHTempDrawingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/*
 * author: kangto
 * createdAt: 28/04/2022
 * time: 22:28
 */
@Repository
public interface KHTempDrawingRP extends JpaRepository<KHTempDrawingEntity, Long> {

    KHTempDrawingEntity findByCode(String code);

    @Query(value = "SELECT * FROM khr_temp_drawing  ORDER BY id DESC LIMIT 1", nativeQuery = true)
    KHTempDrawingEntity lastDraw();

    @Query(value = "SELECT * FROM khr_temp_drawing WHERE `is_recent` = 1 ORDER BY id ASC LIMIT 1", nativeQuery = true)
    KHTempDrawingEntity recentDrawing();

    @Query(value = "SELECT * FROM khr_temp_drawing WHERE CONVERT_TZ(resulted_post_at, '+0:00', '+7:00') = :drawAt ORDER BY id ASC LIMIT 1", nativeQuery = true)
    KHTempDrawingEntity getDrawingByDrawAt(String drawAt);

    @Transactional
    @Modifying
    @Query(value = "UPDATE transaction_balance tb, " +
            "(SELECT " +
            "    vtoi.mc userCode, " +
            "    SUM(IF(vtoi.cc = 'KHR', vtoi.bet_amount * vtoi.member_rebate_rate * vtwoi.wq, 0)) rewardAmountKhr, " +
            "    SUM(IF(vtoi.cc = 'USD', vtoi.bet_amount * vtoi.member_rebate_rate * vtwoi.wq, 0)) rewardAmountUsd " +
            "FROM  khr_temp_order_items vtoi " +
            "INNER JOIN  khr_temp_win_order_items vtwoi ON vtoi.id = vtwoi.oii " +
            "INNER JOIN users u ON u.`code` = vtoi.mc AND u.is_online IS TRUE " +
            "WHERE vtwoi.dc = :drawCode " +
            "GROUP BY mc) win " +
            "SET tb.balance_khr = tb.balance_khr - win.rewardAmountKhr, " +
            "    tb.balance_usd = tb.balance_usd - win.rewardAmountUsd " +
            "WHERE tb.user_code=win.userCode ", nativeQuery = true)
    void updateBalanceMemberOnlineByDrawCode(String drawCode);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO khr_drawing SELECT * FROM khr_temp_drawing WHERE code = :drawCode", nativeQuery = true)
    void saveDrawing(String drawCode);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM khr_drawing WHERE code = :drawCode", nativeQuery = true)
    void resetDrawing(String drawCode);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO khr_drawing_items SELECT * FROM khr_temp_drawing_items WHERE drawing_id = (SELECT id FROM khr_temp_drawing WHERE code = :drawCode)", nativeQuery = true)
    void saveDrawingItems(String drawCode);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM khr_drawing_items WHERE drawing_id = (SELECT id FROM khr_temp_drawing WHERE code = :drawCode)", nativeQuery = true)
    void resetDrawingItems(String drawCode);

    @Transactional
    @Modifying
    @Query(value = "UPDATE khr_temp_drawing SET `status` = 'AWARDED' WHERE `code` = :drawCode ", nativeQuery = true)
    void updateDrawAwarded(String drawCode);

    Boolean existsByCode(String code);
}
