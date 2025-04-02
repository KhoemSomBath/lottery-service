package com.hacknovation.systemservice.v1_0_0.ui.model.response.configuration;

import lombok.Data;

/*
 * author: kangto
 * createdAt: 08/02/2023
 * time: 12:17
 */
@Data
public class ConfigurationItemRS {
    private String code;
    private String name;
    private String value;
    private String inputType;
    private String note;

    public String getCode() {
        if (code != null)
            return code.toUpperCase();
        return null;
    }
}
