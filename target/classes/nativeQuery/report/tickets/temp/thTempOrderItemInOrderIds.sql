SELECT vtoi.id,
       vtoi.oi                               orderId,
       vtoi.dc                               drawCode,
       vtoi.draw_at                          drawAt,
       vtoi.tn                               ticketNumber,
       vtoi.pn                               pageNumber,
       vtoi.cn                               columnNumber,
       vtoi.sn                               sectionNumber,
       '' as posts,
       vtoi.post_analyze                     postAnalyze,
       vtoi.is_lo                            isLo,
       vtoi.multi_digit                      multiDigit,
       vtoi.bet_type                         betType,
       vtoi.bet_title                        betTitle,
       vtoi.rc                               rebateCode,
       vtoi.number_from                      numberFrom,
       vtoi.number_to                        numberTo,
       vtoi.number_three                     numberThree,
       vtoi.number_four                      numberFour,
       vtoi.number_detail                    numberDetail,
       vtoi.number_quantity                  numberQuantity,
       vtoi.cc                               currencyCode,
       vtoi.bet_amount                       betAmount,
       vtoi.total_amount                     totalAmount,
       vtoi.mc                               memberCode,
       vtoi.member_share                     share,
       vtoi.member_commission                commission,
       vtoi.member_water_rate                waterRate,
       vtoi.member_rebate_rate               rebateRate,
       vtoi.senior_commission                seniorCommission,
       vtoi.senior_rebate_rate               seniorRebateRate,
       vtoi.super_commission                 superSeniorCommission,
       vtoi.super_rebate_rate                superSeniorRebateRate,
       vtoi.master_commission                masterCommission,
       vtoi.master_rebate_rate               masterRebateRate,
       IFNULL(vtwoi.wq, 0) * vtoi.bet_amount winAmount,
       IFNULL(vtwoi.wq, 0) winQty,
       vtoi.status,
       vtoi.pair_status                      pairStatus
FROM th_temp_order_items vtoi
LEFT JOIN th_temp_win_order_items vtwoi ON vtoi.id = vtwoi.oii
WHERE vtoi.oi IN :orderIds
group by vtoi.id
ORDER BY vtoi.id DESC