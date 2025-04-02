SELECT lc              lotteryType,
       'all'           type,
       mc              userCode,
       sum(amount_khr) amountKhr,
       sum(amount_usd) amountUsd
FROM settlement_items
WHERE lc = :lotteryType
  AND mc IN :userCodes
  AND date (issued_at) < date (:filterByDate)
GROUP BY mc

