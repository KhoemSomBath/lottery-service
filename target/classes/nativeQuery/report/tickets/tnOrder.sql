SELECT vto.id,
       vto.uc            userCode,
       vto.tn            ticketNumber,
       vto.pn            pageNumber,
       vto.cn            columnNumber,
       vto.dc            drawCode,
       vto.draw_at       drawAt,
       vto.platform_name platformName,
       vto.platform_type platformType,
       vto.device_name   deviceName,
       vto.created_by    createdBy,
       vto.updated_by    updatedBy,
       vto.created_at    createdAt,
       vto.status
FROM tn_orders vto
LEFT JOIN tn_win_order_items vtwoi ON vto.id = vtwoi.oi
WHERE IF(:drawCode <> 'all', vto.dc = :drawCode, TRUE)
  AND vto.uc = :userCode
  AND IF(:pageNumber <> 'all', vto.pn = :pageNumber, TRUE)
  AND DATE (vto.draw_at) = DATE (:filterByDate)
  AND IF(lower(:isWin) <> 'false', vtwoi.wq > 0 , TRUE)
  AND IF(:isCancel != 'false', vto.status = 0 , vto.status != 0)
ORDER BY vto.id DESC