SELECT
	lotteryType,
	userCode,
	superSeniorCode,
	seniorCode,
	masterCode,
	agentCode,
	rebateCode,
	CAST(memberCommission AS DECIMAL(15,2))  memberCommission,
	CAST(betAmountUsd AS DECIMAL(15,2)) betAmountUsd,
	CAST(betAmountKhr AS DECIMAL(15,2)) betAmountKhr,
	CAST(winAmountUsd AS DECIMAL(15,2)) winAmountUsd,
	CAST(winAmountKhr AS DECIMAL(15,2)) winAmountKhr

FROM (
	SELECT
		'VN2' lotteryType,
		oi.mc userCode,
		oi.rc rebateCode,
		oi.member_commission memberCommission,
        oi.member_rebate_rate memberRebateRate,
        SUM(IF(oi.cc = 'USD', oi.total_amount, 0)) betAmountUsd,
        SUM(IF(oi.cc = 'KHR', oi.total_amount, 0)) betAmountKhr,
		SUM(IF(oi.cc = 'USD', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0)) winAmountUsd,
		SUM(IF(oi.cc = 'KHR', IFNULL(lwoi.wq, 0) * oi.bet_amount, 0)) winAmountKhr

	FROM vntwo_order_items oi
		LEFT JOIN (SELECT oii, wq FROM vntwo_win_order_items) lwoi ON lwoi.oii = oi.id
	WHERE oi.mc IN :memberCodes
        AND IF(:filterDate <> '00:00 00:00:00', DATE(oi.draw_at) = DATE(:filterDate), true)
	GROUP BY oi.mc, oi.rc, oi.member_commission
) q1
INNER JOIN (SELECT code memberCode, super_senior_code superSeniorCode, senior_code seniorCode, master_code masterCode, agent_code agentCode FROM users) u ON u.memberCode = q1.userCode