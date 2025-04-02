package com.hacknovation.systemservice.v1_0_0.io.repo.vnone;

import com.hacknovation.systemservice.v1_0_0.io.entity.vnone.VNOneTempDrawingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * author: kangto
 * createdAt: 21/01/2022
 * time: 22:28
 */
@Repository
public interface VNOneTempDrawingRP extends JpaRepository<VNOneTempDrawingEntity, Long> {

    VNOneTempDrawingEntity findByCode(String code);

    @Query(value = "SELECT * FROM vnone_temp_drawing ORDER BY id DESC LIMIT 1", nativeQuery = true)
    VNOneTempDrawingEntity lastDraw();

    @Query(value = "SELECT * FROM vnone_temp_drawing WHERE `is_recent` = 1 ORDER BY resulted_post_at ASC LIMIT 1", nativeQuery = true)
    VNOneTempDrawingEntity recentDrawing();

    @Transactional
    @Modifying
    @Query(value = "UPDATE transaction_balance tb, " +
            "(SELECT " +
            "    vtoi.mc userCode, " +
            "    SUM(IF(vtoi.cc = 'KHR', vtoi.bet_amount * vtoi.member_rebate_rate * vtwoi.wq, 0)) rewardAmountKhr, " +
            "    SUM(IF(vtoi.cc = 'USD', vtoi.bet_amount * vtoi.member_rebate_rate * vtwoi.wq, 0)) rewardAmountUsd " +
            "FROM vnone_temp_order_items vtoi " +
            "INNER JOIN vnone_temp_win_order_items vtwoi ON vtoi.id = vtwoi.oii " +
            "INNER JOIN users u ON u.`code` = vtoi.mc AND u.is_online IS TRUE " +
            "WHERE vtwoi.dc = :drawCode " +
            "GROUP BY mc) win " +
            "SET tb.balance_khr = tb.balance_khr - win.rewardAmountKhr, " +
            "    tb.balance_usd = tb.balance_usd - win.rewardAmountUsd " +
            "WHERE tb.user_code=win.userCode ", nativeQuery = true)
    void updateBalanceMemberOnlineByDrawCode(String drawCode);

    @Transactional
    @Modifying
    @Query(value = "update vnone_temp_drawing set is_recent = :isRecent, is_set_win = 0  where code = :code", nativeQuery = true)
    void updateIsRecent(Integer isRecent, String code);

    Long countByIsRecentFalseAndCode(String code);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO vnone_drawing SELECT * FROM vnone_temp_drawing WHERE code = :drawCode", nativeQuery = true)
    void saveDrawing(String drawCode);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM vnone_drawing WHERE code = :drawCode", nativeQuery = true)
    void resetDrawing(String drawCode);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO vnone_drawing_items SELECT * FROM vnone_temp_drawing_items WHERE drawing_id = (SELECT id FROM vnone_temp_drawing WHERE code = :drawCode)", nativeQuery = true)
    void saveDrawingItems(String drawCode);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM vnone_drawing_items WHERE drawing_id = (SELECT id FROM vnone_temp_drawing WHERE code = :drawCode)", nativeQuery = true)
    void resetDrawingItems(String drawCode);

    @Transactional
    @Modifying
    @Query(value = "UPDATE vnone_temp_drawing SET `status` = 'AWARDED' WHERE `code` = :drawCode ", nativeQuery = true)
    void updateDrawAwarded(String drawCode);
}
