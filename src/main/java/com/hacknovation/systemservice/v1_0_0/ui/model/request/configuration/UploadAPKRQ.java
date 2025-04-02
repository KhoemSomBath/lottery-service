package com.hacknovation.systemservice.v1_0_0.ui.model.request.configuration;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/*
 * author: kangto
 * createdAt: 07/02/2022
 * time: 12:14
 */
@Data
public class UploadAPKRQ {
    @NotBlank(message = "Please provide string version")
    private String version;
    private Boolean isForce = Boolean.FALSE;
}
