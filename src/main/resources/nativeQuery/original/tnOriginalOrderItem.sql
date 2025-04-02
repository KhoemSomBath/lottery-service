SELECT ooi.id                                        originalId,
       lo.uc                                         userCode,
       lo.dc                                         drawCode,
       lo.draw_at                                    drawAt,
       ooi.pn                                        pageNumber,
       CONCAT(IF(ooi.is_lo, 'Lo', ''), ooi.posts) as posts,
       ooi.cc                                        currencyCode,
       ooi.rc                                        rebateCode,
       ooi.bet_amount                                betAmount,
       ooi.total_amount                              totalAmount,
       ooi.number_detail                             numberDetail,
       ooi.number_quantity                           numberQuantity
FROM tn_temp_order_items ooi
         INNER JOIN tn_temp_orders lo ON ooi.oi = lo.id AND ooi.cc = :filterByCurrency
WHERE date(lo.draw_at) = :filterDate
  AND lo.uc = :userCode
ORDER BY ooi.id