package com.hacknovation.systemservice.v1_0_0.ui.model.request.role;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class UpdateRoleRQ {

    @NotBlank
    private List<@Valid RoleName> names;

    private boolean status = true;
}
