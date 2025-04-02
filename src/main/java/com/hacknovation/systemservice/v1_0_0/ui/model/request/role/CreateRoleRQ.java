package com.hacknovation.systemservice.v1_0_0.ui.model.request.role;

import com.hacknovation.systemservice.exception.anotation.Include;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateRoleRQ {

    @NotNull
    private List<@Valid RoleName> names;

    @NotNull
    private String code;

    @Include(contains = "system,front", delimiter = ",")
    private String guard = "system";

    private boolean status = true;
}