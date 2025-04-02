package com.hacknovation.systemservice.v1_0_0.ui.model.response.result;

import lombok.Data;

import java.util.Date;

/*
 * author: kangto
 * createdAt: 14/09/2022
 * time: 13:47
 */
@Data
public class ResultRenderRS {
    private String drawCode;
    private Date drawAt;
    private Boolean isNight;
    private Date start;
    private Date end;
}
