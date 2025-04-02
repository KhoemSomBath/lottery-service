package com.hacknovation.systemservice.v1_0_0.service.roles;

import com.hacknovation.systemservice.constant.MessageConstant;
import com.hacknovation.systemservice.constant.UserConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.PermissionEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.RoleEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.RoleLocaleEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.RolePermissionEntity;
import com.hacknovation.systemservice.v1_0_0.io.repo.PermissionRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.RoleLocaleRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.RolePermissionRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.RoleRP;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.role.RoleDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.role.CreateRoleRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.role.SetRolePermissionRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.role.UpdateRoleRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.role.PermissionModuleRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.role.PermissionRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.role.RoleNameRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.role.RoleRS;
import com.hacknovation.systemservice.v1_0_0.utility.auth.JwtToken;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleIP extends BaseServiceIP implements RoleSV {

    private final RoleLocaleRP localeRP;
    private final HttpServletRequest request;
    private final RoleRP roleRP;
    private final PermissionRP permissionRP;
    private final RolePermissionRP rolePermissionRP;
    private final ModelMapper modelMapper;
    private final JwtToken jwtToken;

    @Override
    public StructureRS getRole() {
        String userType = request.getParameter("filterUserType") != null && !request.getParameter("filterUserType").isEmpty() ? request.getParameter("filterUserType") : "system";
        String roleCode = jwtToken.getUserToken().getUserRole();

        List<String> filterRoles = Arrays.asList(UserConstant.SUPER_SENIOR, UserConstant.SENIOR, UserConstant.MASTER, UserConstant.AGENT, UserConstant.MEMBER);
        if (roleCode.equalsIgnoreCase(UserConstant.SUPER_SENIOR)) {
            filterRoles = Arrays.asList(UserConstant.SENIOR, UserConstant.MASTER, UserConstant.AGENT, UserConstant.MEMBER);
        }
        if (roleCode.equalsIgnoreCase(UserConstant.SENIOR)) {
            filterRoles = Arrays.asList(UserConstant.MASTER, UserConstant.AGENT, UserConstant.MEMBER);
        }

        if (roleCode.equalsIgnoreCase(UserConstant.MASTER)) {
            filterRoles = Arrays.asList(UserConstant.AGENT, UserConstant.MEMBER);
        }

        if (roleCode.equalsIgnoreCase(UserConstant.AGENT)) {
            filterRoles = Collections.singletonList(UserConstant.MEMBER);
        }

        List<RoleDTO> roles = new ArrayList<>(localeRP.getRoles(userType, filterRoles));
        return responseBody(HttpStatus.OK, MessageConstant.SUCCESSFULLY, roles);
    }

    @Override
    public StructureRS create(CreateRoleRQ request) {

        Optional<RoleEntity> oldRole = roleRP.findByCode(request.getCode());
        if (oldRole.isPresent())
            return responseBody(HttpStatus.BAD_REQUEST, MessageConstant.ROLE_ALREADY_EXISTED);

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setGuardName(request.getGuard());
        roleEntity.setStatus(request.isStatus());
        roleEntity.setCode(request.getCode());

        roleRP.save(roleEntity);

        List<RoleLocaleEntity> locales = request.getNames()
                .stream()
                .map(roleName -> {
                    RoleLocaleEntity roleLocaleEntity = new RoleLocaleEntity();
                    roleLocaleEntity.setRoleCode(request.getCode());
                    roleLocaleEntity.setLanguageCode(roleName.getLocale());
                    roleLocaleEntity.setName(roleName.getName());
                    return roleLocaleEntity;
                }).collect(Collectors.toList());

        localeRP.saveAll(locales);

        return responseBody(HttpStatus.OK, MessageConstant.SUCCESSFULLY);
    }

    @Override
    public StructureRS getPermissions() {
        List<PermissionEntity> permissions = permissionRP.findAll();
        Map<String, List<PermissionEntity>> collect = permissions
                .parallelStream()
                .collect(Collectors.groupingBy(PermissionEntity::getModule));

        List<PermissionModuleRS> list = new ArrayList<>();
        collect.forEach((s, permissionEntities) ->
                list.add(new PermissionModuleRS(s,
                        permissionEntities
                                .stream()
                                .map(permissionEntity ->
                                        modelMapper.map(permissionEntity, PermissionRS.class))
                                .collect(Collectors.toList()))));

        return responseBodyWithSuccessMessage(list);
    }

    @Override
    public StructureRS update(String roleCode, UpdateRoleRQ request) {
        Optional<RoleEntity> oldRole = roleRP.findByCode(roleCode);
        if (oldRole.isEmpty())
            return responseBody(HttpStatus.NOT_FOUND, MessageConstant.ROLE_NOT_FOUND);

        if (request.getNames() != null && !request.getNames().isEmpty()) {
            List<RoleLocaleEntity> byRoleCode = localeRP.findByRoleCode(oldRole.get().getCode());
            localeRP.deleteInBatch(byRoleCode);

            List<RoleLocaleEntity> locales = request.getNames()
                    .stream()
                    .map(roleName -> {
                        RoleLocaleEntity roleLocaleEntity = new RoleLocaleEntity();
                        roleLocaleEntity.setRoleCode(oldRole.get().getCode());
                        roleLocaleEntity.setLanguageCode(roleName.getLocale());
                        roleLocaleEntity.setName(roleName.getName());
                        return roleLocaleEntity;
                    }).collect(Collectors.toList());

            localeRP.saveAll(locales);
        }

        return responseBodyWithSuccessMessage();
    }

    @Override
    public StructureRS getRole(String roleCode) {

        Optional<RoleEntity> roleEntity = roleRP.findByCode(roleCode);

        if (roleEntity.isEmpty())
            return responseBody(HttpStatus.NOT_FOUND, MessageConstant.ROLE_NOT_FOUND);

        List<RoleLocaleEntity> localeEntity = localeRP.findByRoleCode(roleCode);
        List<RoleNameRS> roleNameRS = localeEntity
                .stream()
                .map(roleLocaleEntity ->
                        modelMapper.map(roleLocaleEntity, RoleNameRS.class))
                .collect(Collectors.toList());

        List<PermissionEntity> permissionEntities = permissionRP.permissions(roleCode);
        List<PermissionRS> permissionRS =
                permissionEntities
                        .stream()
                        .map(permissionEntity -> modelMapper.map(permissionEntity, PermissionRS.class))
                        .collect(Collectors.toList());

        RoleRS roleRS = new RoleRS();
        roleRS.setCode(roleEntity.get().getCode());
        roleRS.setName(roleEntity.get().getName());
        roleRS.setNames(roleNameRS);
        roleRS.setStatus(roleEntity.get().getStatus());
        roleRS.setPermissions(permissionRS);

        return responseBodyWithSuccessMessage(roleRS);
    }

    @Override
    public StructureRS setRolesPermissions(List<SetRolePermissionRQ> requests) {
        for (SetRolePermissionRQ request : requests) {
            Optional<RoleEntity> oldRole = roleRP.findByCode(request.getCode());
            if (oldRole.isEmpty())
                return responseBody(HttpStatus.NOT_FOUND, MessageConstant.ROLE_NOT_FOUND);

            if (request.getPermissions() != null && !request.getPermissions().isEmpty()) {
                List<RolePermissionEntity> byRoleId = rolePermissionRP.findByRoleId(oldRole.get().getId());
                rolePermissionRP.deleteInBatch(byRoleId);

                List<RolePermissionEntity> rolePermissionEntities = request.getPermissions()
                        .stream()
                        .map(integer -> {
                            RolePermissionEntity rolePermissionEntity = new RolePermissionEntity();
                            rolePermissionEntity.setPermissionId(Long.valueOf(integer));
                            rolePermissionEntity.setRoleId(oldRole.get().getId());
                            return rolePermissionEntity;
                        })
                        .collect(Collectors.toList());

                rolePermissionRP.saveAll(rolePermissionEntities);
            }
        }
        return responseBodyWithSuccessMessage();
    }

}
