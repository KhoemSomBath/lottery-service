SELECT id,
       uc userCode,
       dc drawCode,
       draw_at drawAt,
       has_lottery_id hasLotteryId
FROM vnone_temp_orders WHERE id IN :orderIds