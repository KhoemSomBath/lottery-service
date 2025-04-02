package com.hacknovation.systemservice.v1_0_0.ui.model.request.draw;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.math.BigInteger;
import java.util.Date;

/*
 * author: kangto
 * createdAt: 10/01/2022
 * time: 11:32
 */
@Data
public class UpdateDrawingRQ {
    private String lotteryType;
    @NotEmpty(message = "Please provide a draw code")
    private String drawCode;
    @Min(value = 0)
    private Integer stoppedLoAt;
    @Min(value = 0)
    private Integer stoppedAAt;
    @Min(value = 0)
    private Integer stoppedPostAt;
    private String postponeNumber;
}
