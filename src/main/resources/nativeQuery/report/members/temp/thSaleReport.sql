SELECT userCode,
       drawCode,
       drawAt,
       orderId,
       0.0 as                                         com1DUsd,
       0.0 as                                         com1DKhr,
       CAST(SUM(com2DUsd) AS DECIMAL(15, 2))          com2DUsd,
       CAST(SUM(com2DKhr) AS DECIMAL(15, 2))          com2DKhr,
       CAST(SUM(com3DUsd) AS DECIMAL(15, 2))          com3DUsd,
       CAST(SUM(com3DKhr) AS DECIMAL(15, 2))          com3DKhr,
       CAST(SUM(com4DUsd) AS DECIMAL(15, 2))          com4DUsd,
       CAST(SUM(com4DKhr) AS DECIMAL(15, 2))          com4DKhr,

       0.0 as                                         betAmount1DUsd,
       0.0 as                                         betAmount1DKhr,
       CAST(SUM(betAmount2DUsd) AS DECIMAL(15, 2))    betAmount2DUsd,
       CAST(SUM(betAmount2DKhr) AS DECIMAL(15, 2))    betAmount2DKhr,
       CAST(SUM(betAmount3DUsd) AS DECIMAL(15, 2))    betAmount3DUsd,
       CAST(SUM(betAmount3DKhr) AS DECIMAL(15, 2))    betAmount3DKhr,
       CAST(SUM(betAmount4DUsd) AS DECIMAL(15, 2))    betAmount4DUsd,
       CAST(SUM(betAmount4DKhr) AS DECIMAL(15, 2))    betAmount4DKhr,

       0.0                                            winAmount1DUsd,
       0.0                                            winAmount1DKhr,
       CAST(SUM(winAmount2DUsd) AS DECIMAL(15, 2))    winAmount2DUsd,
       CAST(SUM(winAmount2DKhr) AS DECIMAL(15, 2))    winAmount2DKhr,
       CAST(SUM(winAmount3DUsd) AS DECIMAL(15, 2))    winAmount3DUsd,
       CAST(SUM(winAmount3DKhr) AS DECIMAL(15, 2))    winAmount3DKhr,
       CAST(SUM(winAmount4DUsd) AS DECIMAL(15, 2))    winAmount4DUsd,
       CAST(SUM(winAmount4DKhr) AS DECIMAL(15, 2))    winAmount4DKhr,

       0.0 AS                                         rewardAmount1DUsd,
       0.0 AS                                         rewardAmount1DKhr,
       CAST(SUM(rewardAmount2DUsd) AS DECIMAL(15, 2)) rewardAmount2DUsd,
       CAST(SUM(rewardAmount3DUsd) AS DECIMAL(15, 2)) rewardAmount3DUsd,
       CAST(SUM(rewardAmount2DKhr) AS DECIMAL(15, 2)) rewardAmount2DKhr,
       CAST(SUM(rewardAmount3DKhr) AS DECIMAL(15, 2)) rewardAmount3DKhr,
       CAST(SUM(rewardAmount4DUsd) AS DECIMAL(15, 2)) rewardAmount4DUsd,
       CAST(SUM(rewardAmount4DKhr) AS DECIMAL(15, 2)) rewardAmount4DKhr
FROM (
         SELECT
             oi.mc userCode,
             oi.dc drawCode,
             oi.draw_at drawAt,
             oi.oi orderId,

             SUM(IF(oi.cc = 'USD' AND oi.rc = '2D', oi.total_amount * oi.member_commission / 100, 0)) com2DUsd,
             SUM(IF(oi.cc = 'USD' AND oi.rc = '3D', oi.total_amount * oi.member_commission / 100, 0)) com3DUsd,
             SUM(IF(oi.cc = 'USD' AND oi.rc = '4D', oi.total_amount * oi.member_commission / 100, 0)) com4DUsd,

             SUM(IF(oi.cc = 'KHR' AND oi.rc = '2D', oi.total_amount * oi.member_commission / 100, 0)) com2DKhr,
             SUM(IF(oi.cc = 'KHR' AND oi.rc = '3D', oi.total_amount * oi.member_commission / 100, 0)) com3DKhr,
             SUM(IF(oi.cc = 'KHR' AND oi.rc = '4D', oi.total_amount * oi.member_commission / 100, 0)) com4DKhr,

             SUM(IF(oi.cc = 'USD' AND oi.rc = '2D', oi.total_amount, 0))                              betAmount2DUsd,
             SUM(IF(oi.cc = 'USD' AND oi.rc = '3D', oi.total_amount, 0))                              betAmount3DUsd,
             SUM(IF(oi.cc = 'USD' AND oi.rc = '4D', oi.total_amount, 0))                              betAmount4DUsd,

             SUM(IF(oi.cc = 'KHR' AND oi.rc = '2D', oi.total_amount, 0))                              betAmount2DKhr,
             SUM(IF(oi.cc = 'KHR' AND oi.rc = '3D', oi.total_amount, 0))                              betAmount3DKhr,
             SUM(IF(oi.cc = 'KHR' AND oi.rc = '4D', oi.total_amount, 0))                              betAmount4DKhr,

             SUM(IF(oi.cc = 'USD' AND oi.rc = '2D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount2DUsd,
             SUM(IF(oi.cc = 'USD' AND oi.rc = '3D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount3DUsd,
             SUM(IF(oi.cc = 'USD' AND oi.rc = '4D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount4DUsd,

             SUM(IF(oi.cc = 'KHR' AND oi.rc = '2D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount2DKhr,
             SUM(IF(oi.cc = 'KHR' AND oi.rc = '3D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount3DKhr,
             SUM(IF(oi.cc = 'KHR' AND oi.rc = '4D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount4DKhr,

             SUM(IF(oi.cc = 'USD' AND oi.rc = '2D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0),0)) rewardAmount2DUsd,
             SUM(IF(oi.cc = 'USD' AND oi.rc = '3D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0),0)) rewardAmount3DUsd,
             SUM(IF(oi.cc = 'USD' AND oi.rc = '4D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0),0)) rewardAmount4DUsd,

             SUM(IF(oi.cc = 'KHR' AND oi.rc = '2D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0),0)) rewardAmount2DKhr,
             SUM(IF(oi.cc = 'KHR' AND oi.rc = '3D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0)) rewardAmount3DKhr,
             SUM(IF(oi.cc = 'KHR' AND oi.rc = '4D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0)) rewardAmount4DKhr

         FROM th_temp_order_items oi
                  LEFT JOIN (SELECT oii, wq FROM th_temp_win_order_items) lwoi ON lwoi.oii = oi.id
                  left join th_temp_orders o on o.id = oi.oi
         WHERE DATE(oi.draw_at) BETWEEN DATE(:filterByStartDate) AND DATE(:filterByEndDate) AND oi.mc IN :memberCodes
         and o.status != 0
         GROUP BY oi.dc, oi.mc
         ) q1
GROUP BY q1.drawCode, q1.userCode
ORDER BY q1.drawCode, q1.userCode ASC