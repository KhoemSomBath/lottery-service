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
    IFNULL(tb.balance_khr, 0) balanceKhr,
    IFNULL(tb.balance_usd, 0) balanceUsd,
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
    WHERE u.`role_code` IN (SELECT r.`code` FROM roles r WHERE r.guard_name = :guardName)
    AND CASE WHEN :userLevel <> 'all'
        THEN
            u.`role_code` = :userLevel
        ELSE
            u.username IS NOT NULL
        END
    ORDER BY u.`username` ASC
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
        result.username IS NOT NULL
    END