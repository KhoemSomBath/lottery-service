package com.hacknovation.systemservice.v1_0_0.ui.model.dto;

import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

/*
 * author: kangto
 * createdAt: 10/01/2022
 * time: 11:32
 */
@Data
public class DrawingDTO {
    private BigInteger id;
    private String lottery;
    private String drawCode;
    private String shiftCode;
    private String postponeNumber;
    private Boolean isRecent;
    private Boolean isReleasedLo;
    private Boolean isReleasedPost;
    private Boolean isReleasedPostA;
    private Boolean isSetWin;
    private Date stoppedLoAt;
    private Date stoppedAAt;
    private Date stoppedPostAt;
    private Date resultedLoAt;
    private Date resultedPostAt;
    private Date resultedAAt;
    private Boolean isNight;
    private String status;

    public Boolean getIsNight() {
        if (isNight == null)
            return false;
        return isNight;
    }
}
