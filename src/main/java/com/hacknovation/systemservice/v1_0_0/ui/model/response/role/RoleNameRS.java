package com.hacknovation.systemservice.v1_0_0.ui.model.response.role;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;

@Data
public class RoleNameRS {
    @JsonProperty("locale")
    private String languageCode;
    private String name;
}
