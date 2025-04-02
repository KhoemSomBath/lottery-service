SELECT
    u.`code`,
    u.username,
    u.role_code AS roleCode,
    u.super_senior_code AS superSeniorCode,
    u.senior_code AS seniorCode,
    u.master_code AS masterCode,
    u.agent_code AS agentCode,
    u.nickname,
    u.role_code AS roleName
FROM users u
WHERE CASE WHEN :userLevel <> 'all'
        THEN u.`role_code` = :userLevel
        ELSE u.username IS NOT NULL END
AND (u.`super_senior_code` = :userCode
OR u.`senior_code` = :userCode
OR u.`master_code` = :userCode
OR u.`agent_code` = :userCode)