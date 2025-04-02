package com.hacknovation.systemservice.v1_0_0.ui.model.response.drawing;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/*
 * author: kangto
 * createdAt: 10/01/2022
 * time: 18:03
 */
@Data
public class DrawRS {
    private Boolean isRecent;
    private String drawCode;
    private String postponeNumber;
    private Date drawAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date stoppedLoAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date stoppedAAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date stoppedPostAt;

    private Boolean isNight = Boolean.FALSE;
    private String status;
}
