package com.hacknovation.systemservice.v1_0_0.ui.model.response.role;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleRS {
    private String code;
    private String name;
    private boolean status;
    private List<PermissionRS> permissions;
    private List<RoleNameRS> names;
}
