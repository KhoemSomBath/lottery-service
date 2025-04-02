package com.hacknovation.systemservice.v1_0_0.io.repo;

import com.hacknovation.systemservice.v1_0_0.io.entity.UserEntity;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.user.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRP extends JpaRepository<UserEntity, Long> {
    boolean existsByUsernameIgnoreCase(String username);

    @Query(value = "SELECT * FROM users WHERE id = :parentId", nativeQuery = true)
    UserEntity getUserByParentId(Integer parentId);

    @Query(value = "SELECT * FROM users ORDER BY id DESC LIMIT 1", nativeQuery = true)
    UserEntity getUser();

    @Query(value = "SELECT * FROM users WHERE username = ?1 ORDER BY id DESC LIMIT 1", nativeQuery = true)
    UserEntity getUserByUsername(String username);

    @Query(value = "SELECT * FROM users WHERE code = ?1 LIMIT 1", nativeQuery = true)
    Optional<UserEntity> getUserByCode(String code);

    UserEntity findByCode(String code);

    List<UserEntity> findAllByRoleCodeAndIsOnline(String role, Boolean isOnline);

    List<UserEntity> findAllByRoleCode(String role);

    @Query(value = "SELECT * FROM users WHERE agent_code = ?1 ORDER BY id DESC LIMIT 1", nativeQuery = true)
    UserEntity lastMember(String userCode);

    @Query(value = "SELECT * FROM users WHERE master_code = ?1 AND role_code = 'agent' ORDER BY id DESC LIMIT 1", nativeQuery = true)
    UserEntity lastAgent(String userCode);

    @Query(value = "SELECT * FROM users WHERE senior_code = ?1 AND role_code = 'master' ORDER BY id DESC LIMIT 1", nativeQuery = true)
    UserEntity lastMaster(String userCode);

    @Query(value = "SELECT * FROM users WHERE super_senior_code = ?1 AND role_code = 'senior' ORDER BY id DESC LIMIT 1", nativeQuery = true)
    UserEntity lastSenior(String userCode);

    @Query(value = "SELECT * FROM users WHERE code IN ?1", nativeQuery = true)
    List<UserEntity> getUserByCodeIn(List<String> codes);

    List<UserEntity> findAllByCodeOrSuperSeniorCode(String userCode, String superSeniorCode);

    @Query(nativeQuery = true,
            value = "SELECT u.`code` AS userCode,  " +
                    "       u.username,  " +
                    "       u.`role_code`       AS roleCode,  " +
                    "       u.`role_code`       AS roleName,  " +
                    "       u.`nickname`,  " +
                    "       u.language_code     AS languageCode,  " +
                    "       u.super_senior_code AS superSeniorCode,  " +
                    "       u.`senior_code`     AS seniorCode,  " +
                    "       u.`master_code`     AS masterCode,  " +
                    "       u.`agent_code`      AS agentCode,  " +
                    "       u.`parent_id`       AS parentId,  " +
                    "       u.user_type         AS type,  " +
                    "       u.lottery_type      AS lotteryType," +
                    "       u.platform_type     AS platformType," +
                    "       u.status,  " +
                    "       ifnull(t.balance_khr, 0) as balanceKhr, " +
                    "       ifnull(t.balance_usd, 0) as balanceUsd, " +
                    "       sum(if(tr.type = 'deposit' and tr.currency_code = 'khr', tr.amount, 0)) as depositKhr, " +
                    "       sum(if(tr.type = 'withdraw' and tr.currency_code = 'khr', tr.amount, 0)) as  withdrawKhr, " +
                    "       sum(if(tr.type = 'deposit' and tr.currency_code = 'usd', tr.amount, 0)) as depositUsd, " +
                    "       sum(if(tr.type = 'withdraw' and tr.currency_code = 'usd', tr.amount, 0)) as  withdrawUsd, " +
                    "       u.is_locked         AS isLocked,  " +
                    "       u.`created_at`      AS createdAt,  " +
                    "       u.last_login        AS lastLogin  " +
                    " from users u " +
                    " left join users loggedUser on loggedUser.code = :accessByUser " +
                    " left join transaction_balance t on t.user_code = u.code " +
                    " left join transactions tr on tr.user_code = u.code " +
                    "where u.user_type = :type AND u.role_code <> 'SUPER-ADMIN' " +
                    "  AND u.role_code = :roleCode  " +
                    "  and u.status = :status  " +
                    " and if(:userOnline, true, u.is_online = 0) and " +
                    " IF(:isSubAccount, u.parent_id = loggedUser.id, IF(:selectedUserCode <> 'all',      " +
                    "                              CASE      " +
                    "                                  WHEN :roleCode = 'senior' THEN      " +
                    "                                      u.super_senior_code = :selectedUserCode      " +
                    "                                  WHEN :roleCode = 'master' THEN      " +
                    "                                      u.senior_code = :selectedUserCode      " +
                    "                                  WHEN :roleCode = 'agent' THEN      " +
                    "                                      u.master_code = :selectedUserCode      " +
                    "                                  WHEN :roleCode = 'member' THEN      " +
                    "                                      u.agent_code = :selectedUserCode      " +
                    "                                  ELSE      " +
                    "                                      u.code = :selectedUserCode      " +
                    "                                  END,      " +
                    "                              IF(:accessByUserType <> 'SYSTEM',      " +
                    "                                 CASE      " +
                    "                                     WHEN :accessByRole = 'super-senior' THEN      " +
                    "                                         u.super_senior_code = :accessByUser      " +
                    "                                     WHEN :accessByRole = 'senior' THEN      " +
                    "                                         u.senior_code = :accessByUser      " +
                    "                                     WHEN :accessByRole = 'master' THEN      " +
                    "                                         u.master_code = :accessByUser      " +
                    "                                     WHEN :accessByRole = 'agent' THEN      " +
                    "                                         u.agent_code = :accessByUser      " +
                    "                                     ELSE      " +
                    "                                         u.code = :accessByUser      " +
                    "                                     END      " +
                    "                                  , TRUE)))  AND " +
                    " IF(:keyword <> 'all', u.username LIKE '%' :keyword '%'  " +
                    "    OR u.nickname LIKE '%' :keyword '%'  " +
                    "             OR u.code LIKE '%' :keyword '%', TRUE)  " +
                    "group by u.username",
            countQuery = "select count(*) from users u " +
                    " left join users loggedUser on loggedUser.code = :accessByUser " +
                    " left join transaction_balance t on t.user_code = u.code " +
                    " left join transactions tr on tr.user_code = u.code " +
                    "where u.user_type = :type AND u.role_code <> 'SUPER-ADMIN' " +
                    "  AND u.role_code = :roleCode " +
                    "  and u.status = :status  " +
                    " and if(:userOnline, true, u.is_online = 0) and " +
                    " IF(:isSubAccount, u.parent_id = loggedUser.id, IF(:selectedUserCode <> 'all',      " +
                    "                              CASE      " +
                    "                                  WHEN :roleCode = 'senior' THEN      " +
                    "                                      u.super_senior_code = :selectedUserCode      " +
                    "                                  WHEN :roleCode = 'master' THEN      " +
                    "                                      u.senior_code = :selectedUserCode      " +
                    "                                  WHEN :roleCode = 'agent' THEN      " +
                    "                                      u.master_code = :selectedUserCode      " +
                    "                                  WHEN :roleCode = 'member' THEN      " +
                    "                                      u.agent_code = :selectedUserCode      " +
                    "                                  ELSE      " +
                    "                                      u.code = :selectedUserCode      " +
                    "                                  END,      " +
                    "                              IF(:accessByUserType <> 'SYSTEM',      " +
                    "                                 CASE      " +
                    "                                     WHEN :accessByRole = 'super-senior' THEN      " +
                    "                                         u.super_senior_code = :accessByUser      " +
                    "                                     WHEN :accessByRole = 'senior' THEN      " +
                    "                                         u.senior_code = :accessByUser      " +
                    "                                     WHEN :accessByRole = 'master' THEN      " +
                    "                                         u.master_code = :accessByUser      " +
                    "                                     WHEN :accessByRole = 'agent' THEN      " +
                    "                                         u.agent_code = :accessByUser      " +
                    "                                     ELSE      " +
                    "                                         u.code = :accessByUser      " +
                    "                                     END      " +
                    "                                  , TRUE)))  AND " +
                    " IF(:keyword <> 'all', u.username LIKE '%' :keyword '%'  " +
                    "    OR u.nickname LIKE '%' :keyword '%'  " +
                    "             OR u.code LIKE '%' :keyword '%', TRUE)  " +
                    "group by u.username")
    Page<UserDTO> getUserReferral(String roleCode, String type, String keyword, String accessByUser, String accessByUserType, String accessByRole, String selectedUserCode, Boolean isSubAccount, Boolean userOnline, String status, Pageable pageable);

    @Query(value = "select * from users u where u.super_senior_code = :userCode or u.senior_code = :userCode or u.master_code = :userCode or u.agent_code = :userCode", nativeQuery = true)
    List<UserEntity> getAllByParentCode(String userCode);

    @Query(value = "select * from users u where u.role_code = 'member'", nativeQuery = true)
    List<UserEntity> getAllMember();

    @Transactional
    @Modifying
    @Query(value = "UPDATE users SET last_login=CURRENT_TIMESTAMP,last_login_app=CURRENT_TIMESTAMP,last_login_web=CURRENT_TIMESTAMP WHERE role_code = 'member' ", nativeQuery = true)
    void updateLastLoginAll();

    @Query(value = "select * from users u where u.role_code = :roleCode and if(:userType = 'system', true, u.super_senior_code = :userCode or u.senior_code = :userCode or u.master_code = :userCode or u.agent_code = :userCode) and if(:isCanSeeUserOnline, true, u.is_online = 0) and if(:username = 'all', true, u.username like concat(:username, '%')) order by u.username", nativeQuery = true)
    List<UserEntity> getUserByFilter(String userType, String userCode, String roleCode, String username, boolean isCanSeeUserOnline);
}
