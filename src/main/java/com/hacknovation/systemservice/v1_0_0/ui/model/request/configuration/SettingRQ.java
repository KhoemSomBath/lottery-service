package com.hacknovation.systemservice.v1_0_0.ui.model.request.configuration;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/*
 * author: kangto
 * createdAt: 07/02/2022
 * time: 12:14
 */
@Data
public class SettingRQ {
    @NotBlank(message = "Please provide a code")
    private String code;
    private String name;
    @NotBlank(message = "Please provide a value")
    private String value;
    private String inputType;
    private String note;
}
