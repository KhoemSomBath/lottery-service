package com.hacknovation.systemservice.v1_0_0.io.repo.th;

import com.hacknovation.systemservice.v1_0_0.io.entity.th.THTempWinOrderItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/*
 * author: kangto
 * createdAt: 08/05/2023
 * time: 16:06
 */
@Repository
public interface THTempWinOrderItemRP extends JpaRepository<THTempWinOrderItemsEntity, Long> {


    @Transactional
    @Modifying
    @Query(value = "UPDATE transaction_balance tb, " +
            "(SELECT " +
            "    vtoi.mc userCode, " +
            "    SUM(IF(vtoi.cc = 'KHR', vtoi.bet_amount * vtoi.member_rebate_rate * vtwoi.wq, 0)) rewardAmountKhr, " +
            "    SUM(IF(vtoi.cc = 'USD', vtoi.bet_amount * vtoi.member_rebate_rate * vtwoi.wq, 0)) rewardAmountUsd " +
            "FROM th_temp_order_items vtoi " +
            "INNER JOIN th_temp_win_order_items vtwoi ON vtoi.id = vtwoi.oii " +
            "INNER JOIN users u ON u.`code` = vtoi.mc AND u.is_online IS TRUE " +
            "WHERE vtwoi.dc = :drawCode " +
            "GROUP BY mc) win " +
            "SET tb.balance_khr = tb.balance_khr + win.rewardAmountKhr, " +
            "    tb.balance_usd = tb.balance_usd + win.rewardAmountUsd " +
            "WHERE tb.user_code=win.userCode ", nativeQuery = true)
    void updateBalanceMemberOnlineByDrawCode(String drawCode);

}
