UPDATE summery_daily
SET total_turnover_khr=0,
    total_turnover_usd=0,
    total_reward_khr=0,
    total_reward_usd=0,
    detail = null
WHERE lottery_type = :lotteryType AND date(issued_at) = date(:drawAt)