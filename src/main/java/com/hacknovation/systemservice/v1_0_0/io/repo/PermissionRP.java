package com.hacknovation.systemservice.v1_0_0.io.repo;

import com.hacknovation.systemservice.v1_0_0.io.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRP extends JpaRepository<PermissionEntity, Long> {
    @Query(value = "SELECT * FROM permissions p WHERE p.id IN (SELECT rhp.permission_id FROM role_has_permissions rhp WHERE rhp.role_id = (SELECT r2.id FROM roles r2 WHERE r2.code = ?1))", nativeQuery = true)
    List<PermissionEntity> permissions(String userRole);
}
