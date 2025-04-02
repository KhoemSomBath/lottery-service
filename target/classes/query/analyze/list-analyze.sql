SELECT 'POST'                        postGroup,
       va.`pc`                       postCode,
       va.`rc`                       rebateCode,
       va.`number`                   number,
       SUM(va.`amount`)              betAmount,
       SUM(va.`prize`)               rewardAmount,
       SUM(va.`commission`)          waterAmount
FROM :analyze_tbl va
WHERE va.`di` = :drawId
GROUP BY va.`pc`, va.`number`
ORDER BY va.`pc`, va.`number`