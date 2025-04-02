package com.hacknovation.systemservice.v1_0_0.ui.model.dto.role;

import java.util.Date;

/**
 * @author : sombath
 * @created : 3/19/21, Friday
 **/


public interface PermissionDTO {
    Long getId();
    String getName();
    String getRoleId();
    String getModule();
    Date getCreatedAt();
    Date getUpdatedAt();
}