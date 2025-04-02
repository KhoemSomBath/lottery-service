SELECT 'VN1'                                                           lotteryType,
       voi.mc                                                          userCode,
       voi.pn                                                          pageNumber,
       voi.dc                                                          drawCode,
       voi.draw_at                                                     drawAt,
       voi.rc                                                          rebateCode,
       SUM(IF(voi.cc = 'KHR', voi.total_amount, 0))                    betAmountKhr,
       SUM(IF(voi.cc = 'USD', voi.total_amount, 0))                    betAmountUsd,
       voi.member_commission                                           commission,
       voi.member_rebate_rate                                          rebateRate,
       SUM(IF(voi.cc = 'KHR', IFNULL(vwoi.wq, 0) * voi.bet_amount, 0)) winAmountKhr,
       SUM(IF(voi.cc = 'USD', IFNULL(vwoi.wq, 0) * voi.bet_amount, 0)) winAmountUsd
FROM vnone_order_items voi
LEFT JOIN vnone_win_order_items vwoi ON voi.id = vwoi.oii
WHERE voi.mc IN :userCodes AND date(voi.draw_at) = date(:filterByDate)
GROUP BY voi.dc, voi.mc, voi.pn, voi.rc
ORDER BY voi.mc, voi.dc, voi.pn