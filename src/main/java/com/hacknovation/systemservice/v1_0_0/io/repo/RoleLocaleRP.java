package com.hacknovation.systemservice.v1_0_0.io.repo;

import com.hacknovation.systemservice.v1_0_0.io.entity.RoleLocaleEntity;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.role.RoleDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleLocaleRP extends JpaRepository<RoleLocaleEntity, Long> {

    @Query(value = "SELECT r.id, r.code, rlc.name, GROUP_CONCAT( DISTINCT (ifnull(rhp.permission_id, 0)) ) AS permission FROM roles r LEFT JOIN role_has_permissions rhp ON r.id = rhp.role_id LEFT JOIN role_locales rlc on rlc.role_code = r.code WHERE r.code <> 'sub-account' AND r.code <> 'super-admin' AND r.guard_name = :userType AND CASE WHEN 'system' <> :userType THEN r.code IN (:filterRoles) ELSE TRUE END GROUP BY r.id, r.code, rlc.name ORDER BY r.id", nativeQuery = true)
    List<RoleDTO> getRoles(String userType, List<String> filterRoles);

    List<RoleLocaleEntity> findByRoleCode(String roleCode);
}
