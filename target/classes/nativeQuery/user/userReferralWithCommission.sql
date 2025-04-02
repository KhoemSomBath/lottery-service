SELECT u.`code`             userCode,
       u.username,
       u.nickname,
       UPPER(u.`role_code`) roleCode,
       u.super_senior_code  superSeniorCode,
       u.senior_code        seniorCode,
       u.master_code        masterCode,
       u.agent_code         agentCode,
       u.status
FROM users u
         left join user_has_lotteries 2d on 2d.uc = u.code and 2d.rc = '2D'
         left join user_has_lotteries 3d on 3d.uc = u.code and 3d.rc = '3D'
WHERE u.`role_code` = :filterUserLevel
  AND IF(:filterByUsername <> 'ALL', u.username LIKE :filterByUsername '%' , TRUE)
  and if(:userOnline, true, u.is_online = 0)
  AND CASE
          WHEN :userType <> 'system' THEN
              IF(:filterUserCode <> 'ALL', (u.`super_senior_code` = :filterUserCode
                  OR u.`senior_code` = :filterUserCode
                  OR u.`master_code` = :filterUserCode
                  OR u.`agent_code` = :filterUserCode
                  OR u.code = :filterUserCode), (u.`super_senior_code` = :userCode
                  OR u.`senior_code` = :userCode
                  OR u.`master_code` = :userCode
                  OR u.`agent_code` = :userCode
                  OR u.code = :userCode))
          ELSE
              IF(:filterUserCode <> 'ALL', (u.`super_senior_code` = :filterUserCode
                  OR u.`senior_code` = :filterUserCode
                  OR u.`master_code` = :filterUserCode
                  OR u.`agent_code` = :filterUserCode
                  OR u.code = :filterUserCode), TRUE)
    END
  AND if(:rebateRateTwoD <> '' and :rebateRateThreeD <> '', 2d.rebate_rate = :rebateRateTwoD and 3d.rebate_rate = :rebateRateThreeD , TRUE)
GROUP BY u.id ORDER BY u.`username` ASC