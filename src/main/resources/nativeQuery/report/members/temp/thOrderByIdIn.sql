SELECT id,
       uc userCode,
       dc drawCode,
       draw_at drawAt,
       has_lottery_id hasLotteryId
FROM th_temp_orders WHERE id IN :orderIds