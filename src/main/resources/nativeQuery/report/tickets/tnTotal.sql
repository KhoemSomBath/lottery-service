SELECT sum(if(oi.cc = 'khr', oi.total_amount, 0))                                                             betAmountKhr,
       sum(if(oi.cc = 'khr', oi.total_amount * oi.member_commission / 100, 0))                                waterAmountKhr,
       sum(if(oi.cc = 'khr', if(oiw.id is not null, oi.bet_amount * oiw.wq * oi.member_rebate_rate, 0), 0)) winAmountKhr,
       sum(if(oi.cc = 'khr', oi.total_amount * oi.member_commission / 100, 0)) - sum(if(oi.cc = 'khr', if(oiw.id is not null, oi.bet_amount * oiw.wq * oi.member_rebate_rate, 0),0)) winLoseAmountKhr,
       sum(if(oi.cc = 'usd', oi.total_amount, 0))                                                             betAmountUsd,
       sum(if(oi.cc = 'usd', oi.total_amount * oi.member_commission / 100, 0))                                waterAmountUsd,
       sum(if(oi.cc = 'usd', if(oiw.id is not null, oi.total_amount * oiw.wq * oi.member_rebate_rate, 0), 0)) winAmountUsd,
       sum(if(oi.cc = 'usd', oi.total_amount * oi.member_commission / 100, 0)) - sum(if(oi.cc = 'usd', if(oiw.id is not null, oi.bet_amount * oiw.wq * oi.member_rebate_rate, 0),0)) winLoseAmountUsd
FROM tn_temp_orders o
         LEFT JOIN tn_order_items oi ON o.id = oi.oi
         LEFT JOIN tn_win_order_items oiw ON oi.id = oiw.oii
         LEFT JOIN users u ON u.code = o.uc
WHERE IF(:drawCode <> 'all', o.dc = :drawCode, TRUE)
  AND IF(lower(:isCancel) <> 'false', o.status = 0 , o.status != 0)
  AND IF(:isLevel, o.uc IN :memberCodes, TRUE)
  and if(:userOnline, true, u.is_online = 0)
  AND DATE (o.draw_at) = DATE (:filterByDate)
  AND IF(:keyword <> 'all', (u.username LIKE concat('%' ,:keyword ,'%') OR o.tn LIKE concat('%' ,:keyword ,'%') OR o.dc LIKE concat('%' ,:keyword ,'%')), TRUE)
  AND IF(lower(:isWin) <> 'false', oiw.wq > 0 , TRUE)