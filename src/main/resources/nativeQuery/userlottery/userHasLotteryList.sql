SELECT
	r.code AS originRebateCode,
	r.name AS originRebateName,
	r.rebate_rate AS originRebateRate,
	r.water_rate AS originWaterRate,
	r.type AS originLotteryCode,
	uhl.id,
	uhl.uc AS userCode,
    r.type AS lotteryCode,
    r.code AS rebateCode,
	IFNULL(uhl.rebate_rate, r.rebate_rate) AS rebateRate,
	uhl.water_rate AS waterRate,
	uhl.share,
	IFNULL(uhl.`commission`, r.water_rate) AS commission,
	uhl.max_bet_first AS maxBetFirst,
	uhl.max_bet_second AS maxBetSecond,
	uhl.max_bet_second_min AS maxBetSecondAt,
	uhl.limit_digit AS limitDigit,
    uhl.max_bet_range AS maxBetRange,
    uhl.max_bet_item_range AS maxBetItemRange,
	uhl.status
FROM rebates r
INNER JOIN lotteries l ON l.code = r.type AND l.status = 1
LEFT JOIN user_has_lotteries uhl ON uhl.rc = r.code AND r.type = uhl.lottery_code AND uhl.uc = :userCode
WHERE IF(lower(:lotteryType) <> 'all', r.type = :lotteryType, TRUE)
ORDER BY r.type, r.sort_order