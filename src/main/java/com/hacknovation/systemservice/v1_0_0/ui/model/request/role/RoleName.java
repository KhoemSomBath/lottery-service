package com.hacknovation.systemservice.v1_0_0.ui.model.request.role;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RoleName {

    @NotNull
    private String locale;

    @NotNull
    private String name;
}
