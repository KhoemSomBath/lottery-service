package com.hacknovation.systemservice.v1_0_0.utility.auth;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;

@Data
public class Headers {

    private String platform;
    private String deviceId;
    private String currency;
    private String playerId;
    private String timeZone;
    private String localization;

    public Headers(HttpServletRequest request) {
        this.platform = request.getHeader("X-Platform");
        this.deviceId = request.getHeader("X-DeviceId") != null && !request.getHeader("X-DeviceId").equalsIgnoreCase("") ? request.getHeader("X-DeviceId") : null;
        this.currency = request.getHeader("X-Currency");
        this.playerId = request.getHeader("X-PlayerId") != null && !request.getHeader("X-PlayerId").equalsIgnoreCase("") ? request.getHeader("X-PlayerId") : null;
        this.timeZone = request.getHeader("X-TimeZone");
        this.localization = request.getHeader("X-Localization") != null && !request.getHeader("X-Localization").equalsIgnoreCase("") ? request.getHeader("X-Localization") : null;
    }

}
