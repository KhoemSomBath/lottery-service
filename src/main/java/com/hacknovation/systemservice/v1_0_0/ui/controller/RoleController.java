package com.hacknovation.systemservice.v1_0_0.ui.controller;

import com.hacknovation.systemservice.v1_0_0.service.roles.RoleSV;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.role.CreateRoleRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.role.SetRolePermissionRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.role.UpdateRoleRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1.0.0/roles")
public class RoleController extends BaseController {

    private final RoleSV roleSV;

    public RoleController(RoleSV roleSV) {
        this.roleSV = roleSV;
    }

    @GetMapping
    public ResponseEntity<StructureRS> listing() {
        return response(roleSV.getRole());
    }

    @GetMapping("/permissions")
    public ResponseEntity<StructureRS> getPermissions() { return response(roleSV.getPermissions()); }

    @PreAuthorize("@customSecurityExpressionRoot.can('create-role')")
    @PostMapping
    public ResponseEntity<StructureRS> create(@Valid @RequestBody CreateRoleRQ request) {
        return response(roleSV.create(request));
    }

    @GetMapping("{roleCode}")
    public ResponseEntity<StructureRS> get(@PathVariable String roleCode) {
        return response(roleSV.getRole(roleCode));
    }

    @PreAuthorize("@customSecurityExpressionRoot.can('edit-role')")
    @PutMapping("{roleCode}")
    public ResponseEntity<StructureRS> update(@PathVariable String roleCode, @RequestBody UpdateRoleRQ request) {
        return response(roleSV.update(roleCode, request));
    }

    @PreAuthorize("@customSecurityExpressionRoot.can('edit-role-permission')")
    @PutMapping("/set-permission")
    public ResponseEntity<StructureRS> setPermissions(@RequestBody @Valid List<SetRolePermissionRQ> requests) {
        return response(roleSV.setRolesPermissions(requests));
    }

}
