package com.hacknovation.systemservice.v1_0_0.io.repo.leap;

import com.hacknovation.systemservice.v1_0_0.io.entity.leap.LeapTempDrawingEntity;
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
public interface LeapTempDrawingRP extends JpaRepository<LeapTempDrawingEntity, Long> {

    LeapTempDrawingEntity findByCode(String code);

    @Query(value = "SELECT * FROM leap_temp_drawing  ORDER BY id DESC LIMIT 1", nativeQuery = true)
    LeapTempDrawingEntity lastDraw();

    @Query(value = "SELECT * FROM leap_temp_drawing WHERE `is_recent` = 1 ORDER BY id ASC LIMIT 1", nativeQuery = true)
    LeapTempDrawingEntity recentDrawing();

    @Query(value = "SELECT * FROM leap_temp_drawing WHERE CONVERT_TZ(resulted_post_at, '+0:00', '+7:00') = :drawAt ORDER BY id ASC LIMIT 1", nativeQuery = true)
    LeapTempDrawingEntity getDrawingByDrawAt(String drawAt);

    @Transactional
    @Modifying
    @Query(value = "UPDATE transaction_balance tb, " +
            "(SELECT " +
            "    vtoi.mc userCode, " +
            "    SUM(IF(vtoi.cc = 'KHR', vtoi.bet_amount * vtoi.member_rebate_rate * vtwoi.wq, 0)) rewardAmountKhr, " +
            "    SUM(IF(vtoi.cc = 'USD', vtoi.bet_amount * vtoi.member_rebate_rate * vtwoi.wq, 0)) rewardAmountUsd " +
            "FROM  leap_temp_order_items vtoi " +
            "INNER JOIN  leap_temp_win_order_items vtwoi ON vtoi.id = vtwoi.oii " +
            "INNER JOIN users u ON u.`code` = vtoi.mc AND u.is_online IS TRUE " +
            "WHERE vtwoi.dc = :drawCode " +
            "GROUP BY mc) win " +
            "SET tb.balance_khr = tb.balance_khr - win.rewardAmountKhr, " +
            "    tb.balance_usd = tb.balance_usd - win.rewardAmountUsd " +
            "WHERE tb.user_code=win.userCode ", nativeQuery = true)
    void updateBalanceMemberOnlineByDrawCode(String drawCode);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO leap_drawing SELECT * FROM leap_temp_drawing WHERE code = :drawCode", nativeQuery = true)
    void saveDrawing(String drawCode);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM leap_drawing WHERE code = :drawCode", nativeQuery = true)
    void resetDrawing(String drawCode);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO leap_drawing_items SELECT * FROM leap_temp_drawing_items WHERE drawing_id = (SELECT id FROM leap_temp_drawing WHERE code = :drawCode)", nativeQuery = true)
    void saveDrawingItems(String drawCode);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM leap_drawing_items WHERE drawing_id = (SELECT id FROM leap_temp_drawing WHERE code = :drawCode)", nativeQuery = true)
    void resetDrawingItems(String drawCode);

    @Transactional
    @Modifying
    @Query(value = "UPDATE leap_temp_drawing SET `status` = 'AWARDED' WHERE `code` = :drawCode ", nativeQuery = true)
    void updateDrawAwarded(String drawCode);

    Boolean existsByCode(String code);
}
