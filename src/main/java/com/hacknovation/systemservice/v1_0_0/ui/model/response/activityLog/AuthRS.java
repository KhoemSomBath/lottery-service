package com.hacknovation.systemservice.v1_0_0.ui.model.response.activityLog;

import lombok.Data;

import java.util.Date;

/*
 * author: kangto
 * createdAt: 28/12/2021
 * time: 15:27
 */
@Data
public class AuthRS {
    private String deviceType;
    private Date loginAt;
    private String ipAddress;
}
