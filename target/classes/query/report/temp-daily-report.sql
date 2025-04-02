SELECT lottery,
       userCode,
       drawCode,
       drawAt,
       orderId,
       SUM(com1DUsd)          as com1DUsd,
       SUM(com1DKhr)          as com1DKhr,
       SUM(com2DUsd)          as com2DUsd,
       SUM(com2DKhr)          as com2DKhr,
       SUM(com3DUsd)          as com3DUsd,
       SUM(com3DKhr)          as com3DKhr,
       SUM(com4DUsd)          as com4DUsd,
       SUM(com4DKhr)          as com4DKhr,

       SUM(betAmount1DUsd)    AS betAmount1DUsd,
       SUM(betAmount2DUsd)    AS betAmount2DUsd,
       SUM(betAmount3DUsd)    AS betAmount3DUsd,
       SUM(betAmount4DUsd)    AS betAmount4DUsd,
       SUM(betAmount1DKhr)    AS betAmount1DKhr,
       SUM(betAmount2DKhr)    AS betAmount2DKhr,
       SUM(betAmount3DKhr)    AS betAmount3DKhr,
       SUM(betAmount4DKhr)    AS betAmount4DKhr,

       SUM(winAmount1DUsd)    AS winAmount1DUsd,
       SUM(winAmount2DUsd)    AS winAmount2DUsd,
       SUM(winAmount3DUsd)    AS winAmount3DUsd,
       SUM(winAmount4DUsd)    AS winAmount4DUsd,
       SUM(winAmount1DKhr)    AS winAmount1DKhr,
       SUM(winAmount2DKhr)    AS winAmount2DKhr,
       SUM(winAmount3DKhr)    AS winAmount3DKhr,
       SUM(winAmount4DKhr)    AS winAmount4DKhr,

       SUM(rewardAmount1DUsd) AS rewardAmount1DUsd,
       SUM(rewardAmount2DUsd) AS rewardAmount2DUsd,
       SUM(rewardAmount3DUsd) AS rewardAmount3DUsd,
       SUM(rewardAmount4DUsd) AS rewardAmount4DUsd,
       SUM(rewardAmount1DKhr) AS rewardAmount1DKhr,
       SUM(rewardAmount2DKhr) AS rewardAmount2DKhr,
       SUM(rewardAmount3DKhr) AS rewardAmount3DKhr,
       SUM(rewardAmount4DKhr) AS rewardAmount4DKhr,
       has_lottery_id hasLotteryId

FROM (

         SELECT 'VN1' as                                                                                 lottery,
                oi.mc                                                                                    userCode,
                oi.dc                                                                                    drawCode,
                oi.draw_at                                                                               drawAt,
                oi.oi                                                                                    orderId,

                SUM(IF(oi.cc = 'USD' AND oi.rc = '1D', oi.total_amount * oi.member_commission / 100, 0)) com1DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '2D', oi.total_amount * oi.member_commission / 100, 0)) com2DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '3D', oi.total_amount * oi.member_commission / 100, 0)) com3DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '4D', oi.total_amount * oi.member_commission / 100, 0)) com4DUsd,

                SUM(IF(oi.cc = 'KHR' AND oi.rc = '1D', oi.total_amount * oi.member_commission / 100, 0)) com1DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '2D', oi.total_amount * oi.member_commission / 100, 0)) com2DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '3D', oi.total_amount * oi.member_commission / 100, 0)) com3DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '4D', oi.total_amount * oi.member_commission / 100, 0)) com4DKhr,

                SUM(IF(oi.cc = 'USD' AND oi.rc = '1D', oi.total_amount, 0))                              betAmount1DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '2D', oi.total_amount, 0))                              betAmount2DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '3D', oi.total_amount, 0))                              betAmount3DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '4D', oi.total_amount, 0))                              betAmount4DUsd,

                SUM(IF(oi.cc = 'KHR' AND oi.rc = '1D', oi.total_amount, 0))                              betAmount1DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '2D', oi.total_amount, 0))                              betAmount2DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '3D', oi.total_amount, 0))                              betAmount3DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '4D', oi.total_amount, 0))                              betAmount4DKhr,

                SUM(IF(oi.cc = 'USD' AND oi.rc = '1D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount1DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '2D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount2DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '3D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount3DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '4D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount4DUsd,

                SUM(IF(oi.cc = 'KHR' AND oi.rc = '1D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount1DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '2D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount2DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '3D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount3DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '4D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount4DKhr,

                SUM(IF(oi.cc = 'USD' AND oi.rc = '1D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount1DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '2D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount2DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '3D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount3DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '4D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount4DUsd,

                SUM(IF(oi.cc = 'KHR' AND oi.rc = '1D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount1DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '2D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount2DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '3D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount3DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '4D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount4DKhr,
                o.has_lottery_id

         from vnone_temp_order_items oi
                  left join vnone_temp_win_order_items lwoi ON lwoi.oii = oi.id
                  left join vnone_temp_orders o on o.id = oi.oi
         where date(oi.draw_at) between :startDate and :endDate
           and oi.mc in (:memberCodes)
           and o.status != 0
         group by o.dc, o.uc, o.has_lottery_id

         union all

         SELECT 'VN2' as                                                                                 lottery,
                oi.mc                                                                                    userCode,
                oi.dc                                                                                    drawCode,
                oi.draw_at                                                                               drawAt,
                oi.oi                                                                                    orderId,

                SUM(IF(oi.cc = 'USD' AND oi.rc = '1D', oi.total_amount * oi.member_commission / 100, 0)) com1DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '2D', oi.total_amount * oi.member_commission / 100, 0)) com2DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '3D', oi.total_amount * oi.member_commission / 100, 0)) com3DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '4D', oi.total_amount * oi.member_commission / 100, 0)) com4DUsd,

                SUM(IF(oi.cc = 'KHR' AND oi.rc = '1D', oi.total_amount * oi.member_commission / 100, 0)) com1DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '2D', oi.total_amount * oi.member_commission / 100, 0)) com2DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '3D', oi.total_amount * oi.member_commission / 100, 0)) com3DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '4D', oi.total_amount * oi.member_commission / 100, 0)) com4DKhr,

                SUM(IF(oi.cc = 'USD' AND oi.rc = '1D', oi.total_amount, 0))                              betAmount1DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '2D', oi.total_amount, 0))                              betAmount2DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '3D', oi.total_amount, 0))                              betAmount3DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '4D', oi.total_amount, 0))                              betAmount4DUsd,

                SUM(IF(oi.cc = 'KHR' AND oi.rc = '1D', oi.total_amount, 0))                              betAmount1DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '2D', oi.total_amount, 0))                              betAmount2DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '3D', oi.total_amount, 0))                              betAmount3DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '4D', oi.total_amount, 0))                              betAmount4DKhr,

                SUM(IF(oi.cc = 'USD' AND oi.rc = '1D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount1DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '2D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount2DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '3D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount3DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '4D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount4DUsd,

                SUM(IF(oi.cc = 'KHR' AND oi.rc = '1D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount1DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '2D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount2DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '3D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount3DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '4D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount4DKhr,

                SUM(IF(oi.cc = 'USD' AND oi.rc = '1D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount1DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '2D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount2DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '3D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount3DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '4D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount4DUsd,

                SUM(IF(oi.cc = 'KHR' AND oi.rc = '1D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount1DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '2D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount2DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '3D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount3DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '4D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount4DKhr,
                o.has_lottery_id

         from vntwo_temp_order_items oi
                  left join vntwo_temp_win_order_items lwoi ON lwoi.oii = oi.id
                  left join vntwo_temp_orders o on o.id = oi.oi
         where date(oi.draw_at) between :startDate and :endDate
           and oi.mc in (:memberCodes)
           and o.status != 0
         group by o.dc, o.uc, o.has_lottery_id

         union all

         SELECT 'LEAP' as                                                                                 lottery,
                oi.mc                                                                                    userCode,
                oi.dc                                                                                    drawCode,
                oi.draw_at                                                                               drawAt,
                oi.oi                                                                                    orderId,

                SUM(IF(oi.cc = 'USD' AND oi.rc = '1D', oi.total_amount * oi.member_commission / 100, 0)) com1DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '2D', oi.total_amount * oi.member_commission / 100, 0)) com2DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '3D', oi.total_amount * oi.member_commission / 100, 0)) com3DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '4D', oi.total_amount * oi.member_commission / 100, 0)) com4DUsd,

                SUM(IF(oi.cc = 'KHR' AND oi.rc = '1D', oi.total_amount * oi.member_commission / 100, 0)) com1DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '2D', oi.total_amount * oi.member_commission / 100, 0)) com2DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '3D', oi.total_amount * oi.member_commission / 100, 0)) com3DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '4D', oi.total_amount * oi.member_commission / 100, 0)) com4DKhr,

                SUM(IF(oi.cc = 'USD' AND oi.rc = '1D', oi.total_amount, 0))                              betAmount1DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '2D', oi.total_amount, 0))                              betAmount2DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '3D', oi.total_amount, 0))                              betAmount3DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '4D', oi.total_amount, 0))                              betAmount4DUsd,

                SUM(IF(oi.cc = 'KHR' AND oi.rc = '1D', oi.total_amount, 0))                              betAmount1DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '2D', oi.total_amount, 0))                              betAmount2DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '3D', oi.total_amount, 0))                              betAmount3DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '4D', oi.total_amount, 0))                              betAmount4DKhr,

                SUM(IF(oi.cc = 'USD' AND oi.rc = '1D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount1DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '2D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount2DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '3D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount3DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '4D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount4DUsd,

                SUM(IF(oi.cc = 'KHR' AND oi.rc = '1D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount1DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '2D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount2DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '3D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount3DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '4D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount4DKhr,

                SUM(IF(oi.cc = 'USD' AND oi.rc = '1D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount1DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '2D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount2DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '3D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount3DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '4D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount4DUsd,

                SUM(IF(oi.cc = 'KHR' AND oi.rc = '1D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount1DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '2D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount2DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '3D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount3DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '4D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount4DKhr,
                o.has_lottery_id

         from leap_temp_order_items oi
                  left join leap_temp_win_order_items lwoi ON lwoi.oii = oi.id
                  left join leap_temp_orders o on o.id = oi.oi
         where date(oi.draw_at) between :startDate and :endDate
           and oi.mc in (:memberCodes)
           and o.status != 0
         group by o.dc, o.uc, o.has_lottery_id

         union all

         SELECT 'KH' as                                                                                 lottery,
                oi.mc                                                                                    userCode,
                oi.dc                                                                                    drawCode,
                oi.draw_at                                                                               drawAt,
                oi.oi                                                                                    orderId,

                SUM(IF(oi.cc = 'USD' AND oi.rc = '1D', oi.total_amount * oi.member_commission / 100, 0)) com1DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '2D', oi.total_amount * oi.member_commission / 100, 0)) com2DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '3D', oi.total_amount * oi.member_commission / 100, 0)) com3DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '4D', oi.total_amount * oi.member_commission / 100, 0)) com4DUsd,

                SUM(IF(oi.cc = 'KHR' AND oi.rc = '1D', oi.total_amount * oi.member_commission / 100, 0)) com1DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '2D', oi.total_amount * oi.member_commission / 100, 0)) com2DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '3D', oi.total_amount * oi.member_commission / 100, 0)) com3DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '4D', oi.total_amount * oi.member_commission / 100, 0)) com4DKhr,

                SUM(IF(oi.cc = 'USD' AND oi.rc = '1D', oi.total_amount, 0))                              betAmount1DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '2D', oi.total_amount, 0))                              betAmount2DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '3D', oi.total_amount, 0))                              betAmount3DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '4D', oi.total_amount, 0))                              betAmount4DUsd,

                SUM(IF(oi.cc = 'KHR' AND oi.rc = '1D', oi.total_amount, 0))                              betAmount1DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '2D', oi.total_amount, 0))                              betAmount2DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '3D', oi.total_amount, 0))                              betAmount3DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '4D', oi.total_amount, 0))                              betAmount4DKhr,

                SUM(IF(oi.cc = 'USD' AND oi.rc = '1D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount1DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '2D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount2DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '3D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount3DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '4D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount4DUsd,

                SUM(IF(oi.cc = 'KHR' AND oi.rc = '1D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount1DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '2D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount2DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '3D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount3DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '4D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount4DKhr,

                SUM(IF(oi.cc = 'USD' AND oi.rc = '1D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount1DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '2D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount2DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '3D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount3DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '4D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount4DUsd,

                SUM(IF(oi.cc = 'KHR' AND oi.rc = '1D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount1DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '2D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount2DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '3D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount3DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '4D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount4DKhr,
                o.has_lottery_id

         from khr_temp_order_items oi
                  left join khr_temp_win_order_items lwoi ON lwoi.oii = oi.id
                  left join khr_temp_orders o on o.id = oi.oi
         where date(oi.draw_at) between :startDate and :endDate
           and oi.mc in (:memberCodes)
           and o.status != 0
         group by o.dc, o.uc, o.has_lottery_id

         union all

         SELECT 'SC' as                                                                                 lottery,
                oi.mc                                                                                    userCode,
                oi.dc                                                                                    drawCode,
                oi.draw_at                                                                               drawAt,
                oi.oi                                                                                    orderId,

                SUM(IF(oi.cc = 'USD' AND oi.rc = '1D', oi.total_amount * oi.member_commission / 100, 0)) com1DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '2D', oi.total_amount * oi.member_commission / 100, 0)) com2DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '3D', oi.total_amount * oi.member_commission / 100, 0)) com3DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '4D', oi.total_amount * oi.member_commission / 100, 0)) com4DUsd,

                SUM(IF(oi.cc = 'KHR' AND oi.rc = '1D', oi.total_amount * oi.member_commission / 100, 0)) com1DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '2D', oi.total_amount * oi.member_commission / 100, 0)) com2DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '3D', oi.total_amount * oi.member_commission / 100, 0)) com3DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '4D', oi.total_amount * oi.member_commission / 100, 0)) com4DKhr,

                SUM(IF(oi.cc = 'USD' AND oi.rc = '1D', oi.total_amount, 0))                              betAmount1DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '2D', oi.total_amount, 0))                              betAmount2DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '3D', oi.total_amount, 0))                              betAmount3DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '4D', oi.total_amount, 0))                              betAmount4DUsd,

                SUM(IF(oi.cc = 'KHR' AND oi.rc = '1D', oi.total_amount, 0))                              betAmount1DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '2D', oi.total_amount, 0))                              betAmount2DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '3D', oi.total_amount, 0))                              betAmount3DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '4D', oi.total_amount, 0))                              betAmount4DKhr,

                SUM(IF(oi.cc = 'USD' AND oi.rc = '1D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount1DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '2D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount2DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '3D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount3DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '4D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount4DUsd,

                SUM(IF(oi.cc = 'KHR' AND oi.rc = '1D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount1DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '2D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount2DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '3D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount3DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '4D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount4DKhr,

                SUM(IF(oi.cc = 'USD' AND oi.rc = '1D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount1DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '2D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount2DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '3D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount3DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '4D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount4DUsd,

                SUM(IF(oi.cc = 'KHR' AND oi.rc = '1D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount1DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '2D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount2DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '3D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount3DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '4D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount4DKhr,
                o.has_lottery_id

         from sc_temp_order_items oi
                  left join sc_temp_win_order_items lwoi ON lwoi.oii = oi.id
                  left join sc_temp_orders o on o.id = oi.oi
         where date(oi.draw_at) between :startDate and :endDate
           and oi.mc in (:memberCodes)
           and o.status != 0
         group by o.dc, o.uc, o.has_lottery_id

         union all

         SELECT 'TN' as                                                                                 lottery,
                oi.mc                                                                                    userCode,
                oi.dc                                                                                    drawCode,
                oi.draw_at                                                                               drawAt,
                oi.oi                                                                                    orderId,

                SUM(IF(oi.cc = 'USD' AND oi.rc = '1D', oi.total_amount * oi.member_commission / 100, 0)) com1DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '2D', oi.total_amount * oi.member_commission / 100, 0)) com2DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '3D', oi.total_amount * oi.member_commission / 100, 0)) com3DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '4D', oi.total_amount * oi.member_commission / 100, 0)) com4DUsd,

                SUM(IF(oi.cc = 'KHR' AND oi.rc = '1D', oi.total_amount * oi.member_commission / 100, 0)) com1DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '2D', oi.total_amount * oi.member_commission / 100, 0)) com2DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '3D', oi.total_amount * oi.member_commission / 100, 0)) com3DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '4D', oi.total_amount * oi.member_commission / 100, 0)) com4DKhr,

                SUM(IF(oi.cc = 'USD' AND oi.rc = '1D', oi.total_amount, 0))                              betAmount1DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '2D', oi.total_amount, 0))                              betAmount2DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '3D', oi.total_amount, 0))                              betAmount3DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '4D', oi.total_amount, 0))                              betAmount4DUsd,

                SUM(IF(oi.cc = 'KHR' AND oi.rc = '1D', oi.total_amount, 0))                              betAmount1DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '2D', oi.total_amount, 0))                              betAmount2DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '3D', oi.total_amount, 0))                              betAmount3DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '4D', oi.total_amount, 0))                              betAmount4DKhr,

                SUM(IF(oi.cc = 'USD' AND oi.rc = '1D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount1DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '2D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount2DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '3D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount3DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '4D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount4DUsd,

                SUM(IF(oi.cc = 'KHR' AND oi.rc = '1D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount1DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '2D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount2DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '3D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount3DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '4D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount4DKhr,

                SUM(IF(oi.cc = 'USD' AND oi.rc = '1D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount1DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '2D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount2DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '3D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount3DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '4D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount4DUsd,

                SUM(IF(oi.cc = 'KHR' AND oi.rc = '1D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount1DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '2D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount2DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '3D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount3DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '4D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount4DKhr,
                o.has_lottery_id

         from tn_temp_order_items oi
                  left join tn_temp_win_order_items lwoi ON lwoi.oii = oi.id
                  left join tn_temp_orders o on o.id = oi.oi
         where date(oi.draw_at) between :startDate and :endDate
           and oi.mc in (:memberCodes)
           and o.status != 0
         group by o.dc, o.uc, o.has_lottery_id

         union all

         SELECT 'TH' as                                                                                 lottery,
                oi.mc                                                                                    userCode,
                oi.dc                                                                                    drawCode,
                oi.draw_at                                                                               drawAt,
                oi.oi                                                                                    orderId,

                SUM(IF(oi.cc = 'USD' AND oi.rc = '1D', oi.total_amount * oi.member_commission / 100, 0)) com1DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '2D', oi.total_amount * oi.member_commission / 100, 0)) com2DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '3D', oi.total_amount * oi.member_commission / 100, 0)) com3DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '4D', oi.total_amount * oi.member_commission / 100, 0)) com4DUsd,

                SUM(IF(oi.cc = 'KHR' AND oi.rc = '1D', oi.total_amount * oi.member_commission / 100, 0)) com1DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '2D', oi.total_amount * oi.member_commission / 100, 0)) com2DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '3D', oi.total_amount * oi.member_commission / 100, 0)) com3DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '4D', oi.total_amount * oi.member_commission / 100, 0)) com4DKhr,

                SUM(IF(oi.cc = 'USD' AND oi.rc = '1D', oi.total_amount, 0))                              betAmount1DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '2D', oi.total_amount, 0))                              betAmount2DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '3D', oi.total_amount, 0))                              betAmount3DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '4D', oi.total_amount, 0))                              betAmount4DUsd,

                SUM(IF(oi.cc = 'KHR' AND oi.rc = '1D', oi.total_amount, 0))                              betAmount1DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '2D', oi.total_amount, 0))                              betAmount2DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '3D', oi.total_amount, 0))                              betAmount3DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '4D', oi.total_amount, 0))                              betAmount4DKhr,

                SUM(IF(oi.cc = 'USD' AND oi.rc = '1D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount1DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '2D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount2DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '3D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount3DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '4D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount4DUsd,

                SUM(IF(oi.cc = 'KHR' AND oi.rc = '1D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount1DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '2D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount2DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '3D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount3DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '4D', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0))           winAmount4DKhr,

                SUM(IF(oi.cc = 'USD' AND oi.rc = '1D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount1DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '2D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount2DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '3D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount3DUsd,
                SUM(IF(oi.cc = 'USD' AND oi.rc = '4D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount4DUsd,

                SUM(IF(oi.cc = 'KHR' AND oi.rc = '1D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount1DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '2D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount2DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '3D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount3DKhr,
                SUM(IF(oi.cc = 'KHR' AND oi.rc = '4D', IFNULL(lwoi.wq, 0) * oi.bet_amount * IFNULL(oi.member_rebate_rate, 0), 0))        rewardAmount4DKhr,
                o.has_lottery_id

         from th_temp_order_items oi
                  left join th_temp_win_order_items lwoi ON lwoi.oii = oi.id
                  left join th_temp_orders o on o.id = oi.oi
         where date(oi.draw_at) between :startDate and :endDate
           and o.status != 0
         group by o.dc, o.uc, o.has_lottery_id

      ) res
group by res.lottery, res.drawCode, res.userCode, res.has_lottery_id
order by res.drawCode, res.userCode