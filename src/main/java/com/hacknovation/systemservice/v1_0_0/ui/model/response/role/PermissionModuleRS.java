package com.hacknovation.systemservice.v1_0_0.ui.model.response.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionModuleRS {
    private String module;
    private List<PermissionRS> permissions;
}
