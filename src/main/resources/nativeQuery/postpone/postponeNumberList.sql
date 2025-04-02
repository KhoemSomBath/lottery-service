SELECT lo.code          lotteryType,
       pn.number_detail numberDetail,
       pn.status
FROM lotteries lo
LEFT JOIN postpone_number pn ON lo.code = pn.lottery_type AND pn.status = 1
WHERE lo.status = 1 and lo.code <> 'leap'
ORDER BY lo.code
