package com.hacknovation.systemservice.v1_0_0.ui.model.request.role;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author : sombath
 * @created : 3/19/21, Friday
 **/

@Data
public class SetRolePermissionRQ {

    @NotBlank
    private String code;

    @NotBlank
    private List<Integer> permissions;
}
