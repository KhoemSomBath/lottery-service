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
WHERE u.`role_code` = :filterUserLevel
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
    END AND IF(:filterByUsername <> 'ALL', u.username LIKE :filterByUsername '%' , TRUE)
ORDER BY u.`username` ASC