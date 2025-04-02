package com.hacknovation.systemservice.v1_0_0.service.roles;

import com.hacknovation.systemservice.v1_0_0.ui.model.request.role.CreateRoleRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.role.SetRolePermissionRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.role.UpdateRoleRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleSV {
    StructureRS create(CreateRoleRQ request);
    StructureRS getPermissions();
    StructureRS update(String roleCode, UpdateRoleRQ request);
    StructureRS getRole(String roleCode);
    StructureRS getRole();
    StructureRS setRolesPermissions(List<SetRolePermissionRQ> requests);
}
