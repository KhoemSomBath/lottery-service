SELECT vto.id,
       vto.uc            userCode,
       vto.tn            ticketNumber,
       vto.pn            pageNumber,
       vto.cn            columnNumber,
       vto.dc            drawCode,
       vto.draw_at       drawAt,
       vto.platform_name platformName,
       vto.platform_type platformType,
       vto.created_by    createdBy,
       vto.updated_by    updatedBy,
       vto.created_at    createdAt,
       vto.status
FROM tn_orders vto
LEFT JOIN tn_win_order_items vtwoi ON vto.id = vtwoi.oi
LEFT JOIN users u ON u.code = vto.uc
WHERE IF(:drawCode <> 'all', vto.dc = :drawCode, TRUE)
  and if(:userOnline, true, u.is_online = 0)
  AND IF(:isLevel, vto.uc IN :memberCodes, TRUE)
  AND DATE (vto.draw_at) = DATE (:filterByDate)
  AND IF(:keyword <> 'all', (u.username LIKE '%' :keyword '%' OR vto.tn LIKE '%' :keyword '%' OR vto.dc LIKE '%' :keyword '%'), TRUE)
  AND IF(lower(:isWin) <> 'false', vtwoi.wq > 0 , TRUE)
  AND IF(:isCancel != 'false', vto.status = 0 , vto.status != 0)
ORDER BY vto.id DESC