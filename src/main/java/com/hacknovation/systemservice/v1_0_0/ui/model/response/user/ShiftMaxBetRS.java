package com.hacknovation.systemservice.v1_0_0.ui.model.response.user;

import lombok.Data;

import java.util.Date;

/*
 * author: kangto
 * createdAt: 08/03/2022
 * time: 14:09
 */
@Data
public class ShiftMaxBetRS {
    private Date drawAt;
    private Date maxBetSecondAt;
    private Date stopLoAt;
    private Date stopPostAt;
}
