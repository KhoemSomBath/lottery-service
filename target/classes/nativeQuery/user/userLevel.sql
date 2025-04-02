SELECT *
FROM (
    SELECT
    u.`code` userCode,
    u.username,
    u.`nickname`,
    u.`role_code` roleCode,
    u.super_senior_code superSeniorCode,
    u.`senior_code` seniorCode,
    u.`master_code` masterCode,
    u.`agent_code` agentCode,
    u.`currency_code` currencyCode,
    u.language_code languageCode,
    u.`parent_id` parentId,
    IFNULL(tb.balance, 0) balance,
    u.status,
    u.`user_type` type,
    u.lottery_type lotteryType,
    u.platform_type platformType,
    u.is_locked_screen isLockedScreen,
    u.last_login lastLogin,
    u.last_login_web lastLoginWeb,
    u.last_login_app lastLoginApp,
    u.`created_at` createdAt
    FROM users u
    LEFT JOIN `transaction_balance` tb ON tb.user_code = u.code
    WHERE u.`role_code` IN (SELECT r.`code` FROM roles r WHERE r.guard_name = 'front')

/* if (userLevel != "all") */
    AND u.role_code = :userLevel
/* endif  */
    AND CASE WHEN :userType <> 'system' THEN
            u.`super_senior_code` IN (:userCodes)
            OR u.`senior_code` IN (:userCodes)
            OR u.`master_code` IN (:userCodes)
            OR u.`agent_code` IN (:userCodes)
        ELSE
            CASE WHEN :accessByUser <> 'all' THEN
                    u.`super_senior_code` IN (:userCodes)
                    OR u.`senior_code` IN (:userCodes)
                    OR u.`master_code` IN (:userCodes)
                    OR u.`agent_code` IN (:userCodes)
                ELSE
                    u.username IS NOT NULL
                END
        END

    AND u.status = :status

    ORDER BY u.`code` DESC
) AS result

WHERE CASE WHEN :keyword <> 'all'
    THEN
        result.username LIKE '%' :keyword '%'
        OR result.roleCode LIKE '%' :keyword '%'
        OR result.nickname LIKE '%' :keyword '%'
        OR DATE(result.`createdAt`) LIKE '%' :keyword '%'
        OR result.userCode LIKE '%' :keyword '%'
        OR result.status LIKE '%' :keyword '%'
    ELSE
        TRUE
    END
ORDER BY result.username